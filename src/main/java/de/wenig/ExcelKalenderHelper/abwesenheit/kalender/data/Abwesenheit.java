package de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

public class Abwesenheit {

	private final LocalDate start;
	private final LocalDate end;
	private final String status;
	
	public Abwesenheit(@NotNull LocalDate start, @NotNull LocalDate end, @NotNull String status) {
		this.start = start;
		this.end = end;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public LocalDate getEnd() {
		return end;
	}

	public LocalDate getStart() {
		return start;
	}

	@Override
	public String toString() {
		return "Abwesenheit [start=" + start + ", end=" + end + ", status=" + status + "]";
	}

}
