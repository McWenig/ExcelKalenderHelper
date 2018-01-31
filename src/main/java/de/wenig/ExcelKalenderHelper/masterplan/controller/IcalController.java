package de.wenig.ExcelKalenderHelper.masterplan.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.EncoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import de.wenig.ExcelKalenderHelper.masterplan.ausgabe.ical.MasterplanToIcalTransormer;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.Masterplan;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanFactory;

@Controller
public class IcalController {

	@Value("${excelFile}")
	private String path;

	@Autowired
	@Qualifier("cachingMasterplanFactory")
	private MasterplanFactory factory;

	@RequestMapping("/ical/masterplan.ics")
	public void index(HttpServletResponse response) throws IOException, EncoderException {
		File excelFile = new File(path);
		Masterplan mp = factory.produceMasterplanFromExcel(excelFile);

		String ical = MasterplanToIcalTransormer.transform(mp, null).toString();

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/octet-stream");
		response.getWriter().print(ical);
		response.getWriter().flush();
	}


	@RequestMapping("/ical/{release:\\w+\\.\\w+}.ics")
	public void icalMitRelease(@PathVariable("release") String release, HttpServletResponse response)
			throws EncoderException, IOException {
		File excelFile = new File(path);
		Masterplan mp = factory.produceMasterplanFromExcel(excelFile);

		String ical = MasterplanToIcalTransormer.transform(mp, release).toString();

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/octet-stream");
		response.getWriter().print(ical);
		response.getWriter().flush();

	}

}
