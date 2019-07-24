package com.aadu_maadu_kozhi.gregantech.util;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.library.ExceptionTracker;
import com.aadu_maadu_kozhi.gregantech.library.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by anand_android on 10/1/2016.
 */
public class CommonUtils {

    private static String TAG = "CommonUtils";
    private static CommonUtils commonUtility = null;

    //Single ton method...
    public static CommonUtils getInstance() {
        if (commonUtility != null) {
            return commonUtility;
        } else {
            commonUtility = new CommonUtils();
            return commonUtility;
        }
    }

    public static void justify(final TextView textView) {

        final AtomicBoolean isJustify = new AtomicBoolean(false);

        final String textString = textView.getText().toString();

        final TextPaint textPaint = textView.getPaint();

        final SpannableStringBuilder builder = new SpannableStringBuilder();

        textView.post(new Runnable() {
            @Override
            public void run() {

                if (!isJustify.get()) {

                    final int lineCount = textView.getLineCount();
                    final int textViewWidth = textView.getWidth();

                    for (int i = 0; i < lineCount; i++) {

                        int lineStart = textView.getLayout().getLineStart(i);
                        int lineEnd = textView.getLayout().getLineEnd(i);

                        String lineString = textString.substring(lineStart, lineEnd);

                        if (i == lineCount - 1) {
                            builder.append(new SpannableString(lineString));
                            break;
                        }

                        String trimSpaceText = lineString.trim();
                        String removeSpaceText = lineString.replaceAll(" ", "");

                        float removeSpaceWidth = textPaint.measureText(removeSpaceText);
                        float spaceCount = trimSpaceText.length() - removeSpaceText.length();

                        float eachSpaceWidth = (textViewWidth - removeSpaceWidth) / spaceCount;

                        SpannableString spannableString = new SpannableString(lineString);
                        for (int j = 0; j < trimSpaceText.length(); j++) {
                            char c = trimSpaceText.charAt(j);
                            if (c == ' ') {
                                Drawable drawable = new ColorDrawable(0x00ffffff);
                                drawable.setBounds(0, 0, (int) eachSpaceWidth, 0);
                                ImageSpan span = new ImageSpan(drawable);
                                spannableString.setSpan(span, j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }

                        builder.append(spannableString);
                    }

                    textView.setText(builder);
                    isJustify.set(true);
                }
            }
        });
    }

    public boolean isAboveMarshmallow() {
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            // Do something for marshmallow and above versions
            return true;
        } else {
            // do something for phones running an SDK before marshmallow
            return false;

        }
    }

    /*public Location getLocation(Context context) {
        GPSTracker gpsTracker = new GPSTracker(context);
        if (gpsTracker.canGetLocation()) {
            return gpsTracker.getLocation();
        } else {
            return null;
        }

    }
    */
    public boolean isAboveLollipop() {
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            // Do something for marshmallow and above versions
            return true;
        } else {
            // do something for phones running an SDK before marshmallow
            return false;

        }
    }

    public boolean hasNetworkConnection(Context context) {
        // TODO Auto-generated method stub

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean valid = false;

        /*NetworkInfo wifiNetwork = cm.getActiveNetworkInfo();
        if (wifiNetwork != null && wifiNetwork.isConnectedOrConnecting()) {
            valid = true;
        }*/

       /* NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnectedOrConnecting()) {
            valid = true;
        }*/

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            valid = true;
        }

