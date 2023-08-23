package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.SettingCategory;
import com.shopme.common.entity.Setting;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTests {
	@Autowired
	SettingRepository repo;

	@Test
	public void testCreateGeneralSetting() {
		//Setting siteName = new Setting("SITE_NAME","iShope", SettingCategory.GENERAL);
		Setting siteName1 = new Setting("SITE_lOGO","iShope.png", SettingCategory.GENERAL);
		Setting siteName2 = new Setting("COPYRIGHT","Copyright (C)  2023 iShope Ltd.", SettingCategory.GENERAL);
		
		//Setting saveSetting = repo.save(siteName);
		
		repo.saveAll(List.of(siteName1,siteName2));
		
		Iterable<Setting> iterabl = repo.findAll();
		
		assertThat(iterabl).size().isGreaterThan(0);
	}
	
	@Test
	public void testCreateCurrencySetting() {
		Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
		Setting symbol = new Setting("CURRENCY_SYMBO","$", SettingCategory.CURRENCY);
		Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION","BEFORE", SettingCategory.CURRENCY);
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE","POINT", SettingCategory.CURRENCY);
		Setting decimalDigits = new Setting("DICIMAL_DIGITS","2", SettingCategory.CURRENCY);
		Setting thousandsPointsType = new Setting("THOUSANDS_POINT_TYPE","COMMA", SettingCategory.CURRENCY);
		
		repo.saveAll(List.of(currencyId, symbol, symbolPosition,decimalPointType,decimalDigits,thousandsPointsType));
	}
	@Test
	public void testListSettingsByCategory() {
		List<Setting> settings = repo.findByCategory(SettingCategory.GENERAL);
		
		settings.forEach(System.out:: println);
	}

}
