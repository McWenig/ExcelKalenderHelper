package de.wenig.ExcelKalenderHelper.masterplan.eingabe.impl;

import java.time.LocalDate;

import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItem;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItemFilter;

public class DateRangeItemFilter implements MasterplanItemFilter {

	public LocalDate startDate;
	public LocalDate endDate;

	public DateRangeItemFilter(LocalDate startDate, LocalDate endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	public boolean isAcceptable(MasterplanItem mpi) {
		if (mpi.getStartDate().isAfter(startDate) && mpi.getStartDate().isBefore(endDate)) {
			return true;
		}
		return false;
	}

}
