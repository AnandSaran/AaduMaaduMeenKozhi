package com.aadu_maadu_kozhi.gregantech.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Base64;
import android.view.inputmethod.InputMethodManager;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.common.BaseProject;
import com.aadu_maadu_kozhi.gregantech.library.ExceptionTracker;
import com.aadu_maadu_kozhi.gregantech.library.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;



/**
 * Created by Guru Karthi on 06/12/16.
 */
public class CodeSnippet {

    //Time AM or PM
    private String PM = "PM";
    private String AM = "AM";
    private String TAG = getClass().getSimpleName();
    private Context mContext;

    public CodeSnippet(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Checking the internet connectivity
     *
     * @return true if the connection is available otherwise false
     */
    public boolean hasNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        return false;
    }





    /**
     * Check which type of connection the device is connected to
     */
    public String whichNetworkConnection() {

        ConnectivityManager cm = BaseProject.getInstance().getConnectivityManager();

        NetworkInfo wifiNetwork = cm.getActiveNetworkInfo();
        if (wifiNetwork != null && wifiNetwork.isConnectedOrConnecting()) {
            return mContext.getString(R.string.network_wifi);
        }

        NetworkInfo mobileNetwork = cm.getActiveNetworkInfo();
        if (mobileNetwork != null && mobileNetwork.isConnectedOrConnecting()) {
            return mContext.getString(R.string.network_mobile);
        }

        return null;
    }

