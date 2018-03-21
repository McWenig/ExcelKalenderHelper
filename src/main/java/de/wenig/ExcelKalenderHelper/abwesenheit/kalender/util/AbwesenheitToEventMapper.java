package de.wenig.ExcelKalenderHelper.abwesenheit.kalender.util;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Abwesenheit;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Person;
import de.wenig.ExcelKalenderHelper.common.Event;

@Component
public class AbwesenheitToEventMapper {

	public Event buildEvent(Abwesenheit a, Person p) {
		/*
		 * For whatever reason: Um korrekt dargestellt zu werden, muss das
		 * start-Datum einen Tag vorverlegt werden. Das Ende-Datum sogar um
		 * zwei: einen für die Darstellung und einen, da die Zeitangabe immer
		 * 0:00 ist
		 */
		Date start = Date.from(a.getStart().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(a.getEnd().plusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());

		Event e = new Event(getId(a, p), getDescription(a, p), true, start, end);
		return e;
	}

	public List<Event> buildEventsForPerson(List<Abwesenheit> abwesenheiten, Person person) {
		List<Event> events = new LinkedList<Event>();
		for (Abwesenheit a : abwesenheiten) {
			events.add(buildEvent(a, person));
		}
		return events;
	}

	private String getId(@NotNull Abwesenheit a, @NotNull Person p) {
		return p.getId() + "_" + a.hashCode();
	}

	private String getDescription(Abwesenheit a, Person p) {
		StringBuilder builder = new StringBuilder();
		builder.append("[").append(a.getStatus()).append("]");
		builder.append(p.getVorname()).append(" ").append(p.getName());
		builder.append("(").append(p.getFirma().getName()).append(")");
		if( ChronoUnit.DAYS.between(a.getStart(), a.getEnd()) > 3 ){
			builder.append( getZeitraumString(a) );
		}
		return builder.toString();
	}
	
	private String getZeitraumString(Abwesenheit a){
		StringBuilder builder = new StringBuilder();
		builder.append(" ");
		builder.append( a.getStart().format(DateTimeFormatter.ofPattern("dd.MM.")));
		builder.append(" - ");
		builder.append( a.getEnd().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
		return builder.toString();
	}

}
