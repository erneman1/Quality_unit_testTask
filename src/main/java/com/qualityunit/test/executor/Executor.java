package com.qualityunit.test.executor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Executor {

    private static final int SERVICE_INDEX_ID = 1;
    private static final int QUESTION_INDEX_ID = 2;
    private static final int DATE_INDEX_ID = 4;
    private static final int END_DATE_INDEX_ID = 5;
    private static final int WAITING_INDEX_ID = 5;
    private final Logger logger = Logger.getLogger(Executor.class.getName());

    private final List<String> inputList = new ArrayList<>();

    public void execute() throws IOException {

        try(Stream<String> lines = Files.lines(Paths.get("src/main/resources/input"))) {
            lines.forEach(line -> {
                inputList.add(line);
                if (line.startsWith("D")) {
                    double d = inputList.stream()
                            .filter(s -> s.startsWith("C"))
                            .filter(s -> queryCheck(s, line))
                            .mapToInt(Executor::getWaitingTime)
                            .average().orElse(0);
                    if (d > 0) {
                        logger.info(String.valueOf(d));
                    } else {
                        logger.info("-");
                    }
                }
            });
        }
    }

    private static boolean queryCheck(String stringC, String stringD) {
        String[] lineCArray = lineToArray(stringC);
        String[] lineDArray = lineToArray(stringD);

        if (hasNotLimitedIds(stringD)) {
            return checkDate(lineCArray, lineDArray);
        } else if (hasServiceNotLimitedId(stringD)) {
            return getIdValue(lineCArray[QUESTION_INDEX_ID]).equals(getIdValue(lineDArray[QUESTION_INDEX_ID])) &&
                    checkDate(lineCArray, lineDArray);
        } else if (hasQuestionNotLimitedId(stringD)) {
            return getIdValue(lineCArray[SERVICE_INDEX_ID]).equals(getIdValue(lineDArray[SERVICE_INDEX_ID])) &&
                    checkDate(lineCArray, lineDArray);
        } else return getIdValue(lineCArray[SERVICE_INDEX_ID]).equals(getIdValue(lineDArray[SERVICE_INDEX_ID])) &&
                getIdValue(lineCArray[QUESTION_INDEX_ID]).equals(getIdValue(lineDArray[QUESTION_INDEX_ID])) &&
                checkDate(lineCArray, lineDArray);
    }

    private static boolean checkDate(String[] arrayC, String[] arrayD) {
        if (arrayD.length > END_DATE_INDEX_ID) {
            return isDateInRange(arrayC[DATE_INDEX_ID], arrayD[DATE_INDEX_ID], arrayD[END_DATE_INDEX_ID]);
        } else {
            return isDateEquals(arrayC[DATE_INDEX_ID], arrayD[DATE_INDEX_ID]);
        }
    }

    private static boolean isDateInRange(String date, String dateStart, String dateEnd) {
        boolean compared;

        compared = (parseDate(date).equals(parseDate(dateStart)) || parseDate(date).after(parseDate(dateStart))) &&
                (parseDate(date).equals(parseDate(dateEnd)) || parseDate(date).before(parseDate(dateEnd)));

        return compared;
    }

    private static boolean isDateEquals(String date, String dateStart) {
        return parseDate(date).equals(parseDate(dateStart));
    }

    private static boolean hasNotLimitedIds(String line) {
        return lineToArray(line)[SERVICE_INDEX_ID].equals("*") && lineToArray(line)[QUESTION_INDEX_ID].equals("*");
    }

    private static boolean hasServiceNotLimitedId(String line) {
        return lineToArray(line)[SERVICE_INDEX_ID].equals("*");
    }

    private static boolean hasQuestionNotLimitedId(String line) {
        return lineToArray(line)[QUESTION_INDEX_ID].equals("*");
    }

    private static String[] lineToArray(String s) {
        return s.split("\\s|-");
    }

    private static String getIdValue(String line) {
        return line.split("\\.")[0];
    }

    private static int getWaitingTime(String lineC) {
        return Integer.parseInt(lineToArray(lineC)[WAITING_INDEX_ID]);
    }

    private static Date parseDate(String string) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("dd.MM.yyyy").parse(string);
        } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
        }
        return date;
    }

}
