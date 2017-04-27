/**
 * 
 */
package com.sf.vas.mtnsms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.jboss.logging.Logger;

import com.sf.vas.mtnsms.enums.SmsSetting;
import com.sf.vas.mtnsms.soap.handler.PrintOutboundSoapMessageHandler;
import com.sf.vas.mtnsms.soap.handler.RequestHeaderHandler;
import com.sf.vas.mtnsms.soapartifacts.sendservice.SendSms;
import com.sf.vas.mtnsms.soapartifacts.sendservice.SendSmsService;
import com.sf.vas.mtnsms.tools.SmsMtnQueryService;

/**
 * @author dawuzi
 *
 */
@Stateless
public class SmsMtnSoapService {

	private Logger log = Logger.getLogger(getClass());
	
	@Inject
	SmsMtnQueryService smsQueryService;
	
	private SendSmsService sendSmsService;
	private SendSms sendSms;
	
	@PostConstruct
	private void init(){
		
		sendSmsService = new SendSmsService(getClass().getClassLoader().getResource("soapresources/parlayx_sms_send_service_2_2.wsdl"));
		
		@SuppressWarnings("rawtypes")
		List<Handler> handlers = new ArrayList<>();
		
		handlers.add(new RequestHeaderHandler(smsQueryService));
		handlers.add(new PrintOutboundSoapMessageHandler());
		
		sendSms = sendSmsService.getSendSms();
		
		BindingProvider bindingProvider = (BindingProvider) sendSms;
		
		Binding binding = bindingProvider.getBinding();
		
		binding.setHandlerChain(handlers);
		
		String sendSmsEndpointUrl = smsQueryService.getSettingValue(SmsSetting.SMS_SEND_SMS_SERVICE_URL);
		
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		
		requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, sendSmsEndpointUrl);
		
		log.info("requestContext : "+requestContext);
		
	}

	public SendSms getSendSms(){
		return sendSms;
	}
}
