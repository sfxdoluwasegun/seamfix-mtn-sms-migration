/**
 * 
 */
package com.sf.vas.mtnsms.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.csapi.wsdl.parlayx.sms.send.v2_2.service.SendSms;
import org.csapi.wsdl.parlayx.sms.send.v2_2.service.SendSmsService;
import org.jboss.logging.Logger;

import com.sf.vas.mtnsms.enums.SmsSetting;
import com.sf.vas.mtnsms.exception.SmsRuntimeException;
import com.sf.vas.mtnsms.jaxb.commonheadtypes.RequestSOAPHeader;
import com.sf.vas.mtnsms.tools.SmsQueryService;
import com.sf.vas.mtnsms.util.SecurityUtil;

/**
 * @author dawuzi
 *
 */
@Stateless
public class SmsSoapService {

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(getClass());
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
	
	@Inject
	SmsQueryService smsQueryService;
	
	private SendSmsService sendSmsService;
	private SendSms sendSms;
	private Marshaller headerMarshaller;
	
	@PostConstruct
	private void init(){
		
		String wsdlUrl = smsQueryService.getSettingValue(SmsSetting.SMS_WSDL_FILE_URL);
		
		try {
			sendSmsService = new SendSmsService(new URL(wsdlUrl));
		} catch (MalformedURLException e) {
			throw new SmsRuntimeException("Invalid setting WSDL URL", e);
		}
		
		try {
			headerMarshaller = JAXBContext.newInstance(RequestSOAPHeader.class).createMarshaller();
		} catch (JAXBException e) {
			throw new SmsRuntimeException("Error creating the Marshaller object", e);
		}
		
		final RequestSOAPHeader header = getRequestSoapHeader();
		
		@SuppressWarnings("rawtypes")
		List<Handler> handlers = new ArrayList<>();
		
		handlers.add(new SOAPHandler<SOAPMessageContext>() {

			@Override
			public boolean handleMessage(SOAPMessageContext context) {
				
				Boolean outbound = (Boolean) context.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);
				
				if(outbound != null && outbound){
					try {
		                SOAPHeader soapHeader = context.getMessage().getSOAPPart().getEnvelope().addHeader();
		                // marshalling instance (appending) to SOAP header's xml node
		                headerMarshaller.marshal(header, soapHeader);
					} catch (SOAPException e) {
						throw new SmsRuntimeException("Error adding a new Header element", e);
					} catch (JAXBException e) {
						throw new SmsRuntimeException("Error marshalling the header object", e);
					}
				}
				return true;
			}

			@Override
			public boolean handleFault(SOAPMessageContext context) {
				return true;
			}

			@Override
			public void close(MessageContext context) {
			}

			@Override
			public Set<QName> getHeaders() {
				return Collections.emptySet();
			}
		});
		
		sendSms = sendSmsService.getSendSms();
		
		Binding binding = ((BindingProvider) sendSms).getBinding();
		
		binding.setHandlerChain(handlers);
		
	}

	/**
	 * @return
	 */
	private RequestSOAPHeader getRequestSoapHeader() {
		
		String spId = smsQueryService.getSettingValue(SmsSetting.SMS_SP_ID);
		String serviceId = smsQueryService.getSettingValue(SmsSetting.SMS_SERVICE_ID);
		String spPassword = smsQueryService.getSettingValue(SmsSetting.SMS_SP_PASSWORD);
		String oafa = smsQueryService.getSettingValue(SmsSetting.SMS_OA_FA);
		
		String created = sdf.format(Calendar.getInstance().getTime());
		
		String password = SecurityUtil.md5(spId + spPassword + created);

		RequestSOAPHeader requestHeader = new RequestSOAPHeader();
		
		requestHeader.setSpId(spId);
		requestHeader.setSpPassword(password);
		requestHeader.setServiceId(serviceId);
		requestHeader.setTimeStamp(created);
		requestHeader.setOA(oafa);
		requestHeader.setFA(oafa);
		
		return requestHeader;
	}
	
	public SendSms getSendSms(){
		return sendSms;
	}
}
