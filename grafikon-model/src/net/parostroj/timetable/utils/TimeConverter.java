package net.parostroj.timetable.utils;

import java.util.Formatter;

/**
 * Utility class for converting time from text to int and backwards.
 * 
 * @author jub
 */
public class TimeConverter {

    /**
     * converts from seconds to textual representation. The string contains
     * two positions for hours.
     *
     * @param time time in seconds
     * @return textual representation
     */
    public static String convertFromIntToTextWS(int time) {
        int hours = getHours(time);
        int minutes = getMinutes(time);
        Formatter formatter = new Formatter();
        formatter.format("%1$2d:%2$02d", hours, minutes);
        return formatter.toString();
    }
    
    /**
     * converts from seconds to textual representation. The string contains
     * two positions for hours.
     *
     * @param time time in seconds
     * @param delimiter delimiter
     * @return textual representation
     */
    public static String convertFromIntToTextWS(int time,String delimiter) {
        int hours = getHours(time);
        int minutes = getMinutes(time);
        Formatter formatter = new Formatter();
        String format = "%1$2d" + delimiter + "%2$02d";
        formatter.format(format, hours, minutes);
        return formatter.toString();
    }

    /**
     * converts from seconds to textual representation.
     *
     * @param time time in seconds
     * @return textual representation
     */
    public static String convertFromIntToText(int time) {
        int hours = getHours(time);
        int minutes = getMinutes(time);
        Formatter formatter = new Formatter();
        formatter.format("%1$d:%2$02d", hours, minutes);
        return formatter.toString();
    }

    /**
     * converts from seconds to textual representation.
     *
     * @param time time in seconds
     * @param delimiter delimiter
     * @return textual representation
     */
    public static String convertFromIntToText(int time, String delimiter) {
        int hours = getHours(time);
        int minutes = getMinutes(time);
        Formatter formatter = new Formatter();
        String format = "%1$d" + delimiter + "%2$02d";
        formatter.format(format, hours, minutes);
        return formatter.toString();
    }
    
    /**
     * returns minutes.
     * 
     * @param time time in seconds
     * @return minutes
     */
    public static int getMinutes(int time) {
        return (time % 3600) / 60;
    }
    
    /**
     * returns hours.
     * 
     * @param time time in seconds
     * @return hours
     */
    public static int getHours(int time) {
        return time / 3600;
    }
    
    /**
     * returns string with hours.
     * 
     * @param time time in seconds
     * @return string
     */
    public static String convertHoursToText(int time) {
        return Integer.toString(getHours(time));
    }

    /**
     * returns string with minutes.
     * 
     * @param time time in seconds
     * @return string
     */
    public static String convertMinutesToText(int time) {
        Formatter formatter = new Formatter();
        formatter.format("%1$02d", getMinutes(time));
        return formatter.toString();
    }

    /**
     * returns string with minutes.
     * 
     * @param time time in seconds
     * @return string
     */
    public static String convertAllMinutesToText(int time) {
        return Integer.toString(time / 60);
    }

    /**
     * converts from seconds to textual representation.
     *
     * @param time time in seconds
     * @param delimiter1 delimiter
     * @param delimiter2 delimiter
     * @return textual representation
     */
    public static String convertHoursAndMinutesToText(int time, String delimiter1, String delimiter2) {
        int hours = getHours(time);
        int minutes = getMinutes(time);
        Formatter formatter = new Formatter();
        String format = "%1$d " + delimiter1;
        if (minutes != 0)
            format = format + " %2$d " + delimiter2;
        formatter.format(format, hours, minutes);
        return formatter.toString();
    }

    /**
     * returns the last digit from minutes (needed by GT).
     * 
     * @param time time in seconds
     * @return last digit of minutes
     */
    public static String getLastDigitOfMinutes(int time) {
        int minutes = getMinutes(time);
        String minutesStr = Integer.toString(minutes);
        int lastDigitIndex = minutesStr.length() - 1;
        return minutesStr.substring(lastDigitIndex, lastDigitIndex + 1);
    }
    
    /**
     * converts from textual representation to seconds. It returns <code>-1</code>
     * if there was problem with converting the value.
     * 
     * @param text textual representation
     * @return time in seconds from midnight
     */
    public static int convertFromTextToInt(String text) {
        // remove spaces
        text = text.trim();
        
        int size = 0;
        int[] values = new int[4];
        
        // get digits
        for (char ch : text.toCharArray()) {
            if (Character.isDigit(ch)) {
                values[size++] = Character.digit(ch, 10);
                if (size == 4)
                    break;
            }
        }
        
        // convert to time
        switch (size) {
            case 1:
                return values[0] * 3600;
            case 2:
                return (values[0] * 10 + values[1]) * 3600;
            case 3:
                return (values[0] * 3600) + (values[1] * 10 + values[2]) * 60;
            case 4:
                return (values[0] * 10 + values[1]) * 3600 + (values[2] * 10 + values[3]) * 60;
            default:
                return -1;
        }
    }
}
