package de.wenig.ExcelKalenderHelper.masterplan.eingabe.impl;

import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItem;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItemFilter;

public class ReleaseItemFilter implements MasterplanItemFilter {

	public String release;
	
	public ReleaseItemFilter(String release) {
		super();
		this.release = release;
	}

	@Override
	public boolean isAcceptable(MasterplanItem mpi) {
		return release.equalsIgnoreCase(mpi.getRelease());
	}

}
