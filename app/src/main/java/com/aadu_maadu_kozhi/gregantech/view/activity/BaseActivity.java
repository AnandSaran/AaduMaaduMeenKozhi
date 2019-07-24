package com.aadu_maadu_kozhi.gregantech.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;

import com.aadu_maadu_kozhi.gregantech.R;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aadu_maadu_kozhi.gregantech.library.ExceptionTracker;
import com.aadu_maadu_kozhi.gregantech.presenter.ipresenter.IPresenter;
import com.aadu_maadu_kozhi.gregantech.util.CodeSnippet;
import com.aadu_maadu_kozhi.gregantech.util.CommonUtils;
import com.aadu_maadu_kozhi.gregantech.view.iview.IView;
import com.aadu_maadu_kozhi.gregantech.view.widget.CustomProgressbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;


abstract class BaseActivity extends AppCompatActivity implements IView {

    protected String TAG = getClass().getSimpleName();
    protected View mParentView;
    protected CodeSnippet mCodeSnippet;
    private IPresenter iPresenter;
    private CustomProgressbar mCustomProgressbar;
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        mParentView = getWindow().getDecorView().findViewById(android.R.id.content);
        return super.onCreateView(name, context, attrs);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (iPresenter != null) iPresenter.onStartPresenter();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (iPresenter != null) iPresenter.onStopPresenter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (iPresenter != null) iPresenter.onPausePresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (iPresenter != null) iPresenter.onResumePresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iPresenter != null) iPresenter.onDestroyPresenter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (iPresenter != null) iPresenter.onActivityResultPresenter(requestCode, resultCode, data);
    }

    public void bindPresenter(IPresenter iPresenter) {
        this.iPresenter = iPresenter;
    }

    private CustomProgressbar getProgressBar() {
        if (mCustomProgressbar == null) {
            mCustomProgressbar = new CustomProgressbar(this);
        }
        return mCustomProgressbar;
    }

    @Override
    public void showMessage(String message) {
      //  Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Toasty.success(this, message, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void showErrorMessage(String message) {
        Toasty.error(this, message, Toast.LENGTH_SHORT, true).show();

    }
 @Override
    public void showInfoMessage(String message) {
        Toasty.info(this, message, Toast.LENGTH_SHORT, true).show();

    }

    @Override
    public void showMessage(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showProgressbar() {
        // TODO have to menu_view_question_preference the custom progressbar
        getProgressBar().show();
    }

    @Override
    public void dismissProgressbar() {
        // TODO dismiss the progressbar
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    getProgressBar().dismissProgress();
                } catch (Exception e) {
                    ExceptionTracker.track(e);
                }
            }
        });
    }

    @Override
    public FragmentActivity getActivity() {
        return BaseActivity.this;
    }

    @Override
    public void showSnackBar(String message) {
        if (mParentView != null) {
            Snackbar snackbar = Snackbar.make(mParentView, message, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
    }

    @Override
    public void showSnackBar(@NonNull View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    @Override
    public void showNetworkMessage() {
        if (mParentView != null) {
            Snackbar snackbar = Snackbar.make(mParentView, "No Network found!", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.RED);
           /* snackbar.setAction(R.string.action_settings, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCodeSnippet.showNetworkSettings();
                }
            });*/
            snackbar.show();
        }
    }

    @Override
    public CodeSnippet getCodeSnippet() {
        if (mCodeSnippet == null) {
            mCodeSnippet = new CodeSnippet(getActivity());
            return mCodeSnippet;
        }
        return mCodeSnippet;
    }

    @Override
    public boolean checkNetWork() {
        if (CommonUtils.getInstance().hasNetworkConnection(this)) {
            return true;
        } else {
            dismissProgressbar();

            showNetworkMessage();
            //  showMessage(AppConstant.NO_NETWORK);
            return false;
        }
    }

    @Override
    public FirebaseFirestore getFireStoreDb() {
        return db;
    }
    @Override
    public String sendPushNotification(String to, Map<String, Object> newCall) {
        try {

            final String apiKey = getString(R.string.FCM_API_KEY);
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=" + apiKey);
            conn.setDoOutput(true);
            JSONObject message = new JSONObject();
            message.put("to", to);
            message.put("priority", "high");
            /*message.put("time", "0.2");*/

            JSONObject notification = new JSONObject(newCall);
            // notification.put("title", title);
            //notification.put(Constants.NotificationKey.FOR, Constants.NotificationKey.VIDEO_CALL);

            message.put("data", notification);
            OutputStream os = conn.getOutputStream();
            os.write(message.toString().getBytes());
            os.flush();
            os.close();
            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + message.toString());
            System.out.println("Response Code : " + responseCode);
            System.out.println("Response Code : " + conn.getResponseMessage());

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
            return response.toString();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
        return "error";
    }
    public String sendMultiplePushNotification(List<String> to, Map<String, Object> newCall) {
        try {

            final String apiKey = getString(R.string.FCM_API_KEY);
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=" + apiKey);
            conn.setDoOutput(true);
            JSONObject message = new JSONObject();
            message.put("registration_ids", to.toString());
            message.put("priority", "high");
            /*message.put("time", "0.2");*/

            JSONObject notification = new JSONObject(newCall);
            // notification.put("title", title);
            //notification.put(Constants.NotificationKey.FOR, Constants.NotificationKey.VIDEO_CALL);

            message.put("data", notification);
            OutputStream os = conn.getOutputStream();
            os.write(message.toString().getBytes());
            os.flush();
            os.close();
            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + message.toString());
            System.out.println("Response Code : " + responseCode);
            System.out.println("Response Code : " + conn.getResponseMessage());

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
            return response.toString();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
        return "error";
    }


}
