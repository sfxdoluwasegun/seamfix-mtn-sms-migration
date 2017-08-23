/**
 * 
 */
package com.sf.vas.mtnsms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		updateSmsPropertiesFile();
		initProperties();
	}

	private void updateSmsPropertiesFile() {
		try {
			if(!file.exists()){
				log.info("sms properties file does not exist. Proceeding to create one"); 
				createDefaultSmsPropertiesFile();
			} else {
				log.info("including properties"); 
				includeNewSmsProperties();
				updateSmsParameter();
			}
		} catch (IOException e) {
			log.error("Error updating sms properties file", e);
		}
	}

	private void updateSmsParameter() throws IOException {
		
		Map<String,Map<String, String>> mapOfMessagePairs = processParameters();
		Map<String, String> parameterMessagePair = mapOfMessagePairs.get("parameterMessagePair");
		Map<String, String> keyMessagePair = mapOfMessagePairs.get("keyMessagePair");
		
		if(!(parameterMessagePair == null && keyMessagePair == null)){
			
			for(SmsProps smsProps : SmsProps.values()){
				
				String fromMap = parameterMessagePair.get(smsProps.getKey());
		 		String fromEnum = "#"+smsProps.getDefaultDescription();
		 		
		 		if(!fromEnum.equalsIgnoreCase(fromMap)){
		 			updateOnlyParameters(keyMessagePair);
		 			return;
				}
			}
		}
		
		return;
	}

	private void updateOnlyParameters(Map<String, String> keyMessagePair) throws IOException {
		
		StringBuilder builder = new StringBuilder();
		String newLine = "\n";
		
		builder.append("#Configuration file for the sms messages sent to users").append(newLine).append(newLine).append(newLine);
		
		for(SmsProps smsProps : SmsProps.values()){
			builder.append("#").append(smsProps.getDefaultDescription()).append(newLine);
			String messageNewLine = keyMessagePair.get(smsProps.getKey()) == null ? smsProps.getKey()+"="+smsProps.getDefaultValue():keyMessagePair.get(smsProps.getKey());
			builder.append(messageNewLine).append(newLine).append(newLine);
		}
		
		if(!file.exists()){
			file.createNewFile();
		}
		
		try(PrintWriter out = new PrintWriter(file) ;){ 
			out.write(builder.toString());
		} 
		
	}

	/**
	 * This simply updates the sms properties file with any SmsProp key/value pair not found in the file
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void includeNewSmsProperties() throws FileNotFoundException, IOException {
		
		Properties properties = new Properties();
		
		try (InputStream inputStream = new FileInputStream(file)){
            properties.load(inputStream);
		}
		
		StringBuilder builder = new StringBuilder();
		String newLine = "\n";
		
		for(SmsProps smsProps : SmsProps.values()){
//			property not found in the file, file needs to be included with it 
			if(properties.getProperty(smsProps.getKey()) == null){
				builder.append("#").append(smsProps.getDefaultDescription()).append(newLine);
				builder.append(smsProps.getKey()).append("=").append(smsProps.getDefaultValue()).append(newLine).append(newLine);
			}
			
		}
		
		String updateContents = builder.toString();
		
		if(!updateContents.trim().isEmpty()){
			try(PrintWriter out = new PrintWriter(new FileOutputStream(file, true));){ 
				out.write(updateContents);
			} 
		}
	}

	/**
	 * 
	 * @return
	 */
	private Map<String, Map<String, String>> processParameters() {
		
		log.info("In processParameters");
		Map<String,Map<String, String>> mapOfMessagePairs = new HashMap<String, Map<String,String>>();
		
		Map<String, String> parameterMessagePair = new HashMap<String, String>();
		Map<String, String> keyMessagePair = new HashMap<String, String>();
		List<String> listOfParameters = new ArrayList<>();
		List<String> listOfMessages = new ArrayList<>();
		
		try(Stream<String> stream = Files.lines(Paths.get(smsPropsFile))){
			 listOfParameters = stream.filter(Line -> validate(Line))
					 						  .collect(Collectors.toList());
		 } catch (IOException e) {
			// return empty map
			log.error("Error in process parameter : get list of parameters",e);
		}
		
		try(Stream<String> stream = Files.lines(Paths.get(smsPropsFile))){
			 listOfMessages = stream.filter(Line -> Line.contains("="))
					  .collect(Collectors.toList());
		 } catch (IOException e) {
			// return empty map
			 log.error("Error in process parameter : get list of messages",e);
		}
		
		for (int i = 0; i < listOfParameters.size(); i++) {
			String[] splitMessage = listOfMessages.get(i).split("=");
			parameterMessagePair.put(splitMessage[0], listOfParameters.get(i));
			keyMessagePair.put(splitMessage[0], listOfMessages.get(i));
		 }
		
		mapOfMessagePairs.put("parameterMessagePair", parameterMessagePair);
		mapOfMessagePairs.put("keyMessagePair", keyMessagePair);
		
		return mapOfMessagePairs;
	}
	
	

	private boolean validate(String value) {
		log.info("inside here");
		if(value.equalsIgnoreCase("#CONFIGURATION FILE FOR THE SMS MESSAGES SENT TO USERS")){
			return false;
		}
		
		if(value.startsWith("#")){
			return true;
		}
		
		return false;
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
	
	private void createDefaultSmsPropertiesFile() throws IOException {
		String newLine = "\n";
		StringBuilder builder = new StringBuilder();
		
		builder.append("#Configuration file for the sms messages sent to users").append(newLine).append(newLine).append(newLine);
		
		for(SmsProps smsProps : SmsProps.values()){
			builder.append("#").append(smsProps.getDefaultDescription()).append(newLine);
			builder.append(smsProps.getKey()).append("=").append(smsProps.getDefaultValue()).append(newLine).append(newLine);
		}
		
		if(!file.exists()){
			file.createNewFile();
		}
		
		try(PrintWriter out = new PrintWriter(file) ;){ 
			out.write(builder.toString());
		} 
	}

	public void sendSms(SmsProps smsProps, String msisdn, String param, String value) throws VasException {
		initProperties();
		
		String message = vasProperties.getProperty(smsProps.getKey(), smsProps.getDefaultValue(), param, value);
		
		SmsRequest smsRequest = new SmsRequest();
		
		smsRequest.setMessage(message);
		smsRequest.setMsisdn(msisdn);
		
		log.info("msisdn : "+msisdn+", message : "+message);
		
		sendSms(smsProps, smsRequest);
	}
	
	public void sendSms(SmsProps smsProps, String msisdn, Map<String, Object> params) throws VasException {
		initProperties();
		
		String message = vasProperties.getProperty(smsProps.getKey(), smsProps.getDefaultValue(), params);
		
		SmsRequest smsRequest = new SmsRequest();
		
		smsRequest.setMessage(message);
		smsRequest.setMsisdn(msisdn);
		
		log.info("msisdn : "+msisdn+", message : "+message);
		
		sendSms(smsProps, smsRequest);
	}
	
	public TransactionResponse sendSms (SmsProps smsProps, SmsRequest request) throws VasException {
		
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
		smsLog.setType(smsProps.name());

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
