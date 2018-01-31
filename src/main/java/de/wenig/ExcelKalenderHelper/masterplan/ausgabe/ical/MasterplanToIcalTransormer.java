package de.wenig.ExcelKalenderHelper.masterplan.ausgabe.ical;

import java.net.SocketException;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import de.wenig.ExcelKalenderHelper.masterplan.eingabe.Masterplan;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItem;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanItemFilter;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.factories.MasterplanFilterFactory;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.TzId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;


public class MasterplanToIcalTransormer {

	static {
		try {
			UID_GENERATOR = new UidGenerator("1");
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String DEFAULT_TIMEZONE = "Europe/Berlin";

	@Autowired
	private static UidGenerator UID_GENERATOR;


	public static Calendar transform(Masterplan m, String release) {
		Calendar result = new Calendar();
		result.getProperties().add(new ProdId("- Masterplan " + release + " -"));
		result.getProperties().add(Version.VERSION_2_0);
		result.getProperties().add(CalScale.GREGORIAN);

		List<MasterplanItem> itemList;
		

		if(release != null){
			final MasterplanItemFilter itemFilter = MasterplanFilterFactory.createReleaseFilter(release);
			itemList = m.getFilteredItemList(itemFilter);
		} else {
			itemList = m.getItemList();
		}
		
		for (MasterplanItem mi : itemList) {
			result.getComponents().add(createCalComp(mi));
		}

		return result;
	}

	private static CalendarComponent createCalComp(MasterplanItem mi) {
		java.util.Date startDatePlusOne = DateUtils.addDays(mi.getStartDate(), 1);
		Date startDate = new Date(startDatePlusOne.getTime());

		// String iso88591 = new
		// String(mi.getDescription().getBytes(Charset.forName("ISO-8859-1")));
		// VEvent result = new VEvent(startDate, iso88591);

		VEvent result = new VEvent(startDate, getEventDescription(mi));

		// initialise as an all-day event..
		result.getProperties().getProperty(Property.DTSTART).getParameters().add(Value.DATE);

		result.getProperties().add(UID_GENERATOR.generateUid());

		addDefaultTimezone(result);

		return result;
	}

	private static String getEventDescription(MasterplanItem mi) {
		// Die Event-Bescriebung darf maximal 255 Zeichen lang sein
		String desc = mi.getDescription();
		String shortDesc = desc.substring(0, Math.min(255, desc.length()));
		return shortDesc;
	}

	private static void addDefaultTimezone(CalendarComponent comp) {

		// create a Java util timezone to have basic validation
		TimeZone tz = TimeZone.getTimeZone(DEFAULT_TIMEZONE);

		// create the fishy ical4j object
		TzId timezoneProperty = new TzId(tz.getID());

		// add timezone information.
		comp.getProperties().add(timezoneProperty);

	}

}
