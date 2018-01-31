package de.wenig.ExcelKalenderHelper.abwesenheit.kalender;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Abwesenheit;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Firma;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Organisation;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Person;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Team;

public class InvertedIndeces {
	private Map<Firma, Set<Person>> firmen = new HashMap<Firma, Set<Person>>();
	private Map<Organisation, Set<Person>> organisationen = new HashMap<Organisation, Set<Person>>();
	private Map<Team, Set<Person>> teams = new HashMap<Team, Set<Person>>();
	private Map<String, Set<Person>> namen = new HashMap<String, Set<Person>>();
	private Map<String, Set<Person>> vornamen = new HashMap<String, Set<Person>>();

	public void clearIndex() {
		firmen.clear();
		organisationen.clear();
		teams.clear();
		namen.clear();
		vornamen.clear();
	}

	public void buildInvertedIndices(HashMap<Person, List<Abwesenheit>> abwesenheiten) {
		for (Person p : abwesenheiten.keySet()) {

			Set<Person> indexResult = firmen.get(p.getFirma());
			if (indexResult == null) {
				indexResult = new HashSet<Person>();
				firmen.put(p.getFirma(), indexResult);
			}
			indexResult.add(p);

			indexResult = organisationen.get(p.getOrganisation());
			if (indexResult == null) {
				indexResult = new HashSet<Person>();
				organisationen.put(p.getOrganisation(), indexResult);
			}
			indexResult.add(p);

			indexResult = teams.get(p.getTeam());
			if (indexResult == null) {
				indexResult = new HashSet<Person>();
				teams.put(p.getTeam(), indexResult);
			}
			indexResult.add(p);

			indexResult = namen.get(p.getName());
			if (indexResult == null) {
				indexResult = new HashSet<Person>();
				namen.put(p.getName(), indexResult);
			}
			indexResult.add(p);

			indexResult = vornamen.get(p.getVorname());
			if (indexResult == null) {
				indexResult = new HashSet<Person>();
				vornamen.put(p.getVorname(), indexResult);
			}
			indexResult.add(p);
		}

	}

	public Set<Firma> getFirmen() {
		return Collections.unmodifiableSet(firmen.keySet());
	}

	public Set<Organisation> getOrganisationen() {
		return Collections.unmodifiableSet(organisationen.keySet());
	}

	public Set<Team> getTeams() {
		return Collections.unmodifiableSet(teams.keySet());
	}


}
