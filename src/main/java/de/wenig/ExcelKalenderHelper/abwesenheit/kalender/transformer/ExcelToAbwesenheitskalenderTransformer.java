package de.wenig.ExcelKalenderHelper.abwesenheit.kalender.transformer;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	
	private Log log = LogFactory.getLog(ExcelToAbwesenheitskalenderTransformer.class);

	public static final String REITER = "2018";

	public static final int REIHE_DATUM = 3;

	public static final int SPALTE_NAME = 1;
	public static final int SPALTE_VORNAME = 2;
	public static final int SPALTE_FIRMA = 4;
	public static final int SPALTE_ORGANISATION = 5;
	public static final int SPALTE_TEAM = 6;
	public static final int SPALTE_ABWESENHEITEN_START = 8;
	
	//TODO diese Konstante muss durch etwas sinnvolles erestzt werden. 
	//Jetzt ist sie erstmal da, um die Performance zu erhöhen 
	public static final int  MAX_ROW = 200;
	
	@Autowired
	private AbwesenheitenZusammenfasser zusammenfasser;
	

	@Cacheable(sync=true)
	public Abwesenheitskalender transformExcel(String pathToExcelAwk) {

		Abwesenheitskalender.Builder awk = new Abwesenheitskalender.Builder();

		File excelFile = new File(pathToExcelAwk);
		try (Workbook w = WorkbookFactory.create(excelFile, null, true)){
			Sheet plan = w.getSheet(REITER);
			Row dateRow = plan.getRow(REIHE_DATUM);
			for (Row row : plan) {
				if (row.getRowNum() > REIHE_DATUM+1 && row.getRowNum() < MAX_ROW) {
					Person person = createPersonFromRow(row);
					List<Abwesenheit> abwesenheit = createAbwesenheitFromRow(row, dateRow);
					if (person != null && abwesenheit != null) {
						awk.putAbwesenheiten(person, abwesenheit);
					}
				}
			}
		} catch (Exception e) {
			log.error("Fehler in der Umwandlung", e);
		}

		return awk.build();
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
		Team result = null;
		try{
			String name = row.getCell(SPALTE_TEAM).getStringCellValue();
			if ( !StringUtils.isBlank(name)) {
				result = new Team(name);
			}
			
		}catch (Exception e) {
			log.warn("Zeile "+row.getRowNum()+" enthält kein Team");
		}
		return result;
	}

	private  Organisation createOrganisationFromRow(Row row) {
		Organisation result = null;
		try{
			String name = row.getCell(SPALTE_ORGANISATION).getStringCellValue();
			if ( !StringUtils.isBlank(name)) {
				result = new Organisation(name);
			}
			
		}catch (Exception e) {
			log.warn("Zeile "+row.getRowNum()+" enthält keine Organisation");
		}
		return result;
	}

	private  Firma createFirmaFromRow(Row row) {
		Firma result = null;
		try{
			String name = row.getCell(SPALTE_FIRMA).getStringCellValue();
			if ( !StringUtils.isBlank(name)) {
				result = new Firma(name);
			}
			
		}catch (Exception e) {
			log.warn("Zeile "+row.getRowNum()+" enthält keine Firma");
		}
		return result;
	}

	private  String createVorameFromRow(Row row) {
		String result = null;
		try{
			String name = row.getCell(SPALTE_VORNAME).getStringCellValue();
			if ( !StringUtils.isBlank(name)) {
				result = name;
			}
			
		}catch (Exception e) {
			log.warn("Zeile "+row.getRowNum()+" enthält keinen Vornamen");
		}
		return result;
	}

	private  String createNameFromRow(Row row) {
		String result = null;
		try{
			String name = row.getCell(SPALTE_NAME).getStringCellValue();
			if ( !StringUtils.isBlank(name)) {
				result = name;
			}
			
		}catch (Exception e) {
			log.warn("Zeile "+row.getRowNum()+" enthält keinen Namen");
		}
		return result;
	}

}
