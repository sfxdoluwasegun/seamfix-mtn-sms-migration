/**
 * 
 */
package com.sf.vas.mtnsms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.sf.vas.atjpa.entities.SmsLog;
import com.sf.vas.atjpa.enums.Status;
import com.sf.vas.mtnsms.enums.SmsResponseCode;
import com.sf.vas.mtnsms.enums.SmsSetting;
import com.sf.vas.mtnsms.soapartifacts.sendservice.ObjectFactory;
import com.sf.vas.mtnsms.soapartifacts.sendservice.SendSms;
import com.sf.vas.mtnsms.soapartifacts.sendservice.SendSmsResponse;
import com.sf.vas.mtnsms.soapartifacts.sendservice.SendSms_Type;
import com.sf.vas.mtnsms.soapartifacts.sendservice.SimpleReference;
import com.sf.vas.mtnsms.tools.SmsMtnQueryService;
import com.sf.vas.utils.enums.SmsProps;
import com.sf.vas.utils.exception.VasException;
import com.sf.vas.utils.properties.VasProperties;
import com.sf.vas.utils.restartifacts.TransactionResponse;
import com.sf.vas.utils.restartifacts.sms.SmsRequest;

/**
 * @author dawuzi
 *
 */
@Stateless
@EJB(name="SmsMtnService", beanInterface = SmsMtnService.class)
public class SmsMtnService {
	
	private Logger log = Logger.getLogger(getClass());
	private ObjectFactory objectFactory = new ObjectFactory();
	private VasProperties vasProperties = new VasProperties();
	private boolean initialized = false;
	private String smsPropsFile = System.getProperty("jboss.home.dir")+File.separator+"bin"+File.separator+"sms.properties";
	private File file = new File(smsPropsFile);
	private long lastKnownModifiedTime = 0;
	
	@Inject
	SmsMtnSoapService soapService;
	
	@Inject
	private SmsMtnQueryService queryService;
	
	@PostConstruct
	private void init(){
		initProperties();
	}

	private void initProperties() {
		
		if(!file.exists()){
			log.warn("sms properties file does not exist"); 
			return;
		}	
		
//		if it has been initialized and the file is not modified
		long lastModified = file.lastModified();
		if(initialized && (lastKnownModifiedTime == lastModified)){
			return;
		}
		
		initialized = true;
		lastKnownModifiedTime = lastModified;
		
		try {
			log.info("========== RELOADING SMS PROPERTIES ==========");
			vasProperties.initProps(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException", e);
		} catch (IOException e) {
			log.error("IOException", e);
		}
	}
	
	public void sendSms(SmsProps smsProps, String msisdn, String param, String value) throws VasException {
		initProperties();
		
		String message = vasProperties.getProperty(smsProps.getKey(), smsProps.getDefaultValue(), param, value);
		
		SmsRequest smsRequest = new SmsRequest();
		
		smsRequest.setMessage(message);
		smsRequest.setMsisdn(msisdn);
		
		log.info("msisdn : "+msisdn+", message : "+message);
		
		sendSms(smsRequest);
	}
	
	public void sendSms(SmsProps smsProps, String msisdn, Map<String, Object> params) throws VasException {
		initProperties();
		
		String message = vasProperties.getProperty(smsProps.getKey(), smsProps.getDefaultValue(), params);
		
		SmsRequest smsRequest = new SmsRequest();
		
		smsRequest.setMessage(message);
		smsRequest.setMsisdn(msisdn);
		
		log.info("msisdn : "+msisdn+", message : "+message);
		
		sendSms(smsRequest);
	}
	
	public TransactionResponse sendSms (SmsRequest request) throws VasException {
		
		if(request == null 
				|| request.getMessage() == null || request.getMessage().trim().isEmpty()
				|| request.getMsisdn() == null || request.getMsisdn().trim().isEmpty()
				){
			TransactionResponse response = new TransactionResponse();
			
			response.assignResponseCode(SmsResponseCode.INVALID_REQUEST);
			return response;
		}
		
		SendSms sendSms = soapService.getSendSms();
		SendSms_Type param = objectFactory.createSendSms_Type();
		
		String correlator = String.valueOf(System.currentTimeMillis());
		String address = "tel:"+request.getMsisdn();
		String endpoint = queryService.getSettingValue(SmsSetting.SMS_NOTIFY_RECEIPT_URL);
		String senderName = queryService.getSettingValue(SmsSetting.SMS_SENDER_NAME);
		
		SimpleReference sim = new SimpleReference();
		sim.setCorrelator(correlator);
		sim.setEndpoint(endpoint);
		sim.setInterfaceName("SmsNotification");
		
		param.getAddresses().add(address.toString());
		param.setCharging(null);
		param.setDataCoding(0);
		param.setDestinationport(0);
		param.setEsmClass(1);
		param.setMessage(request.getMessage());
		param.setReceiptRequest(sim);
		param.setSenderName(senderName);
		param.setSourceport(123);
		
		SmsLog smsLog = new SmsLog();
		
		smsLog.setCorrelator(correlator);
		smsLog.setMessage(request.getMessage());
		smsLog.setMsisdn(request.getMsisdn());
		smsLog.setStatus(Status.PENDING);

		queryService.createImmediately(smsLog);
		
		SendSmsResponse smsResponse;
		
		try {
			smsResponse = sendSms.sendSms(param);
		} catch (Exception e) {
			smsLog.setStatus(Status.FAILED);
			smsLog.setFailureReason("EXCEPTION : "+e.getMessage());
			smsLog.setDateModified(new Timestamp(System.currentTimeMillis()));
			queryService.update(smsLog);
			throw new VasException("Error sending SMS", e);
		}
		
		smsLog.setDateModified(new Timestamp(System.currentTimeMillis()));
		smsLog.setStatus(Status.UNKNOWN); // till we get the delivery status later on
		smsLog.setRequestId(smsResponse.getResult());
		queryService.update(smsLog);
		
		log.info("response result : "+smsResponse.getResult());
		
		TransactionResponse response = new TransactionResponse();
		
		response.assignResponseCode(SmsResponseCode.SUCCESS);
		
		response.setTransactionId(String.valueOf(smsLog.getPk()));
		
		return response;
	}
	
}
