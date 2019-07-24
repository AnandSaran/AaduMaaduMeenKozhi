package com.aadu_maadu_kozhi.gregantech.retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by anand_android on 9/28/2016.
 */
public class RetroFitUtils {
    //Single ton object...
    private static RetroFitUtils RetroFitUtility = null;
    private final String TAG = "RetroFitUtils";

    //Single ton method...
    public static RetroFitUtils getInstance() {
        if (RetroFitUtility != null) {
            return RetroFitUtility;
        } else {
            RetroFitUtility = new RetroFitUtils();
            return RetroFitUtility;
        }
    }




    public boolean isConnectingToInternet(Context _context) {

        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }



    public String getStringFromByte(InputStream inputStream) {

        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();

        reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        String result = sb.toString();
        return result;
    }
}
