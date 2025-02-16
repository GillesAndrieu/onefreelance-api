package org.grisbi.onefreelance.model.module;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Module Formatter.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Formats {

  public static final DecimalFormat AMOUNT_FORMAT = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.US));

  static {
    AMOUNT_FORMAT.setParseBigDecimal(true);
  }

}
