package de.wenig.ExcelKalenderHelper.masterplan.eingabe.impl;

import java.util.Date;

import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItem;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItemFilter;

public class DateRangeItemFilter implements MasterplanItemFilter {

	public Date startDate;
	public Date endDate;

	public DateRangeItemFilter(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	public boolean isAcceptable(MasterplanItem mpi) {
		if (mpi.getStartDate().after(startDate) && mpi.getStartDate().before(endDate)) {
			return true;
		}
		return false;
	}

}
