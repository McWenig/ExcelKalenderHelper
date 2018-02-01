package de.wenig.ExcelKalenderHelper.masterplan.controller;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.CacheControl;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.wenig.ExcelKalenderHelper.common.Event;
import de.wenig.ExcelKalenderHelper.masterplan.ausgabe.rest.MasterplanToEventTransformer;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.Masterplan;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanFactory;

@RestController
public class EventsController {

	@Value("${excelFile}")
	private String path;

	@Autowired
	@Qualifier("cachingMasterplanFactory")
	private MasterplanFactory factory;

	@RequestMapping("/events")
	public List<Event> index(@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate start,
			@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate end) {

		File excelFile = new File(path);
		Masterplan mp = factory.produceMasterplanFromExcel(excelFile);
		return MasterplanToEventTransformer.transform(mp, start, end, null);
	}

	@RequestMapping("/events/{release:\\w+\\.\\w+\\.?}")
	public List<Event> evntsPerRelease(@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate start,
			@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate end, @PathVariable("release") String release, HttpServletResponse response) {

		CacheControl cc = CacheControl.maxAge(1, TimeUnit.HOURS);
		response.addHeader("CacheControl", cc.getHeaderValue());
		File excelFile = new File(path);
		Masterplan mp = factory.produceMasterplanFromExcel(excelFile);
		return MasterplanToEventTransformer.transform(mp, start, end, release);
	}

}
