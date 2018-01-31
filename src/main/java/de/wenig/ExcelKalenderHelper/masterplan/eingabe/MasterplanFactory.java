package de.wenig.ExcelKalenderHelper.masterplan.eingabe;

import java.io.File;

public interface MasterplanFactory {

	public final static String MP = "MP";
	public final static int TYPE_COLUMN = 1;
	public final static int DESC_COLUMN = 3;
	public final static int EXT_DESC_COLUMN = 4;
	public final static int RELEASE_COLUMN = 5;
	public final static int DATE_COLUMN = 8;
	public final static int OWNER_COLUMN = 13;

	public Masterplan produceMasterplanFromExcel(File excelFile);

}
