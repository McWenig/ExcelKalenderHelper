package de.wenig.ExcelKalenderHelper.masterplan.eingabe.factories;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import de.wenig.ExcelKalenderHelper.masterplan.eingabe.Masterplan;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanFactory;

@CacheConfig(cacheNames = "masterplan")
@Component("cachingMasterplanFactory")
public class CachingMasterplanFactory implements MasterplanFactory {

	@Autowired
	private MasterplanFactoryImpl factory;

	@Cacheable
	@Override
	public Masterplan produceMasterplanFromExcel(File excelFile) {
		return getCachedMasterplan(excelFile);
	}

	private Masterplan getCachedMasterplan(File excelFile) {
		return factory.produceMasterplanFromExcel(excelFile);
	}

}
