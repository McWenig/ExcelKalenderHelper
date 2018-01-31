package de.wenig.ExcelKalenderHelper.masterplan.eingabe.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotNull;

import de.wenig.ExcelKalenderHelper.masterplan.eingabe.Masterplan;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItem;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItemFilter;

public class MasterplanImpl implements Masterplan {

	private List<MasterplanItem> items = new LinkedList<MasterplanItem>();

	@Override
	public List<MasterplanItem> getFilteredItemList(@NotNull MasterplanItemFilter filter) {
		List<MasterplanItem> result = new LinkedList<MasterplanItem>();
		for (MasterplanItem mpi : items) {
			if (filter.isAcceptable(mpi)) {
				result.add(mpi);
			}
		}
		return Collections.unmodifiableList(result);
	}

	public void addItem(@NotNull MasterplanItem mpi) {
		items.add(mpi);
	}

	@Override
	public List<MasterplanItem> getItemList() {
		return Collections.unmodifiableList(items);
	}

}
