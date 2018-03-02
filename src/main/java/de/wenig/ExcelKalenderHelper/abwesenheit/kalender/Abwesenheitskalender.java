package de.wenig.ExcelKalenderHelper.abwesenheit.kalender;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Abwesenheit;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Firma;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Organisation;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Person;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Team;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.index.SuchIndex;

public class Abwesenheitskalender {

	public static class Builder {
		private final HashMap<Person, List<Abwesenheit>> builderAbwesenheiten = new HashMap<Person, List<Abwesenheit>>();

		public Builder() {
		}

		public void putAbwesenheiten(Person p, List<Abwesenheit> abwesenheitenList) {
			if (p == null) {
				throw new IllegalArgumentException("Person may not be null");
			}
			if (abwesenheitenList == null) {
				throw new IllegalArgumentException("abwesenheitenList may not be null");
			}
			this.builderAbwesenheiten.put(p, abwesenheitenList);
		}

		public Abwesenheitskalender build() {
			return new Abwesenheitskalender(builderAbwesenheiten);
		}
	}

	private final HashMap<Person, List<Abwesenheit>> abwesenheiten;

	private SuchIndex suchIndex = new SuchIndex();

	private Abwesenheitskalender(HashMap<Person, List<Abwesenheit>> abwesenheiten) {
		this.abwesenheiten = new HashMap<Person, List<Abwesenheit>>(abwesenheiten);
		suchIndex.build(this.abwesenheiten);
	}

	public List<Abwesenheit> getAbwesenheiten(Person p) {
		if (!abwesenheiten.containsKey(p)) {
			throw new IllegalArgumentException("Person " + p.toString() + " unbekannt");
		}
		return Collections.unmodifiableList(abwesenheiten.get(p));
	}

	public Set<Firma> getFirmen() {
		return suchIndex.getFirmen();
	}

	public Set<Organisation> getOrganisationen() {
		return suchIndex.getOrganisationen();
	}

	public Set<Team> getTeams() {
		return suchIndex.getTeams();
	}

	public List<Person> suchePersonenLucene(String suchbegriff) {
		return suchIndex.suchePersonen(suchbegriff);
	}

	public List<Person> suchePersonenLucene(String firma, String organisation, String team) {
		return suchIndex.suchePersonen(firma, organisation, team);
	}

	public List<Abwesenheit> getAbwesenheiten(String id) {
		Person p = suchIndex.getPersonById(Integer.valueOf(id));
		return getAbwesenheiten(p);
	}

	public Person getPerson(String id) {
		return suchIndex.getPersonById(Integer.valueOf(id));
	}

}
