package de.wenig.ExcelKalenderHelper.masterplan.eingabe;

import java.util.Date;

public interface MasterplanItem {
	
	String getDescription();
	String getRelease();
	Date getStartDate();
	Date getEndDate();
	boolean isWholeDayEvent();
	String getResponsible();

}
