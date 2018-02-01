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
	private boolean indicesUpToDate = false;

	private InvertedIndeces invertedIndeces = new InvertedIndeces();
	private LuceneIndex luceneIndex = new LuceneIndex();
	
	public Abwesenheitskalender(){
		abwesenheiten = new HashMap<Person, List<Abwesenheit>>();
		indicesUpToDate = false;
	}
	
	public void buildIndices(){
		invertedIndeces.buildInvertedIndices(abwesenheiten);
		luceneIndex.buildLuceneIndices(abwesenheiten);
		indicesUpToDate = true;
	}

	public List<Abwesenheit> getAbwesenheiten(Person p) {
		if( ! abwesenheiten.containsKey(p) ){
			throw new IllegalArgumentException("Person "+p.toString()+" unbekannt");
		}
		return Collections.unmodifiableList(abwesenheiten.get(p));
	}
	
	public void putAbwesenheiten(Person p, List<Abwesenheit> abwesenheitenList){
		if(p == null){
			throw new IllegalArgumentException("Person may not be null");
		}
		if(abwesenheitenList == null){
			throw new IllegalArgumentException("abwesenheitenList may not be null");
		}
		indicesUpToDate = false;
		this.abwesenheiten.put(p, abwesenheitenList);
	}
	
	public Set<Firma> getFirmen(){
		if( !indicesUpToDate ){
			buildIndices();
		}
		return invertedIndeces.getFirmen();
	}

	public Set<Organisation> getOrganisationen(){
		if( !indicesUpToDate ){
			buildIndices();
		}
		return invertedIndeces.getOrganisationen();
	}
	
	public Set<Team> getTeams(){
		if( !indicesUpToDate ){
			buildIndices();
		}
		return invertedIndeces.getTeams();
	}
	

	public List<Person> suchePersonenLucene(String suchbegriff) {
		if( !indicesUpToDate ){
			buildIndices();
		}
		return luceneIndex.suchePersonenLucene(suchbegriff);
	}

	public List<Person> suchePersonenLucene(String firma, String organisation, String team) {
		if( !indicesUpToDate ){
			buildIndices();
		}
		return luceneIndex.suchePersonenLucene(firma, organisation, team);
	}

	public List<Abwesenheit> getAbwesenheiten(String id) {
		if( !indicesUpToDate ){
			buildIndices();
		}
		Person p = luceneIndex.getPersonById( Integer.valueOf(id) );
		return getAbwesenheiten(p);
	}
	
	public Person getPerson(String id){
		if( !indicesUpToDate ){
			buildIndices();
		}
		return luceneIndex.getPersonById( Integer.valueOf(id) );
	}	

}
