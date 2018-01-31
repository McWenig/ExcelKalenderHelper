package de.wenig.ExcelKalenderHelper.masterplan.eingabe.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItem;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItemFilter;

public class ItemFilterChain implements MasterplanItemFilter {

	private List<MasterplanItemFilter> filterList = new LinkedList<MasterplanItemFilter>();
	
	public void addFilter(MasterplanItemFilter... filters){
		for(MasterplanItemFilter filter : filters ){
			filterList.add(filter);
		}
	}
	
	@Override
	public boolean isAcceptable(MasterplanItem mpi) {
		boolean result = true;
		for(MasterplanItemFilter filter : filterList ){
			if( !filter.isAcceptable(mpi) ){
				result = false;
			}
		}
		return result;
	}
	
	public ItemFilterChain(MasterplanItemFilter... filters){
		Collections.addAll(filterList, filters);		
	}

}
