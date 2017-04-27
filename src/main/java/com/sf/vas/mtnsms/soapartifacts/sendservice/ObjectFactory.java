
package com.sf.vas.mtnsms.soapartifacts.sendservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.sf.vas.mtnsms.soapartifacts.sendservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SendSmsResponse_QNAME = new QName("http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", "sendSmsResponse");
    private final static QName _GetSmsDeliveryStatusResponse_QNAME = new QName("http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", "getSmsDeliveryStatusResponse");
    private final static QName _ServiceException_QNAME = new QName("http://www.csapi.org/schema/parlayx/common/v2_1", "ServiceException");
    private final static QName _PolicyException_QNAME = new QName("http://www.csapi.org/schema/parlayx/common/v2_1", "PolicyException");
    private final static QName _SendSmsLogo_QNAME = new QName("http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", "sendSmsLogo");
    private final static QName _SendSmsRingtoneResponse_QNAME = new QName("http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", "sendSmsRingtoneResponse");
    private final static QName _GetSmsDeliveryStatus_QNAME = new QName("http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", "getSmsDeliveryStatus");
    private final static QName _SendSms_QNAME = new QName("http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", "sendSms");
    private final static QName _SendSmsLogoResponse_QNAME = new QName("http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", "sendSmsLogoResponse");
    private final static QName _SendSmsRingtone_QNAME = new QName("http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", "sendSmsRingtone");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sf.vas.mtnsms.soapartifacts.sendservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SendSmsLogoResponse }
     * 
     */
    public SendSmsLogoResponse createSendSmsLogoResponse() {
        return new SendSmsLogoResponse();
    }

    /**
     * Create an instance of {@link SendSmsRingtone }
     * 
     */
    public SendSmsRingtone createSendSmsRingtone() {
        return new SendSmsRingtone();
    }

    /**
     * Create an instance of {@link SendSms_Type }
     * 
     */
    public SendSms_Type createSendSms_Type() {
        return new SendSms_Type();
    }

    /**
     * Create an instance of {@link GetSmsDeliveryStatus }
     * 
     */
    public GetSmsDeliveryStatus createGetSmsDeliveryStatus() {
        return new GetSmsDeliveryStatus();
    }

    /**
     * Create an instance of {@link SendSmsLogo }
     * 
     */
    public SendSmsLogo createSendSmsLogo() {
        return new SendSmsLogo();
    }

    /**
     * Create an instance of {@link SendSmsRingtoneResponse }
     * 
     */
    public SendSmsRingtoneResponse createSendSmsRingtoneResponse() {
        return new SendSmsRingtoneResponse();
    }

    /**
     * Create an instance of {@link SendSmsResponse }
     * 
     */
    public SendSmsResponse createSendSmsResponse() {
        return new SendSmsResponse();
    }

    /**
     * Create an instance of {@link GetSmsDeliveryStatusResponse }
     * 
     */
    public GetSmsDeliveryStatusResponse createGetSmsDeliveryStatusResponse() {
        return new GetSmsDeliveryStatusResponse();
    }

    /**
     * Create an instance of {@link SmsMessage }
     * 
     */
    public SmsMessage createSmsMessage() {
        return new SmsMessage();
    }

    /**
     * Create an instance of {@link DeliveryInformation }
     * 
     */
    public DeliveryInformation createDeliveryInformation() {
        return new DeliveryInformation();
    }

    /**
     * Create an instance of {@link ServiceException }
     * 
     */
    public ServiceException createServiceException() {
        return new ServiceException();
    }

    /**
     * Create an instance of {@link PolicyException }
     * 
     */
    public PolicyException createPolicyException() {
        return new PolicyException();
    }

    /**
     * Create an instance of {@link ServiceError }
     * 
     */
    public ServiceError createServiceError() {
        return new ServiceError();
    }

    /**
     * Create an instance of {@link TimeMetric }
     * 
     */
    public TimeMetric createTimeMetric() {
        return new TimeMetric();
    }

    /**
     * Create an instance of {@link SimpleReference }
     * 
     */
    public SimpleReference createSimpleReference() {
        return new SimpleReference();
    }

    /**
     * Create an instance of {@link ChargingInformation }
     * 
     */
    public ChargingInformation createChargingInformation() {
        return new ChargingInformation();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendSmsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", name = "sendSmsResponse")
    public JAXBElement<SendSmsResponse> createSendSmsResponse(SendSmsResponse value) {
        return new JAXBElement<SendSmsResponse>(_SendSmsResponse_QNAME, SendSmsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSmsDeliveryStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", name = "getSmsDeliveryStatusResponse")
    public JAXBElement<GetSmsDeliveryStatusResponse> createGetSmsDeliveryStatusResponse(GetSmsDeliveryStatusResponse value) {
        return new JAXBElement<GetSmsDeliveryStatusResponse>(_GetSmsDeliveryStatusResponse_QNAME, GetSmsDeliveryStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/common/v2_1", name = "ServiceException")
    public JAXBElement<ServiceException> createServiceException(ServiceException value) {
        return new JAXBElement<ServiceException>(_ServiceException_QNAME, ServiceException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PolicyException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/common/v2_1", name = "PolicyException")
    public JAXBElement<PolicyException> createPolicyException(PolicyException value) {
        return new JAXBElement<PolicyException>(_PolicyException_QNAME, PolicyException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendSmsLogo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", name = "sendSmsLogo")
    public JAXBElement<SendSmsLogo> createSendSmsLogo(SendSmsLogo value) {
        return new JAXBElement<SendSmsLogo>(_SendSmsLogo_QNAME, SendSmsLogo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendSmsRingtoneResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", name = "sendSmsRingtoneResponse")
    public JAXBElement<SendSmsRingtoneResponse> createSendSmsRingtoneResponse(SendSmsRingtoneResponse value) {
        return new JAXBElement<SendSmsRingtoneResponse>(_SendSmsRingtoneResponse_QNAME, SendSmsRingtoneResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSmsDeliveryStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", name = "getSmsDeliveryStatus")
    public JAXBElement<GetSmsDeliveryStatus> createGetSmsDeliveryStatus(GetSmsDeliveryStatus value) {
        return new JAXBElement<GetSmsDeliveryStatus>(_GetSmsDeliveryStatus_QNAME, GetSmsDeliveryStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendSms_Type }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", name = "sendSms")
    public JAXBElement<SendSms_Type> createSendSms(SendSms_Type value) {
        return new JAXBElement<SendSms_Type>(_SendSms_QNAME, SendSms_Type.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendSmsLogoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", name = "sendSmsLogoResponse")
    public JAXBElement<SendSmsLogoResponse> createSendSmsLogoResponse(SendSmsLogoResponse value) {
        return new JAXBElement<SendSmsLogoResponse>(_SendSmsLogoResponse_QNAME, SendSmsLogoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendSmsRingtone }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/sms/send/v2_2/local", name = "sendSmsRingtone")
    public JAXBElement<SendSmsRingtone> createSendSmsRingtone(SendSmsRingtone value) {
        return new JAXBElement<SendSmsRingtone>(_SendSmsRingtone_QNAME, SendSmsRingtone.class, null, value);
    }

}
