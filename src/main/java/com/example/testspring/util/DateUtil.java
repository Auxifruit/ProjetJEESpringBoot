package com.example.testspring.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
//import java.time.temporal.ChronoUnit;

public class DateUtil {
    public static String areDatesValid(String startDateString, String endDateString) {
        try {
            LocalDateTime startDate = LocalDateTime.parse(startDateString);
            LocalDateTime endDate = LocalDateTime.parse(endDateString);

            if (!startDate.toLocalDate().equals(endDate.toLocalDate())) {
                return "Erreur : Les deux dates doivent être le même jour.";
            }

            LocalDateTime validStartDate = startDate.withHour(8).withMinute(30).withSecond(0).withNano(0);
            if (startDate.isBefore(validStartDate)) {
                return "Erreur : Les séances commencent à partir de 8h30.";
            }

            LocalDateTime validEndDate = endDate.withHour(19).withMinute(45).withSecond(0).withNano(0);
            if (endDate.isAfter(validEndDate)) {
                return "Erreur : Les séances se terminent à 19h45.";
            }

//            long minutesBetween = ChronoUnit.MINUTES.between(startDate, endDate);
//            if (minutesBetween > 180) {
//                return "Erreur : Les séances durent un maximum de 3 heures.";
//            }

            if(endDate.isBefore(startDate)) {
                return "Erreur : La date de début doit être avant la date de fin.";
            }
        } catch (DateTimeParseException e) {
            return "Erreur de format de date.";
        }

        return null;
    }

    public static Timestamp dateStringToTimestamp(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);

        Timestamp timestamp = Timestamp.valueOf(localDateTime);

        return timestamp;
    }

    public static String dateFormat(String dateString) {
        if (dateString != null) {
            return dateString.replace("T", " ");
        }
        return null;
    }
}
