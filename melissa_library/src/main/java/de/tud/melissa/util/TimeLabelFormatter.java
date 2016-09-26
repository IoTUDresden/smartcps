package de.tud.melissa.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Formating the x values as date with the Format HH.MM.SS
 * 
 * @author Andreas Hippler
 * 
 */
public class TimeLabelFormatter extends LabelFormatter {
	private static DateFormat formatter = DateFormat.getTimeInstance(DateFormat.MEDIUM);

	@Override
	protected String formatXValue(double x_value) {
		return formatter.format(new Date(Double.valueOf(x_value).longValue()));
	}

	@Override
	protected String formatYValue(double y_value) {
		return String.format(Locale.US, "%.1f", y_value);
	}
}
