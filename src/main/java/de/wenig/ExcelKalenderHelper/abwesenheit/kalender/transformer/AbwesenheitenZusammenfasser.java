package de.wenig.ExcelKalenderHelper.abwesenheit.kalender.transformer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.stereotype.Component;

import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Abwesenheit;

@Component
public class AbwesenheitenZusammenfasser {

	public List<Abwesenheit> fasseAbwesenheitenZusammen(List<Abwesenheit> alleAbwesenheiten) {
		ArrayList<Abwesenheit> abwesenheiten = new ArrayList<Abwesenheit>();
		if (!alleAbwesenheiten.isEmpty()) {
			for (Abwesenheit aktuellerEintrag : alleAbwesenheiten) {
				if ( !abwesenheiten.isEmpty() && istFortetzung(abwesenheiten.get(abwesenheiten.size()-1), aktuellerEintrag)) {
					Abwesenheit temp = kombiniere(abwesenheiten.get(abwesenheiten.size()-1), aktuellerEintrag);
					abwesenheiten.set(abwesenheiten.size()-1, temp);;
				} else {
					abwesenheiten.add(aktuellerEintrag);
				}
			}
		}
		return abwesenheiten;
	}
	
	public Abwesenheit kombiniere(Abwesenheit letzterEintrag, Abwesenheit aktuellerEintrag) {
		return new Abwesenheit(letzterEintrag.getStart(), aktuellerEintrag.getEnd(), letzterEintrag.getStatus());
	}

	public boolean istFortetzung(Abwesenheit letzterEintrag, Abwesenheit aktuellerEintrag) {
		boolean result = false;

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(letzterEintrag.getEnd());
		cal.add(Calendar.DATE, 1);

		if (aktuellerEintrag.getStart().equals(cal.getTime())) {
			if (aktuellerEintrag.getStatus() == letzterEintrag.getStatus()) {
				result = true;
			}
		}
		return result;
	}
}
