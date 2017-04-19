/**
 * 
 */
package com.sf.vas.mtnsms.tools;

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
public class SmsQueryService extends QueryService {

	public String getSettingValue(SmsSetting smsSetting){
		
		Settings settings = getSettingsByName(smsSetting.name()); 
		
		if(settings != null){
			return settings.getValue();
		}
		
		settings = new Settings();
		
		settings.setName(smsSetting.name());
		settings.setValue(smsSetting.getDefaultValue());
		settings.setDescription(smsSetting.getDefaultDescription());
		settings.setSettingType(SettingsType.GENERAL);
		
		createImmediately(settings);
		
		return settings.getValue();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public <T extends JEntity> T createImmediately(T entity){
		create(entity);
		return entity;
	}
}
