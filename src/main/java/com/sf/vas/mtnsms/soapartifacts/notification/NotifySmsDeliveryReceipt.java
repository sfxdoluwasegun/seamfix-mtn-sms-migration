
package com.sf.vas.mtnsms.soapartifacts.notification;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import lombok.ToString;


/**
 * <p>Java class for notifySmsDeliveryReceipt complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="notifySmsDeliveryReceipt">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="correlator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="deliveryStatus" type="{http://www.csapi.org/schema/parlayx/sms/v2_2}DeliveryInformation"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "notifySmsDeliveryReceipt", namespace = "http://www.csapi.org/schema/parlayx/sms/notification/v2_2/local", propOrder = {
    "correlator",
    "deliveryStatus"
})
@ToString
public class NotifySmsDeliveryReceipt {

    @XmlElement(namespace = "http://www.csapi.org/schema/parlayx/sms/notification/v2_2/local", required = true)
    protected String correlator;
    @XmlElement(namespace = "http://www.csapi.org/schema/parlayx/sms/notification/v2_2/local", required = true)
    protected DeliveryInformation deliveryStatus;

    /**
     * Gets the value of the correlator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorrelator() {
        return correlator;
    }

    /**
     * Sets the value of the correlator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorrelator(String value) {
        this.correlator = value;
    }

    /**
     * Gets the value of the deliveryStatus property.
     * 
     * @return
     *     possible object is
     *     {@link DeliveryInformation }
     *     
     */
    public DeliveryInformation getDeliveryStatus() {
        return deliveryStatus;
    }

    /**
     * Sets the value of the deliveryStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeliveryInformation }
     *     
     */
    public void setDeliveryStatus(DeliveryInformation value) {
        this.deliveryStatus = value;
    }

}
