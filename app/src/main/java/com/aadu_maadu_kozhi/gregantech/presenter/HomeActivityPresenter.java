package com.aadu_maadu_kozhi.gregantech.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.model.pojo.Area;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserCreatePostFCM;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserPost;
import com.aadu_maadu_kozhi.gregantech.model.webservice.FcmServerModel;
import com.aadu_maadu_kozhi.gregantech.presenter.ipresenter.IHomeActivityPresenter;
import com.aadu_maadu_kozhi.gregantech.retrofit.ResponseListener;
import com.aadu_maadu_kozhi.gregantech.util.FireStoreKey;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.aadu_maadu_kozhi.gregantech.view.activity.CreatePostActivity;
import com.aadu_maadu_kozhi.gregantech.view.iview.IHomeActivityView;
import com.bluelinelabs.logansquare.LoganSquare;
import com.fxn.pix.Pix;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

import static com.aadu_maadu_kozhi.gregantech.common.Constants.RequestCodes.REQUEST_CODE_CREATE_POST;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_AREA;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_USER;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_AREA_CREATED_DATE_AND_TIME;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_USER_IS_ONLINE;

/**
 * Created by Anand on 8/27/2017.
 */

public class HomeActivityPresenter extends BasePresenter implements IHomeActivityPresenter, View.OnClickListener, ResponseListener {
    private IHomeActivityView iHomeActivityView;
    private List<Area> areaList = new ArrayList<>();

    private List<String> strings = new ArrayList<>();
    private String tokens[];

    public HomeActivityPresenter(IHomeActivityView iHomeActivityView) {
        super(iHomeActivityView);
        this.iHomeActivityView = iHomeActivityView;
        getUserCount();

        //  addAreaList();
        // Crashlytics.getInstance().crash();
    }

