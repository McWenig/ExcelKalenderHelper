package de.wenig.ExcelKalenderHelper.abwesenheit.kalender;

import java.util.List;

import org.junit.Test;

import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.Abwesenheitskalender;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Abwesenheit;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Firma;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Organisation;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Person;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Team;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.transformer.ExcelToAbwesenheitskalenderTransformer;

public class ExcelToAbwesenheitskalenderTransformerIT {

	private static final String pathToExel = "P:/P_Produktbüro/Verwaltungsabläufe_alle_Mitarbeiter/Abwesenheitskalender_2018.xlsx";
	
	@Test
	public void excelFileTransformieren() {
		ExcelToAbwesenheitskalenderTransformer transformer = new ExcelToAbwesenheitskalenderTransformer();
		Abwesenheitskalender awk = transformer.transformExcel(pathToExel);
		Person p = new Person(17, "Wenig", "Martin", new Firma("ACN"), new Organisation("VAM-PO"), new Team("VAM-PO"));
		List<Abwesenheit> abw = awk.getAbwesenheiten(p);
		List<Person> luceneErgebnis = awk.suchePersonenLucene("Weni");
		awk.getClass();
		assert(true);
	}

}