        return valid;
    }

    public void hideSoftKeyboard(View view, Context context) {

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void hideSoftKeyboard(AppCompatActivity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        /* InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();
    *//*
         * If no view is focused, an NPE will be thrown
         *
         * Maxim Dmitriev
         *//*
        if (focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }*/
    }

    public void showSoftKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * Check whether the string contains value (OR) not.
     *
     * @param isNotNull string value which has to be checked
     * @return true if the given string is not null and this will validateBaseResponseList if the contains
     * "null" as a String value too
     */

    public boolean isNullCheck(String isNotNull) {
        isNotNull = isNotNull.trim();
        if (isNotNull != null) {
            if (!isNotNull.equalsIgnoreCase("") && isNotNull.length() > 0) {
                if (!isNotNull.equalsIgnoreCase("null")) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Checks whether the arraylist has values (or) not.
     *
     * @param arrayList which has to be checked
     * @return "true" if the given arraylist is not null and has values; otherwise "false".
     */


    public boolean isNullCheck(ArrayList<?> arrayList) {

        if (arrayList != null) {
            if (arrayList.size() > 0 && !arrayList.isEmpty()) {
                return true;
            }
        }

        return false;
    }


    /**
     * Checks whether the list has values (or) not.
     *
     * @param list which has to be checked
     * @return "true" if the given list is not null and has values; otherwise "false".
     */

    public boolean isNullCheck(List<?> list) {

        if (list != null) {
            if (list.size() > 0 && !list.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public String convertDateToString(String timeData) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        try {
            if (timeData != null) {
                Date createdDate = sdf.parse(timeData);


                Date now = sdf.parse(sdf.format(new Date()));
                long diff = createdDate.getTime() - now.getTime();
                long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                System.out.println("Days: " + days);
                if (days <= 0) {
                    return "Today";
                } else if (days <= 1) {
                    return "Yesterday";
                } else {
                    return (days + " days ago");
                }
            } else {
                return timeData;
            }
        } catch (ParseException e) {


            e.printStackTrace();
            return timeData;
        }


    }

 /*   public void setAlarmAfterDriverTimeSet(Context mContext, Calendar mCalender) {

        Log.e(TAG, "setAlarmAfterDriverTimeSet:");

        Intent alarmIntentForDriverStartRide = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pendingIntentForItemAdd = PendingIntent.getBroadcast(mContext, 1, alarmIntentForDriverStartRide, FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mCalender.add(Calendar.MINUTE, -9);//to set 5 min before push
        mCalender.add(Calendar.SECOND, -55);//to set 5 min before push
        manager.set(AlarmManager.RTC_WAKEUP, mCalender.getTimeInMillis(), pendingIntentForItemAdd);
    }*/

    public boolean isAppForground(Context mContext) {

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
                return false;
            }
        }

        return true;
    }


    public String whichActivityVisible(Context mContext) {

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
                return null;
            }
        }
        return am.getRunningTasks(1).get(0).topActivity.getClassName().toString();
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

    public void showAppUpdateDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(context.getString(R.string.app_name_big));
        builder.setMessage("New version" + /*context.getString(R.string.app_name) +*/ " available in google play.");
        builder.setPositiveButton("UPDATE", null);
        try {
            AlertDialog dialog = builder.create();
            dialog.show();
//Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateAppToGooglePlay(context);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    private void navigateAppToGooglePlay(Context context) {

        //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.gregantech.com/Isai/get_app.html")));
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }


    public boolean checkAppVersion(int app_version, Context context) {

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            Log.e(TAG, "Android:" + app_version
                    + "LocalCurrentVersion:" + pInfo.versionCode);
            if (app_version > pInfo.versionCode) {


                return false;

            } else {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            ExceptionTracker.track(e);

        }

        return true;
    }

/*    public void setAlarm(Context context) {
        Log.d(TAG, "setAlarm()");


        boolean isStartAlarmSet = (PendingIntent.getBroadcast(context, 0,
                new Intent(context, AlarmReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);
        boolean isStopAlarmSet = (PendingIntent.getBroadcast(context, 1,
                new Intent(context, AlarmReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (!isStartAlarmSet) {
            Log.d(TAG, "start Alarm is not active");

            setStartAlarm(context);

        }
        if (!isStopAlarmSet) {
            Log.d(TAG, "stop Alarm is not active");
            setStopAlarm(context);

        }

    }

    private void setStopAlarm(Context context) {
        Log.e(TAG,"setStopAlarm");

        Calendar calSet = Calendar.getInstance();
        calSet.set(Calendar.HOUR_OF_DAY, 4);

        calSet.set(Calendar.MINUTE, 55);
        calSet.set(Calendar.SECOND, 0);
        Log.e(TAG, "stop date: " + calSet.toString());


        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("for", "stop");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void setStartAlarm(Context context) {
        Log.e(TAG,"setStartAlarm");
        Calendar calSet = Calendar.getInstance();
        calSet.set(Calendar.HOUR_OF_DAY, 2);

        calSet.set(Calendar.MINUTE, 5);
        calSet.set(Calendar.SECOND, 0);
        Log.e(TAG, "start date: " + calSet.toString());

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("for", "start");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
    }*/


}
