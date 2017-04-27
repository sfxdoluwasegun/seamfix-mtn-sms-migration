/**
 * 
 */
package com.sf.vas.mtnsms.soap.impl;

import javax.ejb.Asynchronous;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.jboss.logging.Logger;

import com.sf.vas.mtnsms.jaxb.commonheadtypes.NotifySOAPHeader;
import com.sf.vas.mtnsms.service.SmsMtnService;
import com.sf.vas.mtnsms.soapartifacts.notification.NotifySmsDeliveryReceipt;
import com.sf.vas.mtnsms.soapartifacts.notification.NotifySmsDeliveryReceiptResponse;
import com.sf.vas.mtnsms.soapartifacts.notification.NotifySmsReception;
import com.sf.vas.mtnsms.soapartifacts.notification.NotifySmsReceptionResponse;
import com.sf.vas.mtnsms.soapartifacts.notification.ObjectFactory;
import com.sf.vas.mtnsms.soapartifacts.notification.SmsNotification;

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
	
	@Inject
	private SmsMtnService smsMtnService;
	
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

    	log.info("notifySmsReception : "+parameters);
    	log.info("header : "+header);
    	
    	handleNotifySmsReception(parameters);
    	return factory.createNotifySmsReceptionResponse();
    	
    }
    
    @Asynchronous
    private void handleNotifySmsReception(NotifySmsReception notifySmsReception){
		log.info("smsMtnService " + smsMtnService);
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
			
		log.info("smsMtnService" + smsMtnService);
    	log.info("notifySmsDeliveryReceipt : "+parameters);
    	log.info("header : "+header);
    	
    	return factory.createNotifySmsDeliveryReceiptResponse();
    	
    }
    
}
