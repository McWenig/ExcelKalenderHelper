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
	
	@Test
	public void testFasseAbwesenheitenZusammen_zweiZusammenhaengendeUnterschiedlicherStatus() {
		List<Abwesenheit> alle = new LinkedList<Abwesenheit>();
		LocalDate heute = LocalDate.now();
		LocalDate morgen = heute.plusDays(1);
		alle.add(new Abwesenheit(heute, heute, "A"));
		alle.add(new Abwesenheit(morgen, morgen, "F"));
		List<Abwesenheit> ergebnis = zusammenfasser.fasseAbwesenheitenZusammen(alle);
		assert(ergebnis.size() == 1);
		assert(ergebnis.get(0).getStart().compareTo(heute) == 0);
		assert(ergebnis.get(0).getEnd().compareTo(morgen) == 0);
	}

	@Test
	public void testFasseAbwesenheitenZusammen_zweiGetrennte() {
		List<Abwesenheit> alle = new LinkedList<Abwesenheit>();
		LocalDate heute = LocalDate.now();
		LocalDate uebermorgen = heute.plusDays(2);
		alle.add(new Abwesenheit(heute, heute, "A"));
		alle.add(new Abwesenheit(uebermorgen, uebermorgen, "A"));
		List<Abwesenheit> ergebnis = zusammenfasser.fasseAbwesenheitenZusammen(alle);
		assert(ergebnis.size() == 2);
		assert(ergebnis.get(0).getStart().isEqual(heute));
		assert(ergebnis.get(1).getEnd().isEqual(uebermorgen));
	}
	
	
	@Test
	public void testIstWochende_positiv() {
		LocalDate freitag = LocalDate.of(2018, 3, 2);
		LocalDate montag = LocalDate.of(2018, 3, 5);
		Abwesenheit freitagAbwesenheit = new Abwesenheit(freitag, freitag, "A");
		Abwesenheit montagAbwesenheit = new Abwesenheit(montag, montag, "A");
		
		boolean ergebnis = zusammenfasser.istWochenende(freitagAbwesenheit, montagAbwesenheit);
		assert(ergebnis == true);
	}

	@Test
	public void testIstWochende_negativ() {
		LocalDate freitag = LocalDate.of(2018, 3, 2);
		LocalDate dienstag = LocalDate.of(2018, 3, 6);
		Abwesenheit freitagAbwesenheit = new Abwesenheit(freitag, freitag, "A");
		Abwesenheit dienstagAbwesenheit = new Abwesenheit(dienstag, dienstag, "A");
		
		boolean ergebnis = zusammenfasser.istWochenende(freitagAbwesenheit, dienstagAbwesenheit);
		assert(ergebnis == false);
	}

	
	@Test
	public void testFasseWochendeZusammen_positiv() {
		LocalDate start = LocalDate.of(2018, 3, 8);
		LocalDate freitag = LocalDate.of(2018, 3, 9);
		LocalDate montag = LocalDate.of(2018, 3, 12);
		Abwesenheit freitagAbwesenheit = new Abwesenheit(start, freitag, "A");
		Abwesenheit montagAbwesenheit = new Abwesenheit(montag, montag, "A");
		List<Abwesenheit> abwesenheiten = new LinkedList<Abwesenheit>();
		abwesenheiten.add(freitagAbwesenheit);
		abwesenheiten.add(montagAbwesenheit);
		
		List<Abwesenheit> ergebnis = zusammenfasser.fasseAbwesenheitenZusammen(abwesenheiten);
		assert(ergebnis.size() == 1);
		assert(ergebnis.get(0).getStart().isEqual(start));
		assert(ergebnis.get(0).getEnd().isEqual(montag));
	}
	
	@Test
	public void testKombiniere() {
		LocalDate start = LocalDate.of(2018, 3, 8);
		LocalDate freitag = LocalDate.of(2018, 3, 9);
		LocalDate montag = LocalDate.of(2018, 3, 12);
		Abwesenheit freitagAbwesenheit = new Abwesenheit(start, freitag, "A");
		Abwesenheit montagAbwesenheit = new Abwesenheit(montag, montag, "F");
		
		Abwesenheit ergebnis = zusammenfasser.kombiniere(freitagAbwesenheit, montagAbwesenheit);
		assert(ergebnis.getStart().isEqual(start));
		assert(ergebnis.getEnd().isEqual(montag));
		assert(ergebnis.getStatus().equalsIgnoreCase("A,F"));		
	}

	@Test
	public void testFasseAbwesenheitenZusammen_leer() {
		List<Abwesenheit> alle = new LinkedList<Abwesenheit>();
		List<Abwesenheit> ergebnis = zusammenfasser.fasseAbwesenheitenZusammen(alle);
		assert(ergebnis.isEmpty());
	}

}
