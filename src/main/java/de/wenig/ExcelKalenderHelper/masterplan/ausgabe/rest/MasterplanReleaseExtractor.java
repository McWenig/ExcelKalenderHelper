package de.wenig.ExcelKalenderHelper.masterplan.ausgabe.rest;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.wenig.ExcelKalenderHelper.masterplan.ausgabe.rest.comparator.ReleaseComparator;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.Masterplan;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItem;

public class MasterplanReleaseExtractor {
	
	
	public static List<String> getReleaseList(Masterplan mp) {
		final List<String> result = new LinkedList<>();

		final Set<String> releasesSet = new HashSet<>();
		for (final MasterplanItem mpi : mp.getItemList()) {
			releasesSet.add(mpi.getRelease());
		}

		result.addAll(releasesSet);
		result.sort(new ReleaseComparator());

		return result;
	}

}
