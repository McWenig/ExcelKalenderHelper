package de.wenig.ExcelKalenderHelper.abwesenheit.kalender.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Abwesenheit;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Person;
import de.wenig.ExcelKalenderHelper.common.Event;

@Component
public class AbwesenheitToEventMapper {

	public Event buildEvent(Abwesenheit a, Person p){
		/* For whatever reason: Um korrekt dargestellt zu werden, muss das start-Datum einen
		 * Tag vorverlegt werden.
		 * Das Ende-Datum sogar um zwei: einen für die Darstellung 
		 * und einen, da die Zeitangabe immer 0:00 ist*/
		Event e = new Event(getId(a, p), getDescription(a,p), true, morgen(a.getStart()), morgen(morgen(a.getEnd())));
		return e;
	}
	
	public List<Event> buildEventsForPerson(List<Abwesenheit> abwesenheiten, Person person){
		List<Event> events = new LinkedList<Event>();
		for(Abwesenheit a: abwesenheiten){
			events.add(buildEvent(a, person));
		}
		return events;
	}
	
	private Date morgen(Date date){
		Calendar cal  = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	private String getId(@NotNull Abwesenheit a, @NotNull Person p){
		return p.getId()+"_"+ a.hashCode();
	}
	
	private String getDescription(Abwesenheit a, Person p) {
		StringBuilder builder = new StringBuilder();
		builder.append("[").append(a.getStatus()).append("]");
		builder.append(p.getVorname()).append(" ").append(p.getName());
		builder.append("(").append(p.getFirma().getName()).append(")");
		return builder.toString();
	}

}
