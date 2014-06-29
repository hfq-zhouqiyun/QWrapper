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

public class Wrapper_gjsairad001 implements QunarCrawler{
	
	private static final NameValuePair __EVENTTARGET = new NameValuePair("__EVENTTARGET","ControlGroupSearchView2$LinkButtonSubmit");
	private static final NameValuePair _authkey_ = new NameValuePair("_authkey_","null");
	private static final NameValuePair ControlGroupSearchView2$AvailabilitySearchInputSearchView2$CheckBoxUseMacDestination1 = new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$CheckBoxUseMacDestination1","");
	private static final NameValuePair ControlGroupSearchView2$AvailabilitySearchInputSearchView2$CheckBoxUseMacOrigin1 = new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$CheckBoxUseMacOrigin1","");
	private static final NameValuePair ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_ADT = new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_ADT","1");
	private static final NameValuePair ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_CHD = new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_CHD","0");
	private static final NameValuePair ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_INFANT = new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListPassengerType_INFANT","0");
	private static final NameValuePair ControlGroupSearchView2$AvailabilitySearchInputSearchView2$RadioButtonMarketStructure = new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$RadioButtonMarketStructure","RoundTrip");
	private static final NameValuePair ControlGroupSearchView2$AvailabilitySearchInputSearchView2$TextBoxPromoCode = new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$TextBoxPromoCode","CALLCENT");
	private static final NameValuePair culture = new NameValuePair("culture","en-US");
	private static final NameValuePair WebStore = new NameValuePair("WebStore","false");
	
