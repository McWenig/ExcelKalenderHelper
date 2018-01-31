package de.wenig.ExcelKalenderHelper.masterplan.eingabe.impl;

import java.util.Date;

import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItem;

public class MasterplanItemImpl implements MasterplanItem {

	private final String description;
	private final String release;
	private final Date startDate;
	private final Date endDate;
	private final boolean isFullDay;
	private final String responsible;

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getRelease() {
		return release;
	}

	@Override
	public Date getStartDate() {
		return startDate;
	}

	@Override
	public Date getEndDate() {
		return endDate;
	}

	@Override
	public boolean isWholeDayEvent() {
		return isFullDay;
	}

	public MasterplanItemImpl(String description, String release, Date startDate, Date endDate, boolean isFullDay, String responsible) {
		super();
		this.description = description;
		this.release = release;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isFullDay = isFullDay;
		this.responsible = responsible;
	}

	@Override
	public String getResponsible() {
		return responsible;
	}

}
