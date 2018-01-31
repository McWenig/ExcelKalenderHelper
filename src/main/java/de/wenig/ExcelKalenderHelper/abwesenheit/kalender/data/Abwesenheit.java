package de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class Abwesenheit {

	private final Date start;
	private final Date end;
	private final String status;
	
	public Abwesenheit(@NotNull Date start, @NotNull Date end, @NotNull String status) {
		this.start = start;
		this.end = end;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public Date getEnd() {
		return end;
	}

	public Date getStart() {
		return start;
	}

	@Override
	public String toString() {
		return "Abwesenheit [start=" + start + ", end=" + end + ", status=" + status + "]";
	}

}
