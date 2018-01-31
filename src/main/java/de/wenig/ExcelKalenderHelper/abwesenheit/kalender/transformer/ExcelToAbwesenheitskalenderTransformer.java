package de.wenig.ExcelKalenderHelper.abwesenheit.kalender.transformer;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.Abwesenheitskalender;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Abwesenheit;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Firma;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Organisation;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Person;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Team;

@CacheConfig(cacheNames = "abwesenheitskalender")
@Component
public class ExcelToAbwesenheitskalenderTransformer {

	public static final String REITER = "2018";

	public static final int REIHE_DATUM = 3;

	public static final int SPALTE_NAME = 1;
	public static final int SPALTE_VORNAME = 2;
	public static final int SPALTE_FIRMA = 4;
	public static final int SPALTE_ORGANISATION = 5;
	public static final int SPALTE_TEAM = 6;
	public static final int SPALTE_ABWESENHEITEN_START = 8;
	
	@Autowired
	private AbwesenheitenZusammenfasser zusammenfasser;
	
	public ExcelToAbwesenheitskalenderTransformer() {
		// tue erst mal nichts-....
	}

	@Cacheable
	public Abwesenheitskalender transformExcel(String pathToExcelAwk) {

		Abwesenheitskalender awk = new Abwesenheitskalender();

		try {
			File excelFile = new File(pathToExcelAwk);
			Workbook w = WorkbookFactory.create(excelFile, null, true);
			Sheet plan = w.getSheet(REITER);
			Row dateRow = plan.getRow(REIHE_DATUM);
			for (Row row : plan) {
				if (row.getRowNum() > 3 && row.getRowNum() < 191) {
					Person person = createPersonFromRow(row);
					List<Abwesenheit> abwesenheit = createAbwesenheitFromRow(row, dateRow);
					if (person != null && abwesenheit != null) {
						awk.putAbwesenheiten(person, abwesenheit);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return awk;
	}

	private List<Abwesenheit> createAbwesenheitFromRow(Row row, Row dateRow) {
		List<Abwesenheit> alleAbwesenheiten = readAllAbwesenheiten(row, dateRow);
		List<Abwesenheit> abwesenheiten = zusammenfasser.fasseAbwesenheitenZusammen(alleAbwesenheiten);
		return abwesenheiten;
	}

	



	private  List<Abwesenheit> readAllAbwesenheiten(Row row, Row dateRow) {
		List<Abwesenheit> result = new LinkedList<Abwesenheit>();
		for (Cell zelle : row) {
			if (zelle.getColumnIndex() >= SPALTE_ABWESENHEITEN_START) {
				if (zelle.getCellType() == Cell.CELL_TYPE_STRING) {
					String status = zelle.getStringCellValue();
					LocalDate datum = getDate(dateRow, zelle.getColumnIndex());
					Abwesenheit a = new Abwesenheit(datum, datum, status);
					result.add(a);
				}
			}
		}
		return result;
	}

	private  LocalDate getDate(Row dateRow, int col) {
		LocalDate result = null;
		Cell zelle = dateRow.getCell(col);
		if (zelle != null && zelle.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			Date d = zelle.getDateCellValue();
			result = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}
		return result;
	}

	private  Person createPersonFromRow(Row row) {
		final String name = createNameFromRow(row);
		final String vorname = createVorameFromRow(row);
		final Firma firma = createFirmaFromRow(row);
		final Organisation organisation = createOrganisationFromRow(row);
		final Team team = createTeamFromRow(row);
		final int rowNum = row.getRowNum();
		if (name == null || vorname == null || firma == null || organisation == null || team == null) {
			return null;
		} else {
			return new Person(rowNum, name, vorname, firma, organisation, team);
		}
	}

	private  Team createTeamFromRow(Row row) {
		String teamName = row.getCell(SPALTE_TEAM).getStringCellValue();
		if (StringUtils.isBlank(teamName)) {
			return null;
		} else {
			return new Team(teamName);
		}
	}

	private  Organisation createOrganisationFromRow(Row row) {
		String orgName = row.getCell(SPALTE_ORGANISATION).getStringCellValue();
		if (StringUtils.isBlank(orgName)) {
			return null;
		} else {
			return new Organisation(orgName);
		}
	}

	private  Firma createFirmaFromRow(Row row) {
		String firmaName = row.getCell(SPALTE_FIRMA).getStringCellValue();
		if (StringUtils.isBlank(firmaName)) {
			return null;
		} else {
			return new Firma(firmaName);
		}
	}

	private  String createVorameFromRow(Row row) {
		String vorname = row.getCell(SPALTE_VORNAME).getStringCellValue();
		if (StringUtils.isBlank(vorname)) {
			return null;
		} else {
			return vorname;
		}
	}

	private  String createNameFromRow(Row row) {
		String name = row.getCell(SPALTE_NAME).getStringCellValue();
		if (StringUtils.isBlank(name)) {
			return null;
		} else {
			return name;
		}
	}

}
