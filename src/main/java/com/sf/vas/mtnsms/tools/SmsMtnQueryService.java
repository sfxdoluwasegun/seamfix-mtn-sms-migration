/**
 * 
 */
package com.sf.vas.mtnsms.tools;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.sf.vas.atjpa.entities.Settings;
import com.sf.vas.atjpa.enums.SettingsType;
import com.sf.vas.atjpa.parent.JEntity;
import com.sf.vas.atjpa.tools.QueryService;
import com.sf.vas.mtnsms.enums.SmsSetting;

/**
 * @author dawuzi
 *
 */
@Stateless
public class SmsMtnQueryService extends QueryService {
	
	@PostConstruct
	private void initialize(){
//		create all the required settings on init
		for(SmsSetting setting : SmsSetting.values()){
			getSettingValue(setting);
		}
	}

	public String getSettingValue(String name){
		Settings settings = getSettingsByName(name);
		if(settings == null){
			return null;
		} else {
			return settings.getValue();
		}
	}
	
	public String getSettingValue(String name, String value, String description){
		
		Settings settings = getSettingsByName(name); 
		
		if(settings != null){
			return settings.getValue();
		}
		settings = createSetting(name, value, description);
		
		if(settings == null){
			return null;
		} else {
			return settings.getValue();
		}
	}
	
	public String getSettingValue(SmsSetting smsSetting){
		
		Settings settings = getSettingsByName(smsSetting.name()); 
		
		if(settings != null){
			return settings.getValue();
		}
		
		settings = createSetting(smsSetting.name(), smsSetting.getDefaultValue(), smsSetting.getDefaultDescription());
		
		if(settings == null){
			return null;
		} else {
			return settings.getValue();
		}
	}

	/**
	 * @param vtuSetting
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Settings createSetting(String name, String value, String description) {
		return createSettings(name, value, description, SettingsType.GENERAL);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public <T extends JEntity> T createImmediately(T entity){
		create(entity);
		return entity;
	}
}
