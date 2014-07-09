import com.qunar.qfwrapper.bean.booking.BookingResult;
import com.qunar.qfwrapper.bean.booking.BookingInfo;
import com.qunar.qfwrapper.bean.search.FlightSearchParam;
import com.qunar.qfwrapper.bean.search.ProcessResultInfo;
import com.qunar.qfwrapper.bean.search.OneWayFlightInfo;
import com.qunar.qfwrapper.bean.search.FlightDetail;
import com.qunar.qfwrapper.bean.search.FlightSegement;
import com.qunar.qfwrapper.bean.search.RoundTripFlightInfo;
import com.qunar.qfwrapper.interfaces.QunarCrawler;
import com.qunar.qfwrapper.util.QFGetMethod;
import com.qunar.qfwrapper.util.QFHttpClient;
import com.qunar.qfwrapper.util.QFPostMethod;
import com.qunar.qfwrapper.constants.Constants;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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
 * @date 2014-7-9 下午10:41:27
 */

public class Wrapper_gjsairpw001 implements QunarCrawler{
	
	public static void main(String[] args) {

		FlightSearchParam searchParam = new FlightSearchParam();
		searchParam.setDep("JRO");
		searchParam.setArr("NBO");
		searchParam.setDepDate("2014-08-02");
		searchParam.setRetDate("2014-08-03");
		searchParam.setTimeOut("60000");
		searchParam.setToken("");
		
		String html = new  Wrapper_gjsairpw001().getHtml(searchParam);
		ProcessResultInfo result = new ProcessResultInfo();
		result = new  Wrapper_gjsairpw001().process(html,searchParam);
		if(result.isRet() && result.getStatus().equals(Constants.SUCCESS))
		{
			List<RoundTripFlightInfo> flightList = (List<RoundTripFlightInfo>) result.getData();
			for (RoundTripFlightInfo in : flightList){
				System.out.println(in.getInfo().toString());
				System.out.println(in.getDetail().toString());
				System.out.println(in.toString());
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
		
		String[] depStr = arg0.getDepDate().split("-");
		String[] arrStr = arg0.getRetDate().split("-");
		
		BookingInfo bookingInfo = new BookingInfo();
		bookingInfo.setAction(bookingUrlPre);
		bookingInfo.setMethod("post");
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		
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
		
		String strParam = "R ^LF_NEW ^" + arg0.getDep() + " ^" + mapParam.get(arg0.getDep()) + " ^" + arg0.getArr() + " ^" + mapParam.get(arg0.getArr()) + " ^" + depStr[2] + " ^" + depStr[1] + "/" + depStr[0] + " ^" + arrStr[2] + " ^" + arrStr[1] + "/" + arrStr[0] +" ^1 ^0 ^0 ^Y ^NC ^false ^ ^Economy Class ^Non-Stop and Connecting ^BKG ^TZ ^false ^";
		map.put("hdnMode","BKG-LA");
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
			String[] depStr = arg0.getDepDate().split("-");
			String[] arrStr = arg0.getRetDate().split("-");
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
			
			String strParam = "R ^LF_NEW ^" + arg0.getDep() + " ^" + mapParam.get(arg0.getDep()) + " ^" + arg0.getArr() + " ^" + mapParam.get(arg0.getArr()) + " ^" + depStr[2] + " ^" + depStr[1] + "/" + depStr[0] + " ^" + arrStr[2] + " ^" + arrStr[1] + "/" + arrStr[0] +" ^1 ^0 ^0 ^Y ^NC ^false ^ ^Economy Class ^Non-Stop and Connecting ^BKG ^TZ ^false ^";
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


	public ProcessResultInfo process(String html, FlightSearchParam param) {
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
		
		try {
			return parser(html, param);
		} catch(Exception e){
			result.setRet(false);
			result.setStatus(Constants.PARSING_FAIL);
			return result;
		}
	}
	
	
	public ProcessResultInfo parser(String html, FlightSearchParam param) {
		ProcessResultInfo result = new ProcessResultInfo();		
		String[] Str = StringUtils.substringsBetween(html, "new Array();\narrFltInfo0", "[33]= '';");
		
		String tmpPrice = StringUtils.substringBetween(html, "arrPriceOptions[0][0] = '", "';");
		
		List<String> outboundFlights = new ArrayList<String>();
		List<String> returnFlights = new ArrayList<String>();
		Map<String, String> mapFlight = new LinkedHashMap<String, String>();
		for (int j = 0; j < Str.length; j++){
			String tmpStr = StringUtils.substringBetween(Str[j], "[2] = '", "';");
			String mf = StringUtils.substringBetween(Str[j], "[1] = '", "';");
			if (tmpStr.equals(param.getDep())){
				outboundFlights.add(Str[j]);
			}else{
				if (mapFlight.get(mf)==null){
					mapFlight.put(mf, mf);
					returnFlights.add(Str[j]);
				}
			}
		}
		
		DecimalFormat df = new DecimalFormat("#.00");
		
		List<RoundTripFlightInfo> flightList = new ArrayList<RoundTripFlightInfo>();
		//去程信息
		for(String outF : outboundFlights){
			RoundTripFlightInfo flight = new RoundTripFlightInfo();
			// 去程航段
			List<FlightSegement> segs = new ArrayList<FlightSegement>();
			// 返程航段
			List<FlightSegement> retSegs = new ArrayList<FlightSegement>();
			// 去程航班号
			List<String> flightNoList = new ArrayList<String>();
			// 返程航班号
			List<String> flightNoRetList = new ArrayList<String>();
			
			FlightDetail flightDetail = new FlightDetail();
			
				String Icon = "USD";
				String taxStr = "";
				String leavTime = "";
				String landTime = "";
				
				String[] flightStrings = StringUtils.substringsBetween(outF, "[1] = '", "';");
				leavTime = StringUtils.substringBetween(outF, "[7] = '", "';");
				landTime = StringUtils.substringBetween(outF, "[9] = '", "';");
				landTime = StringUtils.substringBetween(outF, "[9] = '", "';");
				
				for (String fn:flightStrings){
					String flightNo = fn.replaceAll("[^a-zA-Z\\d]", "");
					if (!flightNoList.contains(flightNo)){
						FlightSegement seg = new FlightSegement(flightNo);
						seg.setDeptime(leavTime);
						seg.setArrtime(landTime);
						
						seg.setDepDate(param.getDepDate());
						seg.setArrDate(param.getDepDate());
						seg.setDepairport(param.getDep());
						seg.setArrairport(param.getArr());
						
						segs.add(seg);
						flightNoList.add(flightNo);
					}
				}
				
				// 返程信息
				for (String retF : returnFlights) {
					
					
						String[] reflightStrings = StringUtils.substringsBetween(retF, "[1] = '", "';");
						leavTime = StringUtils.substringBetween(retF, "[7] = '", "';");
						landTime = StringUtils.substringBetween(retF, "[9] = '", "';");
						landTime = StringUtils.substringBetween(retF, "[9] = '", "';");
						
						for (String fn:reflightStrings){
							String flightNo = fn.replaceAll("[^a-zA-Z\\d]", "");
							if (!flightNoRetList.contains(flightNo)){
								FlightSegement reseg = new FlightSegement(flightNo);
								reseg.setDeptime(leavTime);
								reseg.setArrtime(landTime);
								
								reseg.setDepDate(param.getRetDate());
								reseg.setArrDate(param.getRetDate());
								reseg.setDepairport(param.getDep());
								reseg.setArrairport(param.getArr());
								
								retSegs.add(reseg);
								flightNoRetList.add(flightNo);
							}
						}
						
						
						
						double price = Double.parseDouble(tmpPrice.replace(",", ""));
						
						// 航班详细信息
						flightDetail.setDepdate(Date.valueOf(param.getDepDate()));
						flightDetail.setFlightno(flightNoList);
						flightDetail.setMonetaryunit(Icon);
						// 总价
						flightDetail.setPrice(Double.parseDouble(df.format(price)));
						flightDetail.setTax(0.00);
						flightDetail.setDepcity(param.getDep());
						flightDetail.setArrcity(param.getArr());
						flightDetail.setWrapperid(param.getWrapperid());
						
						//去程价格
						flight.setOutboundPrice(Double.parseDouble(df.format(0.00)));
						//返程价格
						flight.setReturnedPrice(Double.parseDouble(df.format(0.00)));
						flight.setDetail(flightDetail);
						flight.setRetdepdate(Date.valueOf(param.getRetDate()));
						flight.setInfo(segs);
						flight.setRetinfo(retSegs);
						flight.setRetflightno(flightNoRetList);
						
						flightList.add(flight);
				}
		}
		result.setRet(true);
		result.setStatus(Constants.SUCCESS);
		result.setData(flightList);
		return result;
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