/**
 * 
 */
package com.sf.vas.mtnsms.soap.impl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.jboss.logging.Logger;

import com.sf.vas.atjpa.entities.SmsLog;
import com.sf.vas.atjpa.enums.Status;
import com.sf.vas.mtnsms.enums.SmsSetting;
import com.sf.vas.mtnsms.exception.SmsRuntimeException;
import com.sf.vas.mtnsms.jaxb.commonheadtypes.NotifySOAPHeader;
import com.sf.vas.mtnsms.soapartifacts.notification.DeliveryInformation;
import com.sf.vas.mtnsms.soapartifacts.notification.DeliveryStatus;
import com.sf.vas.mtnsms.soapartifacts.notification.NotifySmsDeliveryReceipt;
import com.sf.vas.mtnsms.soapartifacts.notification.NotifySmsDeliveryReceiptResponse;
import com.sf.vas.mtnsms.soapartifacts.notification.NotifySmsReception;
import com.sf.vas.mtnsms.soapartifacts.notification.NotifySmsReceptionResponse;
import com.sf.vas.mtnsms.soapartifacts.notification.ObjectFactory;
import com.sf.vas.mtnsms.soapartifacts.notification.SmsNotification;
import com.sf.vas.mtnsms.tools.SmsMtnQueryService;
import com.sf.vas.mtnsms.util.SecurityUtil;

/**
 * @author dawuzi
 * 
 * This class is a concrete implementation of the SmsNotification service class generated from the parlayx_sms_notification_service_2_2.wsdl 
 * file. This class should be used by the depending module as the concrete class when exposing our notification SOAP service for receiving 
 * status callback for our previously sent sms requests
 *
 */
