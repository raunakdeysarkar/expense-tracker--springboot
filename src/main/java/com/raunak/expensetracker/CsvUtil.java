package com.raunak.expensetracker;

public class CsvUtil {

    public static String escape(String field) {

        if (field == null) {
            field = "";
        }

        boolean needsQuoting =
                field.contains(",") ||
                field.contains("\"") ||
                field.contains("\n") ||
                field.contains("\r");

        if (!needsQuoting) {
            return field;
        }

        return "\"" + field.replace("\"", "\"\"") + "\"";
    }

    public static String toLine(String... fields) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {

            if (i > 0) {
                sb.append(",");
            }

            sb.append(escape(fields[i]));
        }

        return sb.toString();
    }
}
