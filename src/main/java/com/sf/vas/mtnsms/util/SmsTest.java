/**
 * 
 */
package com.sf.vas.mtnsms.util;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.csapi.schema.parlayx.common.v2_1.ChargingInformation;
import org.csapi.schema.parlayx.common.v2_1.SimpleReference;
import org.csapi.schema.parlayx.sms.send.v2_2.local.SendSms;
import org.csapi.schema.parlayx.sms.send.v2_2.local.SendSmsResponse;
import org.csapi.wsdl.parlayx.sms.send.v2_2.service.PolicyException;
import org.csapi.wsdl.parlayx.sms.send.v2_2.service.SendSmsService;
import org.csapi.wsdl.parlayx.sms.send.v2_2.service.ServiceException;

import com.sf.vas.mtnsms.jaxb.commonheadtypes.RequestSOAPHeader;
import com.sf.vas.mtnsms.util.SecurityUtil;

/**
 * @author dawuzi
 *
 */
public class SmsTest {

	public static RequestSOAPHeader createHeader(){
		
//		RequestSOAPHeaderE requestHeaderE = new RequestSOAPHeader();
		RequestSOAPHeader requestHeader = new RequestSOAPHeader();
		String spId = "35000001";
		String serviceId = "35000001000001";
		String spPassword = "123456";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String created = sdf.format(Calendar.getInstance().getTime());
		String password = SecurityUtil.md5(spId + spPassword + created);
		requestHeader.setSpId(spId);
		requestHeader.setSpPassword(password);
		requestHeader.setServiceId(serviceId);
		requestHeader.setTimeStamp(created);
		requestHeader.setOA("8613300000010");
		requestHeader.setFA("8613300000010");
//		requestHeaderE.setRequestSOAPHeader(requestHeader);
		return requestHeader;		
	}

	public static SendSms createBody() throws URISyntaxException {
		
		URI address = new URI("tel:86122333");
		URI endpoint = new URI("http://10.137.213.69:8080/sms");

		ChargingInformation charging = new ChargingInformation();
		charging.setAmount(new BigDecimal(1));
		charging.setCode("111");
		charging.setCurrency("RMB");
		charging.setDescription("description");

		SimpleReference sim = new SimpleReference();
		sim.setCorrelator("123");
		sim.setEndpoint(endpoint.toString());
		sim.setInterfaceName("SmsNotification");

		SendSms param = new SendSms();

		param.getAddresses().add(address.toString());
//		param.addAddresses(address);
		param.setCharging(charging);
//		param.setData_coding(0);
		param.setDataCoding(0);
		param.setDestinationport(0);
		param.setEncode("utf-8");
//		param.setEsm_class(1);
		param.setEsmClass(1);
		param.setMessage("send a message!");
		param.setReceiptRequest(sim);
		param.setSenderName("1111");
		param.setSourceport(123);

//		SendSmsE sendSms = new SendSmsE();
//		sendSms.setSendSms(param);
		return param;

	}
	
	public static SendSmsResponse sendSms(RequestSOAPHeader header, SendSms body) throws PolicyException, ServiceException, MalformedURLException{
		SendSmsService sendSmsService = new SendSmsService(new URL("http://localhost:8088/mockSendSmsBinding"));
		System.out.println("got here");
		org.csapi.wsdl.parlayx.sms.send.v2_2.service.SendSms sendSms = sendSmsService.getSendSms();
		
		return sendSms.sendSms(body);
	}
	
	public static void main(String[] args) throws Exception {
		SendSmsResponse smsResponse = sendSms(null, null);
		
		System.out.println("smsResponse : "+smsResponse);
	}
}
