/**
 * 
 */
package com.sf.vas.mtnsms.enums;

import java.util.HashSet;
import java.util.Set;

import com.sf.vas.utils.restartifacts.IResponseCode;

/**
 * @author dawuzi
 *
 */
public enum SmsResponseCode implements IResponseCode {

    SUCCESS("00", "Successful"),
    ERROR("01", "Error"), //usually triggered by an exception
    INVALID_REQUEST("03", "Invalid request"), //usually triggered by an exception
    ;
	
	static {
		validateUniqueResponseCodes();
	}

	private static void validateUniqueResponseCodes() {
		Set<String> seenCodes = new HashSet<>();
		for (SmsResponseCode code : SmsResponseCode.values()) {
			if(!seenCodes.add(code.getResponseCode())){
				throw new IllegalStateException("duplicate response codes : "+code.getResponseCode());
			}
		}
	}

    private String responseCode;
    private String responseDescription;

    private SmsResponseCode(String responseCode, String responseDescription) {
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
    }
	
	public String getResponseCode() {
        return responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }
	
}