@WebService(name = "SmsNotification", targetNamespace = "http://www.csapi.org/wsdl/parlayx/sms/notification/v2_2/interface")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public class SmsNotificationImpl implements SmsNotification {

	private Logger log = Logger.getLogger(getClass());
	private ObjectFactory factory = new ObjectFactory();
    private Context ctx;
    private SmsRuntimeException serverException = new SmsRuntimeException("Server error");
	
	{
        try {
			ctx = new InitialContext();
		} catch (NamingException e) {
			log.error("error getting initial context", e);
		} 
	}
	
    /**
     * 
     * @param parameters
     * @return
     *     returns com.sf.vas.sms.notifyartifacts.NotifySmsReceptionResponse
     */
    @WebMethod
    @WebResult(name = "notifySmsReceptionResponse", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/notification/v2_2/local", partName = "result")
    public NotifySmsReceptionResponse notifySmsReception(
        @WebParam(name = "notifySmsReception", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/notification/v2_2/local", partName = "parameters")
        NotifySmsReception parameters,
        @WebParam(name = "NotifySOAPHeader", targetNamespace = "http://www.huawei.com.cn/schema/common/v2_1", header = true)
        NotifySOAPHeader header){

    	try {
    		
        	log.info("notifySmsReception : "+parameters);
        	log.info("header : "+header);
        	
        	if(!isValidHeader(header)){
        		throw new SmsRuntimeException("Error in header");
        	}
        	
        	handleNotifySmsReception(parameters);
        	return factory.createNotifySmsReceptionResponse();
    		
		} catch (SmsRuntimeException e) {
    		log.error("Error in notifySmsReception", e);
			throw e;
		} catch (Exception e) {
    		log.error("Error in notifySmsReception", e);
    		throw serverException;
		}
    }

    private void handleNotifySmsReception(NotifySmsReception notifySmsReception){
    }

    /**
     * 
     * @param parameters
     * @return
     *     returns com.sf.vas.sms.notifyartifacts.NotifySmsDeliveryReceiptResponse
     */
    @WebMethod
    @WebResult(name = "notifySmsDeliveryReceiptResponse", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/notification/v2_2/local", partName = "result")
    public NotifySmsDeliveryReceiptResponse notifySmsDeliveryReceipt(
        @WebParam(name = "notifySmsDeliveryReceipt", targetNamespace = "http://www.csapi.org/schema/parlayx/sms/notification/v2_2/local", partName = "parameters")
        NotifySmsDeliveryReceipt parameters,
        @WebParam(name = "NotifySOAPHeader", targetNamespace = "http://www.huawei.com.cn/schema/common/v2_1", header = true)
        NotifySOAPHeader header){
			
    	try {
    		
        	log.info("notifySmsDeliveryReceipt : "+parameters);
        	log.info("header : "+header);
			
        	if(!isValidHeader(header)){
        		throw new SmsRuntimeException("Error in header");
        	}
        	
        	SmsMtnQueryService queryService;
        	
            try {
            	
            	queryService = getQueryService();
            	
            	if(queryService == null){
            		log.error("Error getting looking up the query service");
            		throw new SmsRuntimeException("Server error");
            	}
            	
            	SmsLog smsLog = queryService.getSmsLogByCorrelator(parameters.getCorrelator());
            	
            	if(smsLog == null){
            		throw new SmsRuntimeException("unknown correlator");
            	}
            	
            	smsLog.setTraceUniqueId(header.getTraceUniqueID());
            	
            	DeliveryInformation deliveryInformation = parameters.getDeliveryStatus();
            	
            	if(deliveryInformation == null){
            		throw new SmsRuntimeException("empty delivery information");
            	}
            	
            	if(DeliveryStatus.DELIVERED_TO_TERMINAL.equals(deliveryInformation.getDeliveryStatus())){
            		smsLog.setStatus(Status.SUCCESSFUL); 
            	} else {
            		smsLog.setStatus(Status.FAILED);
            		if(deliveryInformation.getDeliveryStatus() != null){
                		smsLog.setFailureReason(deliveryInformation.getDeliveryStatus().name());
            		} else {
                		smsLog.setFailureReason("Unknown Status recieved");
            		}
            	}
            	
            	queryService.update(smsLog);
    			
    		} catch (NamingException e) {
    			throw new SmsRuntimeException("Server error", e);
    		}

        	return factory.createNotifySmsDeliveryReceiptResponse();
    		
		} catch (SmsRuntimeException e) {
    		log.error("Error in notifySmsDeliveryReceipt", e);
			throw e;
		} catch (Exception e) {
    		log.error("Error in notifySmsDeliveryReceipt", e);
    		throw serverException;
		}
    }

    /**
	 * @param header
	 * @return
     * @throws NamingException 
	 */
	private boolean isValidHeader(NotifySOAPHeader header) throws NamingException {
		
		if(header == null){
			return false;
		}
		
		SmsMtnQueryService queryService = getQueryService();
		
    	if(queryService == null){
    		log.error("Error getting looking up the query service");
    		throw serverException;
    	}
    	
    	if(!queryService.getSettingValue(SmsSetting.SMS_AUTHENTICATE_NOTIFICATION).equalsIgnoreCase("TRUE")){
    		return true;
    	}
    	
    	String spRevpassword = header.getSpRevpassword();
    	
    	if(spRevpassword == null || spRevpassword.trim().isEmpty()){
    		log.info("spRevpassword is null or empty : "+spRevpassword);
    		return false;
    	}
    	
    	spRevpassword = spRevpassword.trim();
    	
    	String spRevId = queryService.getSettingValue(SmsSetting.SMS_SP_REV_ID);
    	String spPassword = queryService.getSettingValue(SmsSetting.SMS_SP_PASSWORD);
    	String expectedServiceId = queryService.getSettingValue(SmsSetting.SMS_SERVICE_ID);
    	
    	if(header.getServiceId() == null || !expectedServiceId.equals(header.getServiceId().trim())){
    		log.info("wrong service id : "+expectedServiceId);
    		return false;
    	}
    	
    	String timeStamp = header.getTimeStamp();
    	
    	if(timeStamp == null || timeStamp.trim().isEmpty()){
    		log.info("timeStamp is null or empty : "+timeStamp);
    		return false;
    	}
    	
    	timeStamp = timeStamp.trim();
    	
//    	might add an extra check of valid timestamp of being in a specific time range when we are sure of the invoking SDP's service timezone
    	
    	String expectedPassword = SecurityUtil.md5(spRevId+spPassword+timeStamp);
		
		return expectedPassword.equals(spRevpassword);
	}
    
	private SmsMtnQueryService getQueryService() throws NamingException {
		
    	if(ctx == null){
    		log.error("naming context not initialized !!!");
    		return null;
    	}
    	
		Object service = ctx.lookup("java:comp/env/SmsMtnQueryService");
		
		log.info("service : "+service);
		
		if(service == null){
			return null;
		}
		
		boolean isService = SmsMtnQueryService.class.isAssignableFrom(service.getClass());
		
		if(!isService){
			return null;
		}
		
		return (SmsMtnQueryService) service; 
	}
	
}
