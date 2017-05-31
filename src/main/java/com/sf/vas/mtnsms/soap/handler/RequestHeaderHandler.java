/**
 * 
 */
package com.sf.vas.mtnsms.soap.handler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.jboss.logging.Logger;
import org.w3c.dom.Node;

import com.sf.vas.mtnsms.constants.SmsConstants;
import com.sf.vas.mtnsms.enums.SmsSetting;
import com.sf.vas.mtnsms.exception.SmsRuntimeException;
import com.sf.vas.mtnsms.jaxb.commonheadtypes.ObjectFactory;
import com.sf.vas.mtnsms.jaxb.commonheadtypes.RequestSOAPHeader;
import com.sf.vas.mtnsms.soapartifacts.sendservice.SendSms_Type;
import com.sf.vas.mtnsms.tools.SmsMtnQueryService;
import com.sf.vas.mtnsms.util.SecurityUtil;

/**
 * @author dawuzi
 *
 */
public class RequestHeaderHandler implements SOAPHandler<SOAPMessageContext> {

	private SimpleDateFormat sdf = new SimpleDateFormat(SmsConstants.SMS_HEADER_TIMESTAMP_PATTERN);
	private Logger log = Logger.getLogger(getClass());

	private Marshaller headerMarshaller; 
	private Unmarshaller bodyUnmarshaller;
	
	private final Pattern COLON_PATTERN = Pattern.compile(":");

	private SmsMtnQueryService queryService;
	private ObjectFactory factory = new ObjectFactory();
	
	public RequestHeaderHandler(SmsMtnQueryService queryService) {
		this.queryService = queryService;
		try {
			headerMarshaller = JAXBContext.newInstance(RequestSOAPHeader.class).createMarshaller();
			bodyUnmarshaller = JAXBContext.newInstance(SendSms_Type.class).createUnmarshaller();
		} catch (JAXBException e) {
			throw new SmsRuntimeException("Error creating the Marshaller object", e);
		}
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		
		log.info("handle message called in "+getClass().getName());

		Boolean outbound = (Boolean) context.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		if(outbound != null && outbound){
			JAXBElement<RequestSOAPHeader> header;

			try {
				header = getRequestSoapHeader(context);
			} catch (SOAPException | JAXBException e1) {
				log.error("Error getting Soap Header", e1);
				return false;
			}

			try {
				
				SOAPEnvelope envelope = context.getMessage().getSOAPPart().getEnvelope();
				
//				this namespace comes from the request header and needs to be added to the envelope as well for the request to go through
				envelope.addNamespaceDeclaration("v2", "http://www.huawei.com.cn/schema/common/v2_1");
				
				SOAPHeader soapHeader = context.getMessage().getSOAPPart().getEnvelope().getHeader();

				if(soapHeader == null){
					soapHeader = context.getMessage().getSOAPPart().getEnvelope().addHeader();
				}

				// marshalling instance (appending) to SOAP header's xml node
				headerMarshaller.marshal(header, soapHeader);
				
				context.getMessage().saveChanges();
				
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

	/**
	 * @param context 
	 * @return
	 * @throws JAXBException 
	 * @throws SOAPException 
	 */
	private JAXBElement<RequestSOAPHeader> getRequestSoapHeader(SOAPMessageContext context) throws SOAPException, JAXBException {

		String spId = queryService.getSettingValue(SmsSetting.SMS_SP_ID);
		String serviceId = queryService.getSettingValue(SmsSetting.SMS_SERVICE_ID);
		String spPassword = queryService.getSettingValue(SmsSetting.SMS_SP_PASSWORD);
		String oafa = getOaFa(context);

		String created = sdf.format(Calendar.getInstance().getTime());

		String password = SecurityUtil.md5(spId + spPassword + created);

		RequestSOAPHeader requestHeader = new RequestSOAPHeader();

		requestHeader.setSpId(spId);
		requestHeader.setSpPassword(password);
		requestHeader.setServiceId(serviceId);
		requestHeader.setTimeStamp(created);
		requestHeader.setOA(oafa);
		requestHeader.setFA(oafa);

		return factory.createRequestSOAPHeader(requestHeader);
	}

	/**
	 * @param context
	 * @return
	 * @throws SOAPException 
	 * @throws JAXBException 
	 */
	private String getOaFa(SOAPMessageContext context) throws SOAPException, JAXBException {

		SOAPBody body = context.getMessage().getSOAPPart().getEnvelope().getBody();

		SendSms_Type type = unmarshalBody(body);

		List<String> addresses = type.getAddresses();

		if(addresses.isEmpty()){
			log.warn("No phone number specified");
			return null;
		}

		String tel = COLON_PATTERN.split(addresses.get(0))[1];

		return tel;
	}

	private SendSms_Type unmarshalBody(SOAPBody soapBody) throws JAXBException {
		Node bindElement = (Node) soapBody.getFirstChild();
		while (bindElement.getNodeType() != Node.ELEMENT_NODE) {
			bindElement = (Node) bindElement.getNextSibling();
		}
		return bodyUnmarshaller.unmarshal(bindElement, SendSms_Type.class).getValue();
	}

}
