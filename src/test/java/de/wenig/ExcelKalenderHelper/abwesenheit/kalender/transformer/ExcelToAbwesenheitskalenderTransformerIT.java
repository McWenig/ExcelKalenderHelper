package de.wenig.ExcelKalenderHelper.abwesenheit.kalender.transformer;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class} )
public class ExcelToAbwesenheitskalenderTransformerIT {
	
	@Autowired
	ExcelToAbwesenheitskalenderTransformer transformer;
	
	Abwesenheitskalender awk = null;
	
	@Before
	public void setUp() throws Exception {
		String pathToExel = getClass().getClassLoader().getResource("AWK.xlsx").getFile();
		awk = transformer.transformExcel(pathToExel);
	}
	
	@Test
	public void anzahlTeams() {
		assert(awk.getTeams().size() == 8);
	}
	
	@Test
	public void anzahlFirmen() {
		assert(awk.getFirmen().size() == 4);
	}

	@Test
	public void anzahlOrganisationen() {
		assert(awk.getOrganisationen().size() == 3);
	}
	

	@Test
	public void luceneSuche() {		
		List<Person> luceneErgebnis = awk.suchePersonenLucene("Battiste");
		assert(luceneErgebnis.size()==1);
		Person p = luceneErgebnis.get(0);
		assert(p.getName().equalsIgnoreCase("Battiste"));
		assert(p.getVorname().equalsIgnoreCase("Sebrina"));
		assert(p.getFirma().getName().equalsIgnoreCase("CFU"));
		assert(p.getOrganisation().getName().equalsIgnoreCase("B"));
		assert(p.getTeam().getName().equalsIgnoreCase("D"));
	}

	@Test
	public void abwesenheitenZuPerson() {	
		Person p = new Person(17, "Battiste", "Sebrina", new Firma("CFU"), new Organisation("B"), new Team("D"));
		List<Abwesenheit> abw = awk.getAbwesenheiten(p);
		LocalDate ersterJanuar = LocalDate.of(2018, 1, 1);
		LocalDate sechsterJanuar = LocalDate.of(2018, 1, 6);
		assert(abw.size() == 2);
		assert(abw.get(0).getStatus().equalsIgnoreCase("F"));
		assert(abw.get(1).getStatus().equalsIgnoreCase("F"));		
		assert(abw.get(0).getStart().isEqual(ersterJanuar));
		assert(abw.get(1).getStart().isEqual(sechsterJanuar));
	}

//	@Test
//	public void excelFileTransformieren() {
//		
//		Person p = new Person(17, "Battiste", "Sebrina", new Firma("CFU"), new Organisation("B"), new Team("D"));
//		List<Abwesenheit> abw = awk.getAbwesenheiten(p);
//		List<Person> luceneErgebnis = awk.suchePersonenLucene("Battiste");
//		LocalDate ersterJanuar = LocalDate.of(2018, 1, 1);
//		LocalDate sechsterJanuar = LocalDate.of(2018, 1, 6);
//		assert(abw.size() == 2);
//		assert(abw.get(0).getStatus().equalsIgnoreCase("F"));
//		assert(abw.get(1).getStatus().equalsIgnoreCase("F"));		
//		assert(abw.get(0).getStart().isEqual(ersterJanuar));
//		assert(abw.get(1).getStart().isEqual(sechsterJanuar));
//		assert(awk.getTeams().size() == 8);
//		assert(awk.getFirmen().size() == 8);
//		assert(awk.getOrganisationen().size() == 4);
//		assert(luceneErgebnis.size()==1);
//	}

}
