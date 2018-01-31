package de.wenig.ExcelKalenderHelper.common;

import java.util.Date;



public class Event {
	
	private final String id;
	private final String title;
	private final boolean allDay;
	private final Date start;
	private final Date end;
	
	public Event(String id, String title, boolean allDay, Date start, Date end) {
		super();
		this.id = id;
		this.title = title;
		this.allDay = allDay;
		this.start = start;
		this.end = end;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

}
