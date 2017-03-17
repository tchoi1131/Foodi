package com.foodi.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tom Wong on 1/31/2017.
 * This class is for the System Configuration
 * It defines global constant values which are used in different classes.
 * It also defines global functions which are used in different classes.
 */

public class SysConfig {
    //Constant Stings to store the Node Name in Firebase database for different data.
    public static final String FBDB_SYS_CONFIGS = "SysConfigs";
    public static final String FBDB_USERS = "Users";
    public static final String FBDB_DELIVERY_REQUESTS = "DeliveryRequests";
    public static final String FBDB_USER_DELIVERY_REQUESTS = "UserDeliveryRequest";
    public static final String FBDB_DELIVERY_REQUEST_USER_OFFER = "DeliveryRequestUserOffer";
    public static final String FBDB_DELIVERY_REQUEST_CONFIRMED_OFFER = "DeliveryRequestConfirmedOffer";
    public static final String FBDB_USER_DELIVERY_OFFER = "UserDeliveryOffer";
    public static final String FBDB_DELIVERY_OFFERS = "DeliveryOffers";

    //Data/Time formats used in the system.
    public static final String DISPLAY_SHORT_DATE_FORMAT = "MMM dd";
    public static final String DISPLAY_TIME_FORMAT = "hh:mm aa";
    public static final String STORED_DATE_FORMAT = "yyyy-MM-dd hh:mm aa";

    /**
     * convert date from "yyyy-MM-dd hh:mm aa" format to "MMM dd" format
     * @param dateTime: the dateTime String in "yyyy-MM-dd hh:mm aa" format
     * @return A Date format in "MMM dd" format
     * @throws ParseException
     */
    public static String getDisplayShortDate(String dateTime) throws ParseException {
        SimpleDateFormat storedDateFormat = new SimpleDateFormat(SysConfig.STORED_DATE_FORMAT);
        SimpleDateFormat displayDateFormat = new SimpleDateFormat(SysConfig.DISPLAY_SHORT_DATE_FORMAT);

        return displayDateFormat.format(storedDateFormat.parse(dateTime));
    }

    /**
     * convert date from "yyyy-MM-dd hh:mm aa" to "hh:mm aa" format
     * @param dateTime: date in "yyyy-MM-dd hh:mm aa" format
     * @return date String in "hh:mm aa" format
     * @throws ParseException
     */
    public static String getDisplayTime(String dateTime) throws ParseException {
        SimpleDateFormat storedDateFormat = new SimpleDateFormat(SysConfig.STORED_DATE_FORMAT);
        SimpleDateFormat displayTimeFormat = new SimpleDateFormat(SysConfig.DISPLAY_TIME_FORMAT);

        return displayTimeFormat.format(storedDateFormat.parse(dateTime));
    }

    /**
     * Convert Date data type to String in "yyyy-MM-dd hh:mm aa"
     * @param dateTime: date in Date data type
     * @return String in "yyyy-MM-dd hh:mm aa"
     * @throws ParseException
     */
    public static String convertToStoredDateTimeFormat(Date dateTime) throws ParseException {
        SimpleDateFormat storedDateFormat = new SimpleDateFormat(SysConfig.STORED_DATE_FORMAT);

        return storedDateFormat.format(dateTime);
    }

    /**
     * Covert Date data type to String in "hh:mm aa"
     * @param dateTime: data in Date data type
     * @return String in "hh:mm aa"
     * @throws ParseException
     */
    public static String convertToDisplayDateTimeFormat(Date dateTime) throws ParseException {
        SimpleDateFormat displayTimeFormat = new SimpleDateFormat(SysConfig.DISPLAY_TIME_FORMAT);

        return displayTimeFormat.format(dateTime);
    }

    /**
     * convert seconds to a string in "9h 9m 9s" format
     * @param second: the number of seconds
     * @return a string in "9h 9m 9s" format
     */
    public static String printDuration(int second){
        int durationSeconds = 0;
        int durationMins = 0;
        int durationHours = 0;
        String outputString = "";

        durationSeconds = second % 60;
        durationMins = second / 60 % 60;
        durationHours = second / 3600;

        if(durationHours > 0){
            outputString += String.valueOf(durationHours) + "h ";
        }

        if(durationMins > 0){
            outputString += String.valueOf(durationMins) + "m ";
        }

        if(durationSeconds > 0 || (durationHours <= 0 && durationMins <= 0)) {
            outputString += String.valueOf(durationSeconds) + "s";
        }

        return outputString;
    }

    public SysConfig(){

    }
}
