/**
 * 
 */
package com.sf.vas.mtnsms.enums;

/**
 * @author dawuzi
 *
 */
public enum SmsSetting {

    SMS_WSDL_FILE_URL("http://localhost:8088/mockSendSmsBinding?wsdl", "URL to the wsdl file"),
    SMS_SP_ID("35000001", "SpId in the request header"),
    SMS_SERVICE_ID("35000001000001", "serviceId in the request header"),
    SMS_SP_PASSWORD("123456", "spPassword in the request header"),
    SMS_OA_FA("8613300000010", "oa and fa in the request header"),
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
	
}
