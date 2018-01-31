package de.wenig.ExcelKalenderHelper.common;

public class AutocompleteItem {
	private final String label;
	private final String value;

	public AutocompleteItem(String label, String value) {
		super();
		this.label = label;
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}
}
