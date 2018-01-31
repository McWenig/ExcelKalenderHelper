package de.wenig.ExcelKalenderHelper.masterplan.ausgabe.rest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.wenig.ExcelKalenderHelper.common.Event;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.Masterplan;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItem;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItemFilter;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.factories.MasterplanFilterFactory;

public class MasterplanToEventTransformer {

	public static List<Event> transform(Masterplan mp, LocalDate start, LocalDate end, String release) {
		final List<Event> eventList = new LinkedList<>();

		final MasterplanItemFilter filter = MasterplanFilterFactory.createFilter(start, end, release);

		for (final MasterplanItem mpi : mp.getFilteredItemList(filter)) {
			final String id = String.valueOf(mpi.hashCode());
			String title = mpi.getDescription();
			if(mpi.getResponsible() != null ){
				title = title
						.concat(" [")
						.concat(mpi.getResponsible())
						.concat("]");
			}
			final boolean allDay = true;
			/* Die Darstellung im Kalender braucht aus irgendeinem Grund einen Tag spaeter*/
			final Date itemStart = Date.from(mpi.getStartDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
			
			final Event event = new Event(id, title, allDay, itemStart, itemStart);
			eventList.add(event);
		}
		return eventList;
	}
	
}
