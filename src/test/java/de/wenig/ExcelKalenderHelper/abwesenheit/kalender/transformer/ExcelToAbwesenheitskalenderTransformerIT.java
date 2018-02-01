package de.wenig.ExcelKalenderHelper.abwesenheit.kalender.transformer;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.Abwesenheitskalender;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Abwesenheit;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Firma;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Organisation;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Person;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Team;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.transformer.ExcelToAbwesenheitskalenderTransformer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class} )
public class ExcelToAbwesenheitskalenderTransformerIT {
	
	@Autowired
	ExcelToAbwesenheitskalenderTransformer transformer;
	
	@Test
	public void excelFileTransformieren() {
		String pathToExel = getClass().getClassLoader().getResource("AWK.xlsx").getFile();
		Abwesenheitskalender awk = transformer.transformExcel(pathToExel);
		Person p = new Person(17, "Battiste", "Sebrina", new Firma("CFU"), new Organisation("B"), new Team("D"));
		List<Abwesenheit> abw = awk.getAbwesenheiten(p);
		List<Person> luceneErgebnis = awk.suchePersonenLucene("Battiste");
		LocalDate ersterJanuar = LocalDate.of(2018, 1, 1);
		LocalDate sechsterJanuar = LocalDate.of(2018, 1, 6);
		assert(abw.size() == 2);
		assert(abw.get(0).getStatus().equalsIgnoreCase("F"));
		assert(abw.get(1).getStatus().equalsIgnoreCase("F"));		
		assert(abw.get(0).getStart().isEqual(ersterJanuar));
		assert(abw.get(1).getStart().isEqual(sechsterJanuar));
		assert(luceneErgebnis.size()==1);
	}

}
