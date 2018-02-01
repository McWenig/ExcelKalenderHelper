package de.wenig.ExcelKalenderHelper.masterplan.eingabe.factories;

import java.time.LocalDate;

import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItemFilter;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.impl.DateRangeItemFilter;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.impl.ItemFilterChain;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.impl.ReleaseItemFilter;

public class MasterplanFilterFactory {

	public static MasterplanItemFilter createDateRangeFilter(LocalDate start, LocalDate end) {
		return new DateRangeItemFilter(start, end);
	}

	public static MasterplanItemFilter createReleaseFilter(String release) {
		return new ReleaseItemFilter(release);
	}
	
	public static MasterplanItemFilter createFilterChain(MasterplanItemFilter... filters){
		return new ItemFilterChain(filters);
	}
	
	public static MasterplanItemFilter createFilter(LocalDate start, LocalDate end, String release) {
		MasterplanItemFilter filter = null;

		final boolean dateSet = start != null && end != null;
		final boolean releaseSet = release != null;

		MasterplanItemFilter dateFilter = null;
		MasterplanItemFilter releaseFilter = null;

		if (dateSet) {
			dateFilter = createDateRangeFilter(start, end);
			filter = dateFilter;
		}
		if (releaseSet) {
			releaseFilter = createReleaseFilter(release);
			filter = releaseFilter;
		}

		if (dateSet && releaseSet) {
			filter = createFilterChain(dateFilter, releaseFilter);
		}

		return filter;
	}

}
