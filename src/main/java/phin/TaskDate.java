package phin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Provides shared strict date parsing and consistent English display formatting.
 */
public class TaskDate {
    private static final DateTimeFormatter DISPLAY = DateTimeFormatter.ofPattern("MMM dd uuuu", Locale.ENGLISH);

    /**
     * Parses a date-only value, rejecting invalid calendar dates and other input formats.
     */
    public static LocalDate parse(String text) {
        try {
            if (!text.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
                throw new IllegalArgumentException("Use a valid date in yyyy-MM-dd format (e.g., 2019-10-15).");
            }
            return LocalDate.parse(text);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Use a valid date in yyyy-MM-dd format (e.g., 2019-10-15).", exception);
        }
    }

    /**
     * Formats a date for display without changing its ISO representation used in storage.
     */
    public static String format(LocalDate date) {
        return date.format(DISPLAY);
    }
}
