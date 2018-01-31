package de.wenig.ExcelKalenderHelper.masterplan.eingabe;

import java.time.LocalDate;

public interface MasterplanItem {
	
	String getDescription();
	String getRelease();
	LocalDate getStartDate();
	LocalDate getEndDate();
	boolean isWholeDayEvent();
	String getResponsible();

}
