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

public class Abwesenheitskalender {

	private final HashMap<Person, List<Abwesenheit>> abwesenheiten;

	private InvertedIndeces invertedIndeces = new InvertedIndeces();
	private LuceneIndex luceneIndex = new LuceneIndex();

	public Abwesenheitskalender(HashMap<Person, List<Abwesenheit>> abwesenheiten) {
		this.abwesenheiten = abwesenheiten;
		invertedIndeces.buildInvertedIndices(abwesenheiten);
		luceneIndex.buildLuceneIndices(abwesenheiten);
	}

	public List<Abwesenheit> getAbwesenheiten(Person p) {
		return Collections.unmodifiableList(abwesenheiten.get(p));
	}
	
	public Set<Firma> getFirmen(){
		return invertedIndeces.getFirmen();
	}

	public Set<Organisation> getOrganisationen(){
		return invertedIndeces.getOrganisationen();
	}
	
	public Set<Team> getTeams(){
		return invertedIndeces.getTeams();
	}
	

	public List<Person> suchePersonenLucene(String suchbegriff) {
		return luceneIndex.suchePersonenLucene(suchbegriff);
	}

	public List<Person> suchePersonenLucene(String firma, String organisation, String team) {
		return luceneIndex.suchePersonenLucene(firma, organisation, team);
	}

	public List<Abwesenheit> getAbwesenheiten(String id) {
		Person p = luceneIndex.getPersonById( Integer.valueOf(id) );
		return getAbwesenheiten(p);
	}
	
	public Person getPerson(String id){
		return luceneIndex.getPersonById( Integer.valueOf(id) );
	}
	

}