	public static void main(String[] args) {

		FlightSearchParam searchParam = new FlightSearchParam();
		searchParam.setDep("CNF");
		searchParam.setArr("PMW");
		searchParam.setDepDate("2014-08-13");
		searchParam.setRetDate("2014-08-20");
		searchParam.setTimeOut("60000");
		searchParam.setToken("");
		
		String html = new  Wrapper_gjsairad001().getHtml(searchParam);
		ProcessResultInfo result = new ProcessResultInfo();
		result = new  Wrapper_gjsairad001().process(html,searchParam);
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

		String bookingUrlPre = "http://viajemais.voeazul.com.br/Search2.aspx";
		BookingResult bookingResult = new BookingResult();
		
		String[] depStr = arg0.getDepDate().split("-");
		String[] arrStr = arg0.getRetDate().split("-");
		
		BookingInfo bookingInfo = new BookingInfo();
		bookingInfo.setAction(bookingUrlPre);
		bookingInfo.setMethod("post");
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListMarketDay1",depStr[2]);
		map.put("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListMarketMonth1",depStr[0] + "-" + depStr[1]);
		map.put("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListMarketDay1",arrStr[2]);
		map.put("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListMarketMonth1",arrStr[0] + "-" + arrStr[1]);
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
		String[] depStr = arg0.getDepDate().split("-");
		String[] arrStr = arg0.getRetDate().split("-");
		
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

		 			new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListMarketDay1",depStr[2]),
		 			new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListMarketMonth1",depStr[0] + "-" + depStr[1]),
		 			new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListMarketDay2",arrStr[2]),
		 			new NameValuePair("ControlGroupSearchView2$AvailabilitySearchInputSearchView2$DropDownListMarketMonth2",arrStr[0] + "-" + arrStr[1]),
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
//		    post.releaseConnection();
		     
		    //手动完成转向
		    get = new QFGetMethod(strLoca);
		    String cookie = StringUtils.join(httpClient.getState().getCookies(),"; ");
			httpClient.getState().clearCookies();
			get.addRequestHeader("Cookie",cookie);
		    
		    get.setFollowRedirects(false);
		    
		       try {
		            int statusCode = httpClient.executeMethod(get);
		            
//		            System.err.println(get.getResponseBodyAsString());
		       } catch (Exception e) {
		            System.out.println(e.getMessage() + "   asdasdasdad");
		       }
//		    get.releaseConnection();
		    
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


	public ProcessResultInfo process(String html, FlightSearchParam param) {
		try { 
            FileOutputStream out = new FileOutputStream("C:\\FileName1.txt"); 
            out.write(html.getBytes()); 
            out.close(); 
        } catch (FileNotFoundException e) { 
            e.printStackTrace(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        }
		
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
		
		String[] jsonStr = StringUtils.substringsBetween(html, "<table class=\"info-table\">", "</table>");
		
		String[] outboundFlights = StringUtils.substringsBetween(jsonStr[0], "<tr class=\"flightInfo\">", "</tr>");
		String[] returnFlights = StringUtils.substringsBetween(jsonStr[1], "<tr class=\"flightInfo\">", "</tr>");
		
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
			
				//货币 EUR
				String Icon = "USD";
				
				String flightN = StringUtils.substringBetween(outF, "<div class=\"SegmentParam\" style=\"display:none\"><span>", "</span></div>");
				String leavTime = StringUtils.substringBetween(outF, "<div class=\"output\"><span>", "</span></div>");
				String landTime = StringUtils.substringBetween(outF, "<div class=\"arrival\"><span>", "</span></div>");
				String go_price = StringUtils.substringBetween(outF, "<span class=\"farePrice\">", "</span>");
				String gotaxPrice = StringUtils.substringBetween(outF, "boardingTaxes=\"", "\"");
				
				String[] flightStrings = StringUtils.substringsBetween(flightN, "{\"FlightNumber\":\"", "\",\"Departure\"");
				for (String fn:flightStrings){
					String flightNo = "AD" + fn.replaceAll("[^a-zA-Z\\d]", "");
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
						String reflightN = StringUtils.substringBetween(retF, "<div class=\"SegmentParam\" style=\"display:none\"><span>", "</span></div>");
						String releavTime = StringUtils.substringBetween(retF, "<div class=\"output\"><span>", "</span></div>");
						String relandTime = StringUtils.substringBetween(retF, "<div class=\"arrival\"><span>", "</span></div>");
						String re_price = StringUtils.substringBetween(retF, "<span class=\"farePrice\">", "</span>");
						String retaxPrice = StringUtils.substringBetween(retF, "boardingTaxes=\"", "\"");
						
						String[] reflightStrings = StringUtils.substringsBetween(reflightN, "{\"FlightNumber\":\"", "\",\"Departure\"");
						for (String fn:reflightStrings){
							String flightNo = "AD" + fn.replaceAll("[^a-zA-Z\\d]", "");
							if (!flightNoRetList.contains(flightNo)){
								FlightSegement reseg = new FlightSegement(flightNo);
								reseg.setDeptime(releavTime);
								reseg.setArrtime(relandTime);
								
								reseg.setDepDate(param.getRetDate());
								reseg.setArrDate(param.getRetDate());
								reseg.setDepairport(param.getDep());
								reseg.setArrairport(param.getArr());
								
								retSegs.add(reseg);
								flightNoRetList.add(flightNo);
							}
						}
						
						
						
						double outPrice = Double.parseDouble(go_price.replace(",", ""));
						double returnPrice = Double.parseDouble(re_price.replace(",", ""));
						double total = outPrice + returnPrice;
						
						double outtaxPrice = Double.parseDouble(gotaxPrice.replace(",", ""));
						double returntaxPrice = Double.parseDouble(retaxPrice.replace(",", ""));
						double totalTax = outtaxPrice + returntaxPrice;
						
						// 航班详细信息
						flightDetail.setDepdate(Date.valueOf(param.getDepDate()));
						flightDetail.setFlightno(flightNoList);
						flightDetail.setMonetaryunit(Icon);
						// 总价
						flightDetail.setPrice(Double.parseDouble(df.format(total)));
						flightDetail.setTax(Double.parseDouble(df.format(totalTax)));
						flightDetail.setDepcity(param.getDep());
						flightDetail.setArrcity(param.getArr());
						flightDetail.setWrapperid(param.getWrapperid());
						
						//去程价格
						flight.setOutboundPrice(outPrice);
						//返程价格
						flight.setReturnedPrice(returnPrice);
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
}
