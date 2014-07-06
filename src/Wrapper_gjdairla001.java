import com.qunar.qfwrapper.bean.booking.BookingResult;
import com.qunar.qfwrapper.bean.booking.BookingInfo;
import com.qunar.qfwrapper.bean.search.FlightSearchParam;
import com.qunar.qfwrapper.bean.search.ProcessResultInfo;
import com.qunar.qfwrapper.bean.search.OneWayFlightInfo;
import com.qunar.qfwrapper.bean.search.FlightDetail;
import com.qunar.qfwrapper.bean.search.FlightSegement;
import com.qunar.qfwrapper.interfaces.QunarCrawler;
import com.qunar.qfwrapper.util.QFGetMethod;
import com.qunar.qfwrapper.util.QFHttpClient;
import com.qunar.qfwrapper.util.QFPostMethod;
import com.qunar.qfwrapper.constants.Constants;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public class Wrapper_gjdairla001 implements QunarCrawler{

	public static void main(String[] args) {

		FlightSearchParam searchParam = new FlightSearchParam();
		searchParam.setDep("MDE");
		searchParam.setArr("GPS");
		searchParam.setDepDate("2014-08-13");
		searchParam.setTimeOut("60000");
		searchParam.setToken("");
		
		String html = new  Wrapper_gjdairla001().getHtml(searchParam);

		ProcessResultInfo result = new ProcessResultInfo();
		result = new  Wrapper_gjdairla001().process(html,searchParam);
		if(result.isRet() && result.getStatus().equals(Constants.SUCCESS))
		{
			List<OneWayFlightInfo> flightList = (List<OneWayFlightInfo>) result.getData();
			for (OneWayFlightInfo in : flightList){
				System.out.println(in.getInfo().toString());
				System.out.println(in.getDetail().toString());
			}
		}
		else
		{
			System.out.println(result.getStatus());
		}
	}
	
	public BookingResult getBookingInfo(FlightSearchParam arg0) {

		String bookingUrlPre = "http://booking.lan.com/cgi-bin/compra/paso2.cgi";
		BookingResult bookingResult = new BookingResult();
		
		String[] dateStr = arg0.getDepDate().split("-");
		
		BookingInfo bookingInfo = new BookingInfo();
		bookingInfo.setAction(bookingUrlPre);
		bookingInfo.setMethod("post");
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("from_city1",arg0.getDep());
		map.put("to_city1",arg0.getArr());
		map.put("fecha1_dia",dateStr[2]);
		map.put("fecha1_anomes",dateStr[0] + "-" + dateStr[1]);
		map.put("url_promo","");
		map.put("reserva","");
		map.put("otras_ciudades","");
		map.put("num_segmentos_interfaz","1");
		map.put("nchildren","0");
		map.put("ninfants","0");
		map.put("tipo_paso2","flex");
		map.put("flex","0");
		map.put("no_tarifas_promocionales","1");
		map.put("mas_barato_owflex_ida","1");
		map.put("mas_barato_owflex_ida_farebasis","HEEFXZ0K-LA");
		map.put("fecha2_dia","");
		map.put("fecha2_anomes","");
		map.put("mas_barato_owflex_vuelta","");
		map.put("mas_barato_owflex_vuelta_farebasis","");
		
		bookingInfo.setInputs(map);		
		bookingResult.setData(bookingInfo);
		bookingResult.setRet(true);
		return bookingResult;

	}

	public String getHtml(FlightSearchParam arg0) {
		QFGetMethod get = null;	
		try {
			String[] dateStr = arg0.getDepDate().split("-");
			QFHttpClient httpClient = new QFHttpClient(arg0, false);
			httpClient.getHostConfiguration().setHost("booking.lan.com");			
			String getUrl = String.format("http://booking.lan.com/cgi-bin/compra/paso2.cgi?url_promo=&reserva=&otras_ciudades=&num_segmentos_interfaz=1&from_city1=%s&to_city1=%s&nadults=1&nchildren=0&ninfants=0&tipo_paso2=flex&flex=0&no_tarifas_promocionales=1&fecha1_dia=%s&fecha1_anomes=%s&fecha2_dia=&fecha2_anomes=&mas_barato_owflex_ida=1&mas_barato_owflex_vuelta=&mas_barato_owflex_ida_farebasis=HEEFXZ0K-LA&mas_barato_owflex_vuelta_farebasis=",arg0.getDep(),arg0.getArr(),dateStr[2],dateStr[0] + "-" + dateStr[1]);
			get = new QFGetMethod(getUrl);
			get.setRequestHeader("Referer", "http://www.lan.com/en_us/sitio_personas/index.html");
			get.getParams().setContentCharset("UTF-8");
		    
		    httpClient.executeMethod(get);
		    return get.getResponseBodyAsString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (null != get){
				get.releaseConnection();
			}
		}
		return "Exception";
	}


	public ProcessResultInfo process(String arg0, FlightSearchParam arg1) {
		String html = arg0;
		
		ProcessResultInfo result = new ProcessResultInfo();
		if ("Exception".equals(html)) {	
			result.setRet(false);
			result.setStatus(Constants.CONNECTION_FAIL);
			return result;			
		}		
		//需要有明显的提示语句，才能判断是否INVALID_DATE|INVALID_AIRLINE|NO_RESULT
		if (html.contains("Today Flight is full, select an other day or check later for any seat released. ")) {
			result.setRet(false);
			result.setStatus(Constants.INVALID_DATE);
			return result;			
		}
		
		String leavTime = "";
		String landTime = "";
		String unit = "USD";
		String taxStr = "";
		String taxPrice = "";
		
		DecimalFormat df = new DecimalFormat("#.00");
		
		List<OneWayFlightInfo> flightList = new ArrayList<OneWayFlightInfo>();
		
		String[] Str = StringUtils.substringsBetween(html, "<table class=\"table\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\">", "</table>");
		try {
			String[] jsonStr = StringUtils.substringsBetween(Str[0], "<tr>", "</tr>");
			for (int j = 5; j < jsonStr.length; j++){
				OneWayFlightInfo baseFlight = new OneWayFlightInfo();
				List<FlightSegement> segs = new ArrayList<FlightSegement>();
				FlightDetail flightDetail = new FlightDetail();
				List<String> flightNoList = new ArrayList<String>();	
				
				String[] flight = StringUtils.substringsBetween(jsonStr[j], "<strong>", "</strong>");
				String[] strTime = StringUtils.substringsBetween(jsonStr[j], "<span style=\"vertical-align:top\">", "</span>");
				leavTime = strTime[0];
				landTime = StringUtils.substringBetween(strTime[1], "", "<br");
				
				String tmpPrice = getPrice(jsonStr[j]);
				taxStr = StringUtils.substringBetween(jsonStr[j], "Array($H({CO:$H({", "aplicacion:'FARE'})})))");
				String[] taxmono = StringUtils.substringsBetween(taxStr, "monto:", " ,");
				String[] taxporcentaje = StringUtils.substringsBetween(taxStr, "porcentaje:", ",");
				
				double mono = 0.00;
				for (String ta:taxmono){
					mono += Double.parseDouble(ta.replace(",", ""));
				}
				
				double porcentaje = 0.00;
				for (String ta:taxporcentaje){
					porcentaje += Double.parseDouble(ta.replace(",", ""));
				}
				
				double tax = mono + Double.parseDouble(tmpPrice.replace(",", "")) * porcentaje / 100;
				
				for (String fn:flight){
					String flightNo = fn.replaceAll("[^a-zA-Z\\d]", "");
					if (!flightNoList.contains(flightNo)){
						FlightSegement seg = new FlightSegement(flightNo);
						seg.setDeptime(leavTime);
						seg.setArrtime(landTime);
						
						seg.setDepDate(arg1.getDepDate());
						seg.setArrDate(arg1.getDepDate());
						seg.setDepairport(arg1.getDep());
						seg.setArrairport(arg1.getArr());
						
						segs.add(seg);
						flightNoList.add(flightNo);
					}
				}
					
				double price = Double.parseDouble(tmpPrice.replace(",", ""));
				flightDetail.setDepdate(Date.valueOf(arg1.getDepDate()));
				flightDetail.setFlightno(flightNoList);
				flightDetail.setMonetaryunit(unit);
				flightDetail.setPrice(Double.parseDouble(df.format(price)));
				flightDetail.setTax(Double.parseDouble(df.format(tax)));
				flightDetail.setDepcity(arg1.getDep());
				flightDetail.setArrcity(arg1.getArr());
				flightDetail.setWrapperid(arg1.getWrapperid());
				baseFlight.setDetail(flightDetail);
				baseFlight.setInfo(segs);
				flightList.add(baseFlight);
			}
			result.setRet(true);
			result.setStatus(Constants.SUCCESS);
			result.setData(flightList);
			return result;
		} catch(Exception e){
			result.setRet(false);
			result.setStatus(Constants.PARSING_FAIL);
			return result;
		}
	}
	
	public String getPrice(String html) {
		String price = "";
		String[] tmpPrice = StringUtils.substringsBetween(html, "tdfamilia", "</td>");
		for (String tp:tmpPrice){
			String strPrice = StringUtils.substringBetween(tp, "valor_tarifa_seg", "/>");
			if (strPrice != null){
				price = StringUtils.substringBetween(strPrice, "value=\"", "\"");
				break;
			}
		}
		return price;
	}
}
