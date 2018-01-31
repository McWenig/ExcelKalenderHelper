package de.wenig.ExcelKalenderHelper.masterplan.controller;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.wenig.ExcelKalenderHelper.masterplan.ausgabe.rest.MasterplanReleaseExtractor;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.Masterplan;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanFactory;

@RestController
public class ReleaselistController {

	@Value("${excelFile}")
	private String path;

	@Autowired
	@Qualifier("cachingMasterplanFactory")
	private MasterplanFactory factory;

	@RequestMapping("/releases")
	public List<String> releases(HttpServletResponse response) {
		List<String> result = new LinkedList<String>();
		
		CacheControl cc = CacheControl.maxAge(1, TimeUnit.HOURS);
		response.addHeader("CacheControl", cc.getHeaderValue());
		
		File excelFile = new File(path);
		Masterplan mp = factory.produceMasterplanFromExcel(excelFile);
		result = MasterplanReleaseExtractor.getReleaseList(mp);

		return result;
	}

}
