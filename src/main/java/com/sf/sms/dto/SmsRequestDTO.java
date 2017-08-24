package com.sf.sms.dto;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.sf.vas.atjpa.enums.NetworkCarrierType;
import com.sf.vas.utils.enums.SmsProps;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author DAWUZI
 *
 */

@Getter
@Setter
@ToString
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SmsRequestDTO implements Serializable {

	private static final long serialVersionUID = 3940016486528692188L;
	
	private SmsProps smsProps; 
	private String msisdn; 
	private Map<String, Object> params;
	private NetworkCarrierType networkCarrierType;

}