    public String getNetworkType() {
        ConnectivityManager cm = BaseProject.getInstance().getConnectivityManager();
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork && activeNetwork.isConnectedOrConnecting()) {
            return activeNetwork.getTypeName();
        }
        return null;
    }
    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            ExceptionTracker.track(e);
            Log.e(TAG, "printHashKey()" + e);
        } catch (Exception e) {
            ExceptionTracker.track(e);

            Log.e(TAG, "printHashKey()" + e);
        }
    }
    public boolean isTodayLieInBetween(String str1, String str2) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String todayStr = formatter.format(Calendar.getInstance().getTime());
            Date todayDate = formatter.parse(todayStr);
            Date date1 = formatter.parse(str1);
            Date date2 = formatter.parse(str2);

            return date1.compareTo(todayDate) <= 0 && date2.compareTo(todayDate) >= 0;
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        return false;
    }

    public Calendar getCalendarTime(String time) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formatter.parse(time));
            return calendar;
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        return null;
    }

    public String getCalendarTime(Calendar time) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            return formatter.format(time);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        return null;
    }

    public Calendar getCalendarForYear(String time) {
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formatter.parse(time));
            return calendar;
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        return null;
    }

    public Calendar getCalendarToStandard(String time) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formatter.parse(time));
            return calendar;
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        return null;
    }

    public Calendar getCalendarWithTimeOnly(String time) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formatter.parse(time));
            return calendar;
        } catch (Exception e) {
            ExceptionTracker.track(e);
            SimpleDateFormat formatter = new SimpleDateFormat("hh : mm aa", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(formatter.parse(time));
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            return calendar;
        }
    }

    public String getDayOfMonthMonthAndYear(Calendar calendar) {
        //TODO mention
        String dateString = "";
        dateString = calendar.get(Calendar.DAY_OF_MONTH) + " " + monthNameFromInt(calendar.get(Calendar.MONTH)) + ", " + calendar.get(Calendar.YEAR);
        return dateString;
    }

    public String getDayOfMonthMonthAndYearStd(Calendar calendar) {
        String dateString = "";
        int month = calendar.get(Calendar.MONTH) + 1;
        DecimalFormat formatter = new DecimalFormat("00");
        dateString = formatter.format(calendar.get(Calendar.DAY_OF_MONTH)) + "-" + formatter.format(month) + "-" + calendar.get(Calendar.YEAR);
        return dateString;
    }

    private String monthNameFromInt(int monthInt) {
        String month = "";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (monthInt >= 0 && monthInt <= 11) {
            month = months[monthInt];
        }
        return month.substring(0, 4);
    }

    public String getPastTimerString(Calendar calendar) {
        long time = System.currentTimeMillis() - calendar.getTimeInMillis();
        long mins = time / 60000;
        if (mins > 59L) {
            long hours = mins / 60;
            if (hours > 24) {
                long days = hours / 24;
                if (days > 1) {
                    return days + " days";
                } else {
                    return days + " day";
                }
            } else {
                return hours + " hours ago";
            }
        } else {
            return "less than a minute";
        }
    }

    public Date getTimeFromMilisecond(long timeStamp) {
        return new Date((long) timeStamp * 1000);
    }

    public String getOrdinaryTime(Time time) {
        if (time.hour > 12) {
            return formatTime(time.hour - 12) + ":" + formatTime(time.minute)
                    + " " + getAMorPM(time);
        }

        return (time.hour == 0 ? String.valueOf(12) : formatTime(time.hour)) + ":"
                + formatTime(time.minute) + " " + getAMorPM(time);
    }

    public String getOrdinaryDate(Calendar calendar) {

        int month = calendar.get(Calendar.MONTH) + 1;
        DecimalFormat formatter = new DecimalFormat("00");
        Log.d(TAG, "getOrdinaryDate : " + formatter.format(month));
        return formatter.format(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + formatter.format(month) + "/" + calendar.get(Calendar.YEAR);
    }

    public String getOrdinaryTime(Calendar calendar) {

        int min = calendar.get(Calendar.MINUTE);
        //Log.d(TAG,"hours :"+calendar.get(Calendar.HOUR_OF_DAY));
        String meridian = "AM";
        if (calendar.get(Calendar.HOUR_OF_DAY) > 11) {
            meridian = "PM";
        }
        DecimalFormat formatter = new DecimalFormat("00");
        return formatter.format(calendar.get(Calendar.HOUR)) + ":" + formatter.format(min) + " " + meridian;
    }

    public String getOrdinaryDateWithFipe(Time date) {

        return date.monthDay + " | " + date.month + " | " + date.year;
    }

    private String formatTime(int time) {
        if (String.valueOf(time).length() < 2)
            return "0" + time;
        else
            return String.valueOf(time);
    }

    private String getAMorPM(Time time) {
        if (time.hour > 11) {
            return PM;
        } else
            return AM;
    }





    private Intent getSettingsIntent(String settings) {
        return new Intent(settings);
    }

    private void startActivityBySettings(Context context, String settings) {
        context.startActivity(getSettingsIntent(settings));
    }

    private void startActivityBySettings(Context context, Intent intent) {
        context.startActivity(intent);
    }

    public void showGpsSettings(Context context) {
        startActivityBySettings(context, Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    }

    public void showNetworkSettings() {
        Intent chooserIntent = Intent.createChooser(getSettingsIntent(Settings.ACTION_DATA_ROAMING_SETTINGS),
                "Complete action using");
        List<Intent> networkIntents = new ArrayList<>();
        networkIntents.add(getSettingsIntent(Settings.ACTION_WIFI_SETTINGS));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, networkIntents.toArray(new Parcelable[]{}));
        startActivityBySettings(mContext, chooserIntent);
    }

    public boolean isSpecifiedDelay(long exisingTime, long specifiedDelay) {
        return specifiedDelay >= (Calendar.getInstance().getTimeInMillis() - exisingTime);
    }

    public void hideKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }

    public void showKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.showSoftInputFromInputMethod(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }

    public boolean isNull(Object object) {
        return null == object || object.toString().compareTo("null") == 0;
    }

    public final boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean isAboveMarshmallow() {
        int currentapiVersion = Build.VERSION.SDK_INT;
        // Do something for marshmallow and above versions
// do something for phones running an SDK before marshmallow
        return currentapiVersion >= Build.VERSION_CODES.M;
    }

    public boolean isAboveLollipop() {
        int currentapiVersion = Build.VERSION.SDK_INT;
        // Do something for marshmallow and above versions
// do something for phones running an SDK before marshmallow
        return currentapiVersion >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Fetch the drawable object for the given resource id.
     *
     * @param resourceId to which the value is to be fetched.
     * @return drawable object for the given resource id.
     */

    public Drawable getDrawable(int resourceId) {
        return ResourcesCompat.getDrawable(mContext.getResources(), resourceId, null);
    }

    /**
     * Returns the current date.
     *
     * @return Current date
     */

    public Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * Fetch the string value from a xml file returns the value.
     *
     * @param resId to which the value has to be fetched.
     * @return String value of the given resource id.
     */

    public String getString(int resId) {
        return mContext.getResources().getString(resId);
    }

    /**
     * Fetch the color value from a xml file returns the value.
     *
     * @param colorId to which the value has to be fetched.
     * @return Integer value of the given resource id.
     */

    public int getColor(int colorId) {
        return ContextCompat.getColor(mContext, colorId);
    }


    private interface OnGooglePlayServiceListener {
        void onInstallingService();

        void onCancelServiceInstallation();
    }

}