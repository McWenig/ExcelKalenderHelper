package de.arbeitsagentur.kalender.abwesenheit.kalender.transformer;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Abwesenheit;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.transformer.AbwesenheitenZusammenfasser;

public class AbwesenheitenZusammenfasserTest {

	
	private AbwesenheitenZusammenfasser zusammenfasser = new AbwesenheitenZusammenfasser();
	
	@Test
	public void testFasseAbwesenheitenZusammen_zweiZusammenhaengende() {
		List<Abwesenheit> alle = new LinkedList<Abwesenheit>();
		Date heute = entferneZeitanteil(new Date());
		Date morgen = new Date(heute.getTime()+24*60*60*1000);
		alle.add(new Abwesenheit(heute, heute, "A"));
		alle.add(new Abwesenheit(morgen, morgen, "A"));
		List<Abwesenheit> ergebnis = zusammenfasser.fasseAbwesenheitenZusammen(alle);
		assert(ergebnis.size() == 1);
		assert(ergebnis.get(0).getStart().compareTo(heute) == 0);
		assert(ergebnis.get(0).getEnd().compareTo(morgen) == 0);
	}
	
	private Date entferneZeitanteil(Date d){
		LocalDate date = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return new Date(date.toEpochDay());
	}

}
