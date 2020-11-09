package fr.ifpen.synergreen.service.util;

import com.google.common.base.Strings;
import fr.ifpen.synergreen.domain.enumeration.Civilite;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class StringFormatUtils {
    private static final ZoneId ZONE_FRANCE = ZoneId.of("Europe/Paris");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRANCE);
    private static final DateTimeFormatter DATE_TIME_FORMATTER_INSTRUCTIONS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);

    private StringFormatUtils() {
    }

    public static String formatDate(LocalDate date) {
        return DATE_TIME_FORMATTER.format(date);
    }

    public static String bigDecimalToString(BigDecimal decimal) {
        return decimal == null ? "" : decimal.toPlainString();
    }

    public static String dateToString(Date date) {
        return date == null ? null : localDateTimeToString(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
    }

    public static String localDateToString(LocalDate date) {
        return date == null ? null : DateTimeFormatter.ISO_LOCAL_DATE.format(date);
    }

    public static String localDateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZONE_FRANCE);
        return DateTimeFormatter.ISO_INSTANT.format(zonedDateTime);
    }

    public static String instantToString(Instant dateTime) {
        return dateTime == null ? null : DateTimeFormatter.ISO_INSTANT.format(dateTime.atZone(ZoneId.systemDefault()));
    }

    public static String instantToInstructionDate(ZonedDateTime dateTime) {
        if (!(dateTime == null)) {
            return dateTime == null ? null : DATE_TIME_FORMATTER_INSTRUCTIONS.format(dateTime);
        } else {
            return null;
        }
    }

    public static ZonedDateTime instructionDateToInstant(String date) {
        LocalDateTime localDateTime = LocalDateTime.parse(date, DATE_TIME_FORMATTER_INSTRUCTIONS);
        return localDateTime.atZone(ZONE_FRANCE);
    }

    public static String yearToString(Year year) {
        return year == null ? null : year.toString();
    }

    public static String fullName(Civilite civility, String firstName, String lastName) {
        StringBuilder sb = new StringBuilder();
        if (civility != null) {
            sb.append(civility.getLibelle());
            sb.append(' ');
        }
        if (firstName != null) {
            sb.append(firstName);
            sb.append(' ');
        }
        sb.append(lastName.toUpperCase(Locale.FRANCE));
        return sb.toString();
    }

    public static String normalize(String str) {
        if (Strings.isNullOrEmpty(str)) return "";
        String normalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        return normalizedString
            .replaceAll("[' ]", "_")
            .replaceAll("[^A-Za-z0-9_-]+", "");
    }


    public static ZonedDateTime getCurrentTime() {
        Instant now = Instant.now();
        return ZonedDateTime.ofInstant(now, ZONE_FRANCE);
    }


    public static String instantToFileDate(ZonedDateTime dateTime) {
        String date = instantToInstructionDate(dateTime);
        return date.replace(" ", "_");
    }

    public static ZonedDateTime epochToZonedDateTime(Long epochTime) {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(epochTime), ZONE_FRANCE);
    }

    public static Long zonedDateTimeToEpoch(ZonedDateTime dateTime) {
        LocalDateTime localDateTime = dateTime.toLocalDateTime();
        Instant instant = localDateTime.atZone(ZONE_FRANCE).toInstant();
        return instant.toEpochMilli() / 1000L;
    }

}
