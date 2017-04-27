/**
 * 
 */
package com.sf.vas.mtnsms.soap.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.jboss.logging.Logger;
import org.w3c.dom.Node;

import com.sf.vas.mtnsms.exception.SmsRuntimeException;
import com.sf.vas.mtnsms.jaxb.commonheadtypes.RequestSOAPHeader;
import com.sf.vas.mtnsms.soapartifacts.sendservice.SendSms_Type;

/**
 * @author dawuzi
 *
 */
public class PrintOutboundSoapMessageHandler implements SOAPHandler<SOAPMessageContext> {

	private Logger log = Logger.getLogger(getClass());

	@Override
	public boolean handleMessage(SOAPMessageContext context) {

		log.info("handle message called in "+getClass().getName());
		
		Boolean outbound = (Boolean) context.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if(outbound != null && outbound){
			try {
				
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				
				context.getMessage().writeTo(outputStream);
				
				String string = outputStream.toString(StandardCharsets.UTF_8.name());
				
				log.info("XXXXXXXXXXXXXXXX message XXXXXXXXXXX");
				log.info(string);
				
				SOAPHeader header = context.getMessage().getSOAPPart().getEnvelope().getHeader();
				
				RequestSOAPHeader soapHeader = unmarshal(RequestSOAPHeader.class, header); 
				
				SOAPBody body = context.getMessage().getSOAPPart().getEnvelope().getBody();
				
				SendSms_Type type = unmarshal(SendSms_Type.class, body);
				
				log.info("soapHeader : "+soapHeader.toString());
				log.info("type : "+type.toString());
				
			} catch (SOAPException e) {
				throw new SmsRuntimeException("Error adding a new Header element", e);
//			} catch (JAXBException e) {
//				throw new SmsRuntimeException("Error marshalling the header object", e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
	private <T> T unmarshal(Class<T> clazz, SOAPElement soapBody)
			throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		Node bindElement = (Node) soapBody.getFirstChild();
		while (bindElement.getNodeType() != Node.ELEMENT_NODE) {
			bindElement = (Node) bindElement.getNextSibling();
		}
		return unmarshaller.unmarshal(bindElement, clazz).getValue();
	}

}
