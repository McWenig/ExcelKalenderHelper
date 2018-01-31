package de.wenig.ExcelKalenderHelper.abwesenheit.kalender.transformer;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Abwesenheit;

public class AbwesenheitenZusammenfasserTest {

	
	private AbwesenheitenZusammenfasser zusammenfasser = new AbwesenheitenZusammenfasser();
	
	@Test
	public void testFasseAbwesenheitenZusammen_zweiZusammenhaengende() {
		List<Abwesenheit> alle = new LinkedList<Abwesenheit>();
		LocalDate heute = LocalDate.now();
		LocalDate morgen = heute.plusDays(1);
		alle.add(new Abwesenheit(heute, heute, "A"));
		alle.add(new Abwesenheit(morgen, morgen, "A"));
		List<Abwesenheit> ergebnis = zusammenfasser.fasseAbwesenheitenZusammen(alle);
		assert(ergebnis.size() == 1);
		assert(ergebnis.get(0).getStart().compareTo(heute) == 0);
		assert(ergebnis.get(0).getEnd().compareTo(morgen) == 0);
	}
	

}
