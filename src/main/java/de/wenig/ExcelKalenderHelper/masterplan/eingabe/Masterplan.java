package de.wenig.ExcelKalenderHelper.masterplan.eingabe;

import java.util.List;

public interface Masterplan {

	List<MasterplanItem> getFilteredItemList(MasterplanItemFilter filter);

	List<MasterplanItem> getItemList();
}