    private void getUserCount() {
        iHomeActivityView.getFireStoreDb().collection(FS_COL_USER).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (SharedPref.getInstance().getUserEmailId(iHomeActivityView.getActivity()) != null && SharedPref.getInstance().getUserEmailId(iHomeActivityView.getActivity()).equalsIgnoreCase("s.anandsaravanan@gmail.com")) {
                        iHomeActivityView.showInfoMessage(String.valueOf(task.getResult().size()));
                    }
                    for (DocumentSnapshot documentSnapshot :
                            task.getResult().getDocuments()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null && user.getToken() != null && !SharedPref.getInstance().getUserId(iHomeActivityView.getActivity()).equalsIgnoreCase(documentSnapshot.getId())) {
                            strings.add(user.getToken());
                        }
                    }
                   /* if (strings != null && strings.size() > 0) {
                        tokens = strings.toArray(new String[strings.size()]);
                    }
                    Log.e(TAG, "Size: " + strings.size());*/
                }
            }
        });
    }

    private void addAreaList() {
        List<Area> areas = new ArrayList<>();
        areas.add(new Area("அரியலூர் மாவட்டம்"));
        areas.add(new Area("இராமநாதபுரம் மாவட்டம்"));
        areas.add(new Area("ஈரோடு மாவட்டம்"));
        areas.add(new Area("கடலூர் மாவட்டம்"));
        areas.add(new Area("கரூர் மாவட்டம்"));
        areas.add(new Area("கரூர் மாவட்டம்"));
        areas.add(new Area("கன்னியாகுமரி மாவட்டம்"));
        areas.add(new Area("காஞ்சிபுரம் மாவட்டம்"));
        areas.add(new Area("கிருஷ்ணகிரி மாவட்டம்"));
        areas.add(new Area("கோயம்புத்தூர் மாவட்டம்"));
        areas.add(new Area("சிவகங்கை மாவட்டம்"));
        areas.add(new Area("சென்னை மாவட்டம்"));
        areas.add(new Area("சேலம் மாவட்டம்"));
        areas.add(new Area("தஞ்சாவூர் மாவட்டம்"));
        areas.add(new Area("தர்மபுரி மாவட்டம்"));
        areas.add(new Area("திண்டுக்கல் மாவட்டம்"));
        areas.add(new Area("திருச்சிராப்பள்ளி மாவட்டம்"));
        areas.add(new Area("திருநெல்வேலி மாவட்டம்"));
        areas.add(new Area("திருப்பூர் மாவட்டம்"));
        areas.add(new Area("திருவண்ணாமலை மாவட்டம்"));
        areas.add(new Area("திருவள்ளூர் மாவட்டம்"));
        areas.add(new Area("திருவாரூர் மாவட்டம்"));
        areas.add(new Area("தூத்துக்குடி மாவட்டம்"));
        areas.add(new Area("நாகப்பட்டினம் மாவட்டம்"));
        areas.add(new Area("நாமக்கல் மாவட்டம்"));
        areas.add(new Area("நீலகிரி மாவட்டம்"));
        areas.add(new Area("புதுக்கோட்டை மாவட்டம்"));
        areas.add(new Area("பெரம்பலூர் மாவட்டம்"));
        areas.add(new Area("மதுரை மாவட்டம்"));
        areas.add(new Area("விருதுநகர் மாவட்டம்"));
        areas.add(new Area("விழுப்புரம் மாவட்டம்"));
        areas.add(new Area("வேலூர் மாவட்டம்"));
        areas.add(new Area("கொழும்பு மாவட்டம்"));
        areas.add(new Area("கம்பஹா மாவட்டம்"));
        areas.add(new Area("களுத்துறை மாவட்டம்"));
        areas.add(new Area("காலி மாவட்டம்"));
        areas.add(new Area("மாத்தறை மாவட்டம்"));
        areas.add(new Area("அம்பாந்தோட்டை மாவட்டம்"));
        areas.add(new Area("இரத்தினபுரி மாவட்டம்"));
        areas.add(new Area("மொனராகலை மாவட்டம்"));
        areas.add(new Area("அம்பாறை மாவட்டம்"));
        areas.add(new Area("பதுளை மாவட்டம்"));
        areas.add(new Area("நுவரெலியா மாவட்டம்"));
        areas.add(new Area("கண்டி மாவட்டம்"));
        areas.add(new Area("மாத்தளை மாவட்டம்"));
        areas.add(new Area("மட்டக்களப்பு மாவட்டம்"));
        areas.add(new Area("திருகோணமலை மாவட்டம்"));
        areas.add(new Area("அநுராதபுரம் மாவட்டம்"));
        areas.add(new Area("பொலநறுவை மாவட்டம்"));
        areas.add(new Area("வவுனியா மாவட்டம்"));
        areas.add(new Area("குருநாகல் மாவட்டம்"));
        areas.add(new Area("புத்தளம் மாவட்டம்"));
        areas.add(new Area("முல்லைத்தீவு மாவட்டம்"));
        areas.add(new Area("யாழ்ப்பாணம் மாவட்டம்"));
        areas.add(new Area("மன்னார் மாவட்டம்"));
        areas.add(new Area("கிளிநொச்சி மாவட்டம்"));
        areas.add(new Area("கேகாலை மாவட்டம்"));
        areas.add(new Area("மற்றவை"));

        for (Area area :
                areas) {
            iHomeActivityView.getFireStoreDb().collection(FS_COL_AREA).add(area);

        }
    }

    @Override
    public void onStartPresenter() {
        super.onStartPresenter();
        updateUserOnline(true);

    }

    @Override
    public void onDestroyPresenter() {
        super.onDestroyPresenter();
        updateUserOnline(false);

    }

    public void updateUserOnline(boolean isOnline) {
        try {
            iHomeActivityView.getFireStoreDb().collection(FireStoreKey.FS_COL_USER).document(SharedPref.getInstance().getUserId(iHomeActivityView.getActivity())).update(FS_KEY_USER_IS_ONLINE, isOnline);

        } catch (Exception e) {
        }
    }

    @Override
    public void onCreatePresenter(Bundle bundle) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void picImage() {
        Pix.start(iHomeActivityView.getActivity(), Constants.RequestCodes.REQUEST_CODE_PIC_IMAGE, Constants.Common.NUMBER_OF_IMAGES_TO_SELECT);
    }

    @Override
    public void getAreaFromFireStore() {
        Area area = new Area();
        area.setArea_name(iHomeActivityView.getActivity().getString(R.string.all_area));
        areaList.add(area);
        iHomeActivityView.getFireStoreDb().collection(FS_COL_AREA).orderBy(FS_KEY_AREA_CREATED_DATE_AND_TIME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    areaList.addAll(task.getResult().toObjects(Area.class));
                    ArrayAdapter<Area> areaSpinnerAdapter = new ArrayAdapter<Area>(iHomeActivityView.getActivity(), R.layout.inflate_spinner_area, areaList);

                    // Drop down layout style - list view with radio button
                    areaSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    iHomeActivityView.setAreaSpinnerAdapter(areaSpinnerAdapter);
                } else {

                }
            }
        });
    }

    @Override
    public String getAreaName(int position) {
        if (position == 0) {
            return null;
        } else {
            return areaList.get(position).toString();
        }
    }


    @Override
    public void onActivityResultPresenter(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.RequestCodes.REQUEST_CODE_PIC_IMAGE) {
            ArrayList<String> imagePath = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Intent intent = new Intent(iHomeActivityView.getActivity(), CreatePostActivity.class);
            intent.putStringArrayListExtra(Constants.BundleKey.IMAGE_PATH, imagePath);
            iHomeActivityView.getActivity().startActivityForResult(intent, REQUEST_CODE_CREATE_POST);

        } else if (resultCode == Activity.RESULT_OK && requestCode == Constants.RequestCodes.REQUEST_CODE_CREATE_POST) {
            Bundle bundle = data.getExtras();
            if (bundle != null && bundle.containsKey(Constants.BundleKey.USER_POST)) {
                try {
                final UserPost userPost = bundle.getParcelable(Constants.BundleKey.USER_POST);
                UserCreatePostFCM userCreatePostFCM = new UserCreatePostFCM();
                userCreatePostFCM.setForr(Constants.NotificationKey.UserPostCreate);
                userCreatePostFCM.setUserName(SharedPref.getInstance().getUserName(iHomeActivityView.getActivity()));
                String jsonString = null/*.replace(",", ",\n").replace("{", "{\n").replace("}", "}\n")*/;

                    jsonString = LoganSquare.serialize(userPost);


                userCreatePostFCM.setUserPost(jsonString);
                userCreatePostFCM.setTokens(strings);
                FcmServerModel fcmServerModel=new FcmServerModel(HomeActivityPresenter.this);
                fcmServerModel.sendUserCreatePost(0,userCreatePostFCM);
                } catch (IOException e) {
                    e.printStackTrace();
                }

               /* new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            String jsonString = LoganSquare.serialize(userPost)*//*.replace(",", ",\n").replace("{", "{\n").replace("}", "}\n")*//*;

                            Map<String, Object> newCall = new HashMap<>();
                            newCall.put(Constants.NotificationKey.UserPost, jsonString);
                            newCall.put(Constants.NotificationKey.UserName, SharedPref.getInstance().getUserName(iHomeActivityView.getActivity()));
                            newCall.put(Constants.NotificationKey.For, Constants.NotificationKey.UserPostCreate);

                         //   iHomeActivityView.sendMultiplePushNotification(strings, newCall);



                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                }).start();*/
            }
            iHomeActivityView.refreshUserPost(iHomeActivityView.getAreaSpinnerSelectedPostion());
            iHomeActivityView.showInterstitialAdds();
            iHomeActivityView.sendCreatePostNotification();

        }
    }

    @Override
    public void onSuccess(Object mResponse, long flag) {

    }

    @Override
    public void onFailureApi(Throwable mThrowable) {

    }

    @Override
    public void showErrorDialog(Object mResponse, long flag) {

    }
}
