package com.shopme.setting;

import java.util.List;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingBag;

public class EmailSettingBag extends SettingBag {

	public EmailSettingBag(List<Setting> listSettings) {
		super(listSettings);
	}
	
	public String getHost() {
		return super.getValue("MAIL_HOST");
	}
	public int getPort() {
		return Integer.parseInt( super.getValue("MAIL_PORT"));
	}
	public String getUsername() {
		return super.getValue("MAIL_USERNAME");
	}
	public String getPassword() {
		return super.getValue("MAIL_PASSWORD");
	}
	public String getSmptAuth() {
		return super.getValue("SMPT_AUTH");
	}
	public String getSmptSecured() {
		return super.getValue("SMPT_SECURED");
	}
	public String getFormAddress() {
		return super.getValue("MAIL_FROM");
	}
	public String getFormSenderName() {
		return super.getValue("MAIL_SENDER_NAME");
	}
	public String getCustomerVerifySubject() {
		return super.getValue("CUSTOMER_VERIFY_SUBJECT");
	}
	public String getCustomerVerifyContent() {
		return super.getValue("CUSTOMER_VERIFY_CONTENT");
	}

}
