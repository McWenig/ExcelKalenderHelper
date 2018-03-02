package de.wenig.ExcelKalenderHelper.abwesenheit.kalender.index;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Abwesenheit;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Firma;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Organisation;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Person;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Team;

@Component
public class SuchIndex {
	private InvertedIndeces invertedIndeces = new InvertedIndeces();
	private LuceneIndex luceneIndex = new LuceneIndex();

	public Set<Firma> getFirmen() {
		return invertedIndeces.getFirmen();
	}

	public Set<Organisation> getOrganisationen() {
		return invertedIndeces.getOrganisationen();
	}

	public Set<Team> getTeams() {
		return invertedIndeces.getTeams();
	}

	public List<Person> suchePersonen(String suchbegriff) {
		return luceneIndex.suchePersonen(suchbegriff);
	}

	public List<Person> suchePersonen(String firma, String organisation, String team) {
		return luceneIndex.suchePersonen(firma, organisation, team);
	}

	public Person getPersonById(int id) {
		return luceneIndex.getPersonById(id);
	}

	public void build(HashMap<Person, List<Abwesenheit>> abwesenheiten) {
		invertedIndeces.buildInvertedIndices(abwesenheiten);
		luceneIndex.buildLuceneIndices(abwesenheiten);
	}
}
