package de.wenig.ExcelKalenderHelper.masterplan.ausgabe.rest.comparator;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReleaseComparator implements Comparator<String> {

	private static final Pattern PATTERN = Pattern.compile("PRV_(\\d+).(\\d+)(\\.?)");
	
	@Override
	public int compare(String leftRelease, String rightRelease) {
		final Matcher leftMatcher = PATTERN.matcher(leftRelease);
		final Matcher rightMatcher = PATTERN.matcher(rightRelease);
		if (!leftMatcher.matches() || !rightMatcher.matches()) {
			return leftRelease.compareTo(rightRelease);
		} else {
			final int leftMajor = Integer.valueOf(leftMatcher.group(1));
			final int rightMajor = Integer.valueOf(rightMatcher.group(1));
			final int majorCompare = Integer.compare(leftMajor, rightMajor);
			if (majorCompare != 0) {
				return majorCompare;
			} else {
				final int leftMinor = Integer.valueOf(leftMatcher.group(2));
				final int rightMinor = Integer.valueOf(rightMatcher.group(2));
				final int minorCompare = Integer.compare(leftMinor, rightMinor);
				return minorCompare;
			}
		}
	}
}