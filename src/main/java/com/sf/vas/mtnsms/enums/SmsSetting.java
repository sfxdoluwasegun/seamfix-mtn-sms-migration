/**
 * 
 */
package com.sf.vas.mtnsms.enums;

import com.sf.vas.atjpa.enums.SettingsType;
import com.sf.vas.dsl.contracts.ISettingInfo;

/**
 * @author dawuzi
 *
 */
public enum SmsSetting implements ISettingInfo {

    SMS_SP_ID("2340110003533", "SpId in the request header"),
    SMS_SERVICE_ID("234012000008996", "serviceId in the request header"),
    SMS_SP_PASSWORD("qav6yMJeIEo+hGVzeXRg2g==", "spPassword in the request header"), // Abc123 after decryption with test key
    SMS_NOTIFY_RECEIPT_URL("http://10.137.213.69:8080/smsrecieptnotification", "This the SOAP url for waiting for recieving sms delivery status"),
    SMS_SENDER_NAME("seamfix", "Sms sender name"),
    SMS_SEND_SMS_SERVICE_URL("http://10.199.198.45:8310/SendSmsService/services/SendSms", "This is the URL to the send sms service"),
    SMS_SP_REV_ID("sdp", "SpRevId in the request header when recieving notification"),
    SMS_AUTHENTICATE_NOTIFICATION("TRUE", "A boolean setting that determines if the app should authenticate the header for sms notification"),
	;
	
	SmsSetting(String defaultValue, String defaultDescription) {
        this.defaultValue = defaultValue;
        this.defaultDescription = defaultDescription;
    }

    private String defaultValue;

    private String defaultDescription;

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getDefaultDescription() {
        return defaultDescription;
    }
	
	public String getName() {
		return name();
	}
	
	public SettingsType getSettingsType() {
		return SettingsType.GENERAL;
	}
}
