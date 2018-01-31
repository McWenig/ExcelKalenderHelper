package de.wenig.ExcelKalenderHelper.abwesenheit.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.Abwesenheitskalender;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Firma;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Organisation;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Person;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Team;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.transformer.ExcelToAbwesenheitskalenderTransformer;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.util.AbwesenheitToEventMapper;
import de.wenig.ExcelKalenderHelper.common.AutocompleteItem;
import de.wenig.ExcelKalenderHelper.common.Event;

@RestController
public class AbwesenheitController {

	@Value("${awkFile}")
	private String path;
	
	@Autowired
	private ExcelToAbwesenheitskalenderTransformer transformer;
	
	@Autowired
	private AbwesenheitToEventMapper mapper;

	@RequestMapping("/person/suche")
	public List<AutocompleteItem> suchePersonen(@RequestParam String term) {

		List<AutocompleteItem> result = new LinkedList<AutocompleteItem>();
		Abwesenheitskalender awk = transformer.transformExcel(path);
		List<Person> personen =  awk.suchePersonenLucene(term);
		for(Person p: personen){
			AutocompleteItem item = mapAutocompleteItem(p);
			result.add(item);
		}
		return result;
	}

	private AutocompleteItem mapAutocompleteItem(Person p) {
		return new AutocompleteItem(p.getVorname().concat(" ").concat(p.getName()), String.valueOf(p.getId()));
	}

	@RequestMapping("/personen/firma/{firma}/organisation/{organisation}/team/{team}")
	public List<Person> suchePersonen(@PathVariable("firma") String firma, @PathVariable("organisation") String organisation, @PathVariable("team") String team) {

		Abwesenheitskalender awk = transformer.transformExcel(path);
		return awk.suchePersonenLucene(firma,organisation,team);
		
	}

	@RequestMapping("/Person/{id}/abwesenheiten")
	public List<Event> getAbwesenheiten(@PathVariable("id") String id) {

		Abwesenheitskalender awk = transformer.transformExcel(path);
		return mapper.buildEventsForPerson(awk.getAbwesenheiten(id),awk.getPerson(id));
		
	}
	
	@RequestMapping("/Person/firmen")
	public Set<Firma> listFirmen() {

		Abwesenheitskalender awk = transformer.transformExcel(path);
		Set<Firma> firmen = awk.getFirmen();
		return firmen;
		
	}

	@RequestMapping("/Person/organisationen")
	public Set<Organisation> listOrganisationen() {

		Abwesenheitskalender awk = transformer.transformExcel(path);
		Set<Organisation> firmen = awk.getOrganisationen();
		return firmen;
		
	}

	@RequestMapping("/Person/teams")
	public Set<Team> listTeams() {

		Abwesenheitskalender awk = transformer.transformExcel(path);
		Set<Team> firmen = awk.getTeams();
		return firmen;
		
	}

	
}
