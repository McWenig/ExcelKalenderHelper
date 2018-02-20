package de.wenig.ExcelKalenderHelper.masterplan.eingabe.factories;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import de.wenig.ExcelKalenderHelper.masterplan.eingabe.Masterplan;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanFactory;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItem;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.impl.MasterplanImpl;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.impl.MasterplanItemImpl;

@Component
public class MasterplanFactoryImpl implements MasterplanFactory {

	private Log logger = LogFactory.getLog(MasterplanFactoryImpl.class);

	public Masterplan produceMasterplanFromExcel(File excelFile) {
		MasterplanImpl result = new MasterplanImpl();

		try (Workbook w = WorkbookFactory.create(excelFile, null, true)) {
			Sheet plan = w.getSheet(MP);
			for (Row row : plan) {
				MasterplanItem mpi = createMasteplanItemFromRow(row);
				if (mpi != null) {
					result.addItem(mpi);
				}
			}
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			logger.error("Fehler beim lesen des Masterplan-Dokuments", e);
		}

		return result;
	}

	private static MasterplanItem createMasteplanItemFromRow(Row row) {
		MasterplanItemImpl result = null;

		if (row.getRowNum() == 0) {
			// skip first row
			return null;
		}

		String description = getDescriptionFromRow(row);
		String release = getReleaseFromRow(row);
		LocalDate startDate = getStartDateFromRow(row);
		LocalDate endDate = null;
		boolean fullDay = true;
		String responsible = getResponsibleFromRow(row);

		result = new MasterplanItemImpl(description, release, startDate, endDate, fullDay, responsible);

		return result;
	}

	private static LocalDate getStartDateFromRow(Row row) {
		LocalDate result = null;
		if (row == null) {
			return result;
		}
		Cell dateCell = row.getCell(DATE_COLUMN);
		if (dateCell == null) {
			return result;
		}

		FormulaEvaluator evaluator = row.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
		CellValue value = evaluator.evaluate(dateCell);
		Date d = DateUtil.getJavaDate(value.getNumberValue());
		result = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return result;
	}

	private static String getDescriptionFromRow(Row row) {
		String release = getReleaseFromRow(row);
		String description = row.getCell(DESC_COLUMN).getStringCellValue();
		String type = row.getCell(TYPE_COLUMN).getStringCellValue();
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(release).append("] ");
		if ("B".equalsIgnoreCase(type)) {
			sb.append("Beginn ");
		} else if ("E".equalsIgnoreCase(type)) {
			sb.append("Ende ");
		}
		sb.append(description);
		return sb.toString();
	}

	private static String getResponsibleFromRow(Row row) {
		String owner = null;
		if (row != null) {
			owner = row.getCell(OWNER_COLUMN).getStringCellValue();
		}
		return owner;
	}

	private static String getReleaseFromRow(Row row) {
		return row.getCell(RELEASE_COLUMN).getStringCellValue();
	}

}
