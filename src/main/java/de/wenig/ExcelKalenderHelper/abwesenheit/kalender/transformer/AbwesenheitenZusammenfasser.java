package de.wenig.ExcelKalenderHelper.abwesenheit.kalender.transformer;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Abwesenheit;

@Component
public class AbwesenheitenZusammenfasser {

	public List<Abwesenheit> fasseAbwesenheitenZusammen(List<Abwesenheit> alleAbwesenheiten) {
		ArrayList<Abwesenheit> abwesenheiten = new ArrayList<Abwesenheit>();
		if (!alleAbwesenheiten.isEmpty()) {
			for (Abwesenheit aktuellerEintrag : alleAbwesenheiten) {
				if (!abwesenheiten.isEmpty()) {
					Abwesenheit vorherigerEintrag = vorigeAbwesenheit(abwesenheiten);
					if (istFortetzung(vorherigerEintrag, aktuellerEintrag)) {
						Abwesenheit temp = kombiniere(vorigeAbwesenheit(abwesenheiten), aktuellerEintrag);
						abwesenheiten.set(abwesenheiten.size() - 1, temp);
					} else {
						abwesenheiten.add(aktuellerEintrag);
					}
				} else {
					abwesenheiten.add(aktuellerEintrag);
				}
			}
		}
		return abwesenheiten;
	}

	public Abwesenheit vorigeAbwesenheit(List<Abwesenheit> abwesenheiten) {
		return abwesenheiten.get(abwesenheiten.size() - 1);
	}

	public Abwesenheit kombiniere(Abwesenheit letzterEintrag, Abwesenheit aktuellerEintrag) {
		Abwesenheit ergebnis = new Abwesenheit(letzterEintrag.getStart(), aktuellerEintrag.getEnd(),
				kombiniereStatus(letzterEintrag.getStatus(), aktuellerEintrag.getStatus()));
		return ergebnis;
	}

	public String kombiniereStatus(String statusAlt, String statusNeu) {
		List<String> statusList = new ArrayList<String>();
		statusList.addAll( Arrays.asList(StringUtils.split(statusAlt, ",") ) );
		if( ! statusList.contains(statusNeu) ){
			statusList.add(statusNeu);
		}
		return StringUtils.join(statusList.iterator(), ",");
	}

	public boolean istFortetzung(Abwesenheit letzterEintrag, Abwesenheit aktuellerEintrag) {
		boolean result = false;

		if ( istFolgetag(letzterEintrag, aktuellerEintrag) || istWochenende(letzterEintrag, aktuellerEintrag) ) {
			result = true;
		} 

		return result;
	}
	
	public boolean istFolgetag(Abwesenheit letzterEintrag, Abwesenheit aktuellerEintrag){
		boolean result = false;

		if (aktuellerEintrag.getStart().isEqual(letzterEintrag.getEnd().plusDays(1))) {
			result = true;
		}
		return result;
		
	}

	public boolean istWochenende(Abwesenheit letzterEintrag, Abwesenheit aktuellerEintrag) {
		boolean result = false;

		if (letzterEintrag.getEnd().getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
			if (aktuellerEintrag.getStart().getDayOfWeek().equals(DayOfWeek.MONDAY)) {
				if (aktuellerEintrag.getStart().isEqual(letzterEintrag.getEnd().plusDays(3))) {
					result = true;
				}
			}
		}
		return result;
	}
}
