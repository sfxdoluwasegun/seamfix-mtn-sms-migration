/**
 * 
 */
package com.sf.vas.mtnsms.service;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.soap.SOAPHeader;

import com.sf.vas.mtnsms.jaxb.commonheadtypes.RequestSOAPHeader;
import com.sf.vas.mtnsms.soapartifacts.sendservice.ObjectFactory;
import com.sf.vas.mtnsms.soapartifacts.sendservice.SendSms_Type;

/**
 * @author dawuzi
 *
 */
public class SmsTest {

	public static void main(String[] args) throws Exception {
		test();
	}
	
	public static void test() throws JAXBException {
		
		ObjectFactory objectFactory = new ObjectFactory();
		
		SendSms_Type type = objectFactory.createSendSms_Type();
		
		type.setMessage("ho theer");
		type.setDataCoding(10);
		type.setEncode("UTF-8");
		type.setSenderName("dawuz");
		
		StringWriter writer;
		
		writer = new StringWriter();
		
		JAXBElement<SendSms_Type> createSendSms = objectFactory.createSendSms(type);
		
		Marshaller marshaller = JAXBContext.newInstance(SendSms_Type.class, RequestSOAPHeader.class).createMarshaller();
		
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		
//		writer = new StringWriter();
//		marshaller.marshal(type, writer);
//		System.out.println(writer.toString());
		
		
		RequestSOAPHeader header = new RequestSOAPHeader();
		
		header.setAuthId("hii");
		header.setBundleID("fghjk");
		header.setFA("fghj");
		header.setLinkid("cvn");
		header.setOA("ghj");
		header.setOauthToken("hfhkd");
		header.setSpId("ghjk");
		header.setSpPassword("ghjk");
		header.setTimeStamp("sddfsd");
		
		JAXBElement<RequestSOAPHeader> createRequestSOAPHeader = new com.sf.vas.mtnsms.jaxb.commonheadtypes.ObjectFactory().createRequestSOAPHeader(header);
		
		writer = new StringWriter();
		marshaller.marshal(createRequestSOAPHeader, writer);
		System.out.println(writer.toString());
		
		writer = new StringWriter();
		marshaller.marshal(createSendSms, writer);
		System.out.println(writer.toString());
		
		System.out.println();
		
	}

}
