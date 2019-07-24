package com.aadu_maadu_kozhi.gregantech.util;

import java.util.Calendar;

public class DateTimeUtils {
    private static String TAG = DateTimeUtils.class.getSimpleName();
    private static DateTimeUtils dateTimeUtils = null;

    //Single ton method...
    public static DateTimeUtils getInstance() {
        if (dateTimeUtils != null) {
            return dateTimeUtils;
        } else {
            dateTimeUtils = new DateTimeUtils();
            return dateTimeUtils;
        }
    }

    public String getTimeAgo(long time_ago) {
        time_ago=time_ago/1000;
        long cur_time = (Calendar.getInstance().getTimeInMillis())/1000 ;
        long time_elapsed = cur_time - time_ago;
        long seconds = time_elapsed;
        // Seconds
        if (seconds <= 60) {
            return "Just now";
        }
        //Minutes
        else{
            int minutes = Math.round(time_elapsed / 60);

            if (minutes <= 60) {
                if (minutes == 1) {
                    return "a min ago";
                } else {
                    return minutes + " min's ago";
                }
            }
            //Hours
            else {
                int hours = Math.round(time_elapsed / 3600);
                if (hours <= 24) {
                    if (hours == 1) {
                        return "An hr ago";
                    } else {
                        return hours + " hrs ago";
                    }
                }
                //Days
                else {
                    int days = Math.round(time_elapsed / 86400);
                    if (days <= 7) {
                        if (days == 1) {
                            return "Yesterday";
                        } else {
                            return days + " days ago";
                        }
                    }
                    //Weeks
                    else {
                        int weeks = Math.round(time_elapsed / 604800);
                        if (weeks <= 4.3) {
                            if (weeks == 1) {
                                return "1 week ago";
                            } else {
                                return weeks + " weeks ago";
                            }
                        }
                        //Months
                        else {
                            int months = Math.round(time_elapsed / 2600640);
                            if (months <= 12) {
                                if (months == 1) {
                                    return "1 month ago";
                                } else {
                                    return months + " months ago";
                                }
                            }
                            //Years
                            else {
                                int years = Math.round(time_elapsed / 31207680);
                                if (years == 1) {
                                    return "1 yr ago";
                                } else {
                                    return years + " yr's ago";
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}


