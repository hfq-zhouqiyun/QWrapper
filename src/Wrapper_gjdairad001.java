package myjob;
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

import demo.Wrapper_gjdairbe001;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public class Wrapper_gjdairad001 implements QunarCrawler{
	private Logger logger = LoggerFactory.getLogger(Wrapper_gjdairbe001.class);
	
	private static final NameValuePair __EVENTTARGET = new NameValuePair("__EVENTTARGET","ControlGroupSearchView2$LinkButtonSubmit");
	private static final NameValuePair _authkey_ = new NameValuePair("_authkey_","null");
	private static final NameValuePair ControlGroupSearchView2$AvailabilitySearchInputSearchView2$CheckBoxUseMacDestination1 = new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$CheckBoxUseMacDestination1","");
	private static final NameValuePair ControlGroupSearchView2$AvailabilitySearchInputSearchView2$CheckBoxUseMacOrigin1 = new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$CheckBoxUseMacOrigin1","");
	private static final NameValuePair ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_ADT = new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_ADT","1");
	private static final NameValuePair ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_CHD = new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_CHD","0");
	private static final NameValuePair ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_INFANT = new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_INFANT","0");
	private static final NameValuePair ControlGroupSearchView2$AvailabilitySearchInputSearchView2$RadioButtonMarketStructure = new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$RadioButtonMarketStructure","OneWay");
	private static final NameValuePair ControlGroupSearchView2$AvailabilitySearchInputSearchView2$TextBoxPromoCode = new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$TextBoxPromoCode","CALLCENT");
	private static final NameValuePair culture = new NameValuePair("culture","en-US");
	private static final NameValuePair WebStore = new NameValuePair("WebStore","false");

	public static void main(String[] args) {

		FlightSearchParam searchParam = new FlightSearchParam();
		searchParam.setDep("CNF");
		searchParam.setArr("PMW");
		searchParam.setDepDate("2014-05-13");
		searchParam.setTimeOut("60000");
		searchParam.setToken("");
		
		String html = new  Wrapper_gjdairad001().getHtml(searchParam);

		ProcessResultInfo result = new ProcessResultInfo();
		result = new  Wrapper_gjdairad001().process(html,searchParam);
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

		String bookingUrlPre = "http://viajemais.voeazul.com.br/Search2.aspx";
		BookingResult bookingResult = new BookingResult();
		
		String[] dateStr = arg0.getDepDate().split("-");
		
		BookingInfo bookingInfo = new BookingInfo();
		bookingInfo.setAction(bookingUrlPre);
		bookingInfo.setMethod("post");
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListMarketDay1",dateStr[2]);
		map.put("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListMarketMonth1",dateStr[0] + "-" + dateStr[1]);
		map.put("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$TextBoxMarketDestination1",arg0.getDep());
		map.put("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$TextBoxMarketOrigin1",arg0.getArr());
		
		map.put("__EVENTTARGET","ControlGroupSearchView2$LinkButtonSubmit");
		map.put("_authkey_","null");
		map.put("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$CheckBoxUseMacDestination1","");
		map.put("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$CheckBoxUseMacOrigin1","");
		map.put("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_ADT","1");
		map.put("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_CHD","0");
		map.put("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_INFANT","0");
		map.put("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$RadioButtonMarketStructure","OneWay");
		map.put("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$TextBoxPromoCode","CALLCENT");
		map.put("culture","en-US");
		map.put("WebStore","false");
		
		bookingInfo.setInputs(map);		
		bookingResult.setData(bookingInfo);
		bookingResult.setRet(true);
		return bookingResult;

	}

	public String getHtml(FlightSearchParam arg0) {
		QFGetMethod get = null;	
		try {	
		QFHttpClient httpClient = new QFHttpClient(arg0, false);
		httpClient.getHostConfiguration().setHost("viajemais.voeazul.com.br");
		String getUrl = String.format("http://viajemais.voeazul.com.br/Search2.aspx");
	
		String data = arg0.getDepDate().replaceAll("-", "/");
		String[] dateStr = arg0.getDepDate().split("-");
		
		QFPostMethod post = null;
		post = new QFPostMethod(getUrl);
		post.setFollowRedirects(false);
		NameValuePair[] names = {
		 	__EVENTTARGET,
		 	_authkey_,
		 	ControlGroupSearchView2$AvailabilitySearchInputSearchView2$CheckBoxUseMacDestination1,
		 	ControlGroupSearchView2$AvailabilitySearchInputSearchView2$CheckBoxUseMacOrigin1,
		 	ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_ADT,
		 	ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_CHD,
		 	ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_INFANT,
		 	ControlGroupSearchView2$AvailabilitySearchInputSearchView2$RadioButtonMarketStructure,
		 	ControlGroupSearchView2$AvailabilitySearchInputSearchView2$TextBoxPromoCode,
		 	culture,
		 	WebStore,
		 	new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListMarketDay1",dateStr[2]),
		 	new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListMarketMonth1",dateStr[0] + "-" + dateStr[1]),
		 	new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$TextBoxMarketDestination1",arg0.getArr()),
		 	new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$TextBoxMarketOrigin1",arg0.getDep()),
		};
		
		post.setRequestBody(names);
		post.setRequestHeader("Origin", "http://www.voeazul.com.br");
		post.setRequestHeader("Referer", "http://www.voeazul.com.br/en/home");
		post.setRequestHeader("Host", "viajemais.voeazul.com.br");
		post.getParams().setContentCharset("UTF-8");
			
		int status = httpClient.executeMethod(post);
		String responseString = new String(post.getResponseBodyAsString().getBytes("UTF-8"));
		
		//获取response中location的值
		Header loca = post.getResponseHeader("location");
		String strLoca = loca.getValue();
		post.releaseConnection();
		     
		    //手动完成转向
		    get = new QFGetMethod(strLoca);
		    String cookie = StringUtils.join(httpClient.getState().getCookies(),"; ");
			httpClient.getState().clearCookies();
			get.addRequestHeader("Cookie",cookie);
		    get.setFollowRedirects(false);
		    
		    httpClient.executeMethod(get);
		    return get.getResponseBodyAsString();

		} catch (Exception e) {			
			logger.error(e.getMessage(), e);
		} finally{
			if (null != get){
				get.releaseConnection();
			}
		}
		return "Exception";
	}


	public ProcessResultInfo process(String arg0, FlightSearchParam arg1) {
		String html = arg0;
		
		/* ProcessResultInfo中，
		 * ret为true时，status可以为：SUCCESS(抓取到机票价格)|NO_RESULT(无结果，没有可卖的机票)
		 * ret为false时，status可以为:CONNECTION_FAIL|INVALID_DATE|INVALID_AIRLINE|PARSING_FAIL|PARAM_ERROR
		 */
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
		
		String flight = "";
		String leavTime = "";
		String landTime = "";
		String unit = "USD";
		String tmpPrice = "";
		String taxPrice = "";
		
		List<OneWayFlightInfo> flightList = new ArrayList<OneWayFlightInfo>();
		
		String[] jsonStr = StringUtils.substringsBetween(html, "<tr class=\"flightInfo\">", "</tr>");
		try {
			for (String tempStr:jsonStr){
				OneWayFlightInfo baseFlight = new OneWayFlightInfo();
				List<FlightSegement> segs = new ArrayList<FlightSegement>();
				FlightDetail flightDetail = new FlightDetail();
				List<String> flightNoList = new ArrayList<String>();	
				
				flight = StringUtils.substringBetween(tempStr, "<div class=\"SegmentParam\" style=\"display:none\"><span>", "</span></div>");
				leavTime = StringUtils.substringBetween(tempStr, "<div class=\"output\"><span>", "</span></div>");
				landTime = StringUtils.substringBetween(tempStr, "<div class=\"arrival\"><span>", "</span></div>");
				tmpPrice = StringUtils.substringBetween(tempStr, "<span class=\"farePrice\">", "</span>");
				taxPrice = StringUtils.substringBetween(tempStr, "boardingTaxes=\"", "\"");
				
				String[] flightStrings = StringUtils.substringsBetween(flight, "{\"FlightNumber\":\"", "\",\"Departure\"");
				for (String fn:flightStrings){
					String flightNo = "AD" + fn.replaceAll("[^a-zA-Z\\d]", "");
					if (!flightNoList.contains(flightNo)){
						FlightSegement seg = new FlightSegement(flightNo);
						seg.setDeptime(leavTime);
						seg.setArrtime(landTime);
						
						seg.setDepDate(arg1.getDepDate());
						seg.setDepairport(arg1.getDep());
						seg.setArrairport(arg1.getArr());
						
						segs.add(seg);
						flightNoList.add(flightNo);
					}
				}
					
				double tax = Double.parseDouble(taxPrice.replace(",", ""));
				double price = Double.parseDouble(tmpPrice.replace(",", ""));
				flightDetail.setDepdate(Date.valueOf(arg1.getDepDate()));
				flightDetail.setFlightno(flightNoList);
				flightDetail.setMonetaryunit(unit);
				flightDetail.setPrice(price);
				flightDetail.setTax(tax);
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
}