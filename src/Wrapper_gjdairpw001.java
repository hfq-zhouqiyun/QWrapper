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
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

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

/**
 * 
 * @company Qunar
 * @author zhouqiyun
 * @version   V1.0
 * @date 2014-7-9 下午10:29:58
 */

public class Wrapper_gjdairpw001 implements QunarCrawler{

	public static void main(String[] args) {

		FlightSearchParam searchParam = new FlightSearchParam();
		searchParam.setDep("JRO");
		searchParam.setArr("NBO");
		searchParam.setDepDate("2014-08-02");
		searchParam.setTimeOut("60000");
		searchParam.setToken("");
		
		String html = new  Wrapper_gjdairpw001().getHtml(searchParam);

		ProcessResultInfo result = new ProcessResultInfo();
		result = new  Wrapper_gjdairpw001().process(html,searchParam);
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

		String bookingUrlPre = "https://bookings.precisionairtz.com/ibe/public/showAvailability.action";
		BookingResult bookingResult = new BookingResult();
		
		String[] dateStr = arg0.getDepDate().split("-");
		
		BookingInfo bookingInfo = new BookingInfo();
		bookingInfo.setAction(bookingUrlPre);
		bookingInfo.setMethod("post");
		
		Map<String, String> mapParam = new LinkedHashMap<String, String>();
		mapParam.put("ABV","Abuja");
		mapParam.put("ACC","Accra");
		mapParam.put("ADD","Addis Ababa");
		mapParam.put("ARK","Arusha");
		mapParam.put("BKZ","BUKOBA");
		mapParam.put("DAR","Daresaalam");
		mapParam.put("EBB","Entebbe");
		mapParam.put("CAN","Guangzhou");
		mapParam.put("AMS","Haarlemmermeer");
		mapParam.put("HRE","Harare");
		mapParam.put("JED","Jeddah");
		mapParam.put("KRT","Khartoum");
		mapParam.put("KGL","Kigali");
		mapParam.put("JRO","Kilimanjaro");
		mapParam.put("FIH","Kinshasa");
		mapParam.put("LOS","Lagos");
		mapParam.put("LLW","Lilongwe");
		mapParam.put("MPM","Maputo");
		mapParam.put("MBI","Mbeya");
		mapParam.put("MBA","Mombasa");
		mapParam.put("HAH","Moroni");
		mapParam.put("MYW","Mtwara");
		mapParam.put("MWZ","Mwanza");
		mapParam.put("NBO","Nairobi");
		mapParam.put("APL","Nampula");
		mapParam.put("POL","Pemba");
		mapParam.put("TKQ","Kigoma");
		mapParam.put("FBM","Lubumbashi");
		mapParam.put("LUN","Lusaka");
		mapParam.put("SHY","Shinyanga");
		mapParam.put("TBO","Tabora");
		mapParam.put("MUZ","Musoma");
		mapParam.put("ZNZ","Zanzibar");
				
		String strParam = "O ^LF_NEW ^" + arg0.getDep() + " ^" + mapParam.get(arg0.getDep()) + " ^" + arg0.getArr() + " ^" + mapParam.get(arg0.getArr()) + " ^" + dateStr[2] + " ^" + dateStr[1] + "/" + dateStr[0] +" ^ ^ ^1 ^0 ^0 ^Y ^NC ^false ^ ^Economy Class ^Non-Stop and Connecting ^BKG ^TZ ^false ^";		
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("hdnMode","BKG");
		map.put("hdnVoucherData","showAvailability.action");
		map.put("hdnData",strParam);
		
		bookingInfo.setInputs(map);		
		bookingResult.setData(bookingInfo);
		bookingResult.setRet(true);
		return bookingResult;

	}

	public String getHtml(FlightSearchParam arg0) {
		QFPostMethod post = null;	
		try {
			String[] dateStr = arg0.getDepDate().split("-");
			QFHttpClient httpClient = new QFHttpClient(arg0, false);
			httpClient.getHostConfiguration().setHost("bookings.precisionairtz.com");
			
			String getUrl = String.format("https://bookings.precisionairtz.com/ibe/public/showAvailability.action");
			post = new QFPostMethod(getUrl);
			post.setFollowRedirects(false);
			
			Map<String, String> mapParam = new LinkedHashMap<String, String>();
			mapParam.put("ABV","Abuja");
			mapParam.put("ACC","Accra");
			mapParam.put("ADD","Addis Ababa");
			mapParam.put("ARK","Arusha");
			mapParam.put("BKZ","BUKOBA");
			mapParam.put("DAR","Daresaalam");
			mapParam.put("EBB","Entebbe");
			mapParam.put("CAN","Guangzhou");
			mapParam.put("AMS","Haarlemmermeer");
			mapParam.put("HRE","Harare");
			mapParam.put("JED","Jeddah");
			mapParam.put("KRT","Khartoum");
			mapParam.put("KGL","Kigali");
			mapParam.put("JRO","Kilimanjaro");
			mapParam.put("FIH","Kinshasa");
			mapParam.put("LOS","Lagos");
			mapParam.put("LLW","Lilongwe");
			mapParam.put("MPM","Maputo");
			mapParam.put("MBI","Mbeya");
			mapParam.put("MBA","Mombasa");
			mapParam.put("HAH","Moroni");
			mapParam.put("MYW","Mtwara");
			mapParam.put("MWZ","Mwanza");
			mapParam.put("NBO","Nairobi");
			mapParam.put("APL","Nampula");
			mapParam.put("POL","Pemba");
			mapParam.put("TKQ","Kigoma");
			mapParam.put("FBM","Lubumbashi");
			mapParam.put("LUN","Lusaka");
			mapParam.put("SHY","Shinyanga");
			mapParam.put("TBO","Tabora");
			mapParam.put("MUZ","Musoma");
			mapParam.put("ZNZ","Zanzibar");
			
			String strParam = "O ^LF_NEW ^" + arg0.getDep() + " ^" + mapParam.get(arg0.getDep()) + " ^" + arg0.getArr() + " ^" + mapParam.get(arg0.getArr()) + " ^" + dateStr[2] + " ^" + dateStr[1] + "/" + dateStr[0] +" ^ ^ ^1 ^0 ^0 ^Y ^NC ^false ^ ^Economy Class ^Non-Stop and Connecting ^BKG ^TZ ^false ^";
			NameValuePair[] names = {
					new NameValuePair("hdnMode","BKG"),
					new NameValuePair("hdnVoucherData","showAvailability.action"),
					new NameValuePair("hdnData",strParam),
			};
				
			post.setRequestBody(names);
			post.setRequestHeader("Origin", "https://bookings.precisionairtz.com");
			post.setRequestHeader("Referer", "https://bookings.precisionairtz.com/ibe/public/showIBEHome.action");
			post.setRequestHeader("Host", "bookings.precisionairtz.com");
			post.getParams().setContentCharset("UTF-8");
		    
		    httpClient.executeMethod(post);
//		    System.err.println(post.getResponseBodyAsString());
		    return post.getResponseBodyAsString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (null != post){
				post.releaseConnection();
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
		
		String[] Str = StringUtils.substringsBetween(html, "new Array();\narrFltInfo0", "[33]= '';");
		
		String tmpPrice = StringUtils.substringBetween(html, "arrPriceOptions[0][0] = '", "';");
		System.err.println(html);
		try {
			for (int j = 0; j < Str.length; j++){
				OneWayFlightInfo baseFlight = new OneWayFlightInfo();
				List<FlightSegement> segs = new ArrayList<FlightSegement>();
				FlightDetail flightDetail = new FlightDetail();
				List<String> flightNoList = new ArrayList<String>();	
				
				String flight = StringUtils.substringBetween(Str[j], "[1] = '", "';");
				
				leavTime = StringUtils.substringBetween(Str[j], "[7] = '", "';");
				landTime = StringUtils.substringBetween(Str[j], "[9] = '", "';");
				
				String flightNo = flight.replaceAll("[^a-zA-Z\\d]", "");
				FlightSegement seg = new FlightSegement(flightNo);
				seg.setDeptime(leavTime);
				seg.setArrtime(landTime);
				seg.setDepDate(arg1.getDepDate());
				seg.setArrDate(arg1.getDepDate());
				seg.setDepairport(arg1.getDep());
				seg.setArrairport(arg1.getArr());
				segs.add(seg);
				flightNoList.add(flightNo);
					
				double price = Double.parseDouble(tmpPrice.replace(",", ""));
				flightDetail.setDepdate(Date.valueOf(arg1.getDepDate()));
				flightDetail.setFlightno(flightNoList);
				flightDetail.setMonetaryunit(unit);
				flightDetail.setPrice(Double.parseDouble(df.format(price)));
				flightDetail.setTax(Double.parseDouble(df.format(0.00)));
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
