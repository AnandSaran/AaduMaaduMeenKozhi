package com.aadu_maadu_kozhi.gregantech.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ChatMessage;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserPost;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserPostComment;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.aadu_maadu_kozhi.gregantech.view.activity.CreatePostActivity;
import com.aadu_maadu_kozhi.gregantech.view.activity.HomeActivity;
import com.bluelinelabs.logansquare.LoganSquare;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_USER;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_TOKEN;


/**
 * Created by anand_android on 11/4/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private static final String CHANNEL_ID_POST_COMMENTS = "1";
    private static final String CHANNEL_ID_POST_LIKES = "2";
    private static final String CHANNEL_ID_CHAT = "3";
    private static final String CHANNEL_ID_USER_POST = "4";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        createNotificationChannel();

        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "data: " + remoteMessage.getData().toString());

        JSONObject jsonObject = new JSONObject(remoteMessage.getData());
        Log.e(TAG, "json::" + jsonObject.toString());
        //   sendNotification(jsonObject);
        if (remoteMessage.getData().size() > 0) {

            handleNotification(remoteMessage.getData());
        }


    }

    private void updateFCMToken() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(FS_COL_USER).document(SharedPref.getInstance()
                .getStringValue(getApplicationContext(), getString(R.string.user_id))).update(FS_KEY_TOKEN, SharedPref.getInstance().getStringValue(this, getString(R.string.fcm_token)));
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        String refreshedToken = token;
        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        SharedPref.getInstance()
                .setSharedValue(getApplicationContext(), getString(R.string.fcm_token), refreshedToken);
        if (SharedPref.getInstance()
                .getStringValue(getApplicationContext(), getString(R.string.user_id)) != null) {
            updateFCMToken();
        }
    }

    private void handleNotification(Map<String, String> data) {
        JSONObject jsonObject = new JSONObject(data);
        Intent intent = null;
        Bundle bundle = null;

        switch (jsonObject.optString(Constants.NotificationKey.For)) {
            case Constants.NotificationKey.UserPostComment:
                try {
                    UserPostComment userPostComment = LoganSquare.parse(jsonObject.optString(Constants.NotificationKey.UserPostComment), UserPostComment.class);
                    UserPost userPost = LoganSquare.parse(jsonObject.optString(Constants.NotificationKey.UserPost), UserPost.class);
                    User user = LoganSquare.parse(jsonObject.optString(Constants.NotificationKey.User), User.class);

                    intent = new Intent(this, CreatePostActivity.class);
                    bundle = new Bundle();
                    bundle.putParcelable(Constants.BundleKey.UserPostComment, userPostComment);
                    bundle.putParcelable(Constants.BundleKey.USER_POST, userPost);
                    bundle.putParcelable(Constants.BundleKey.USER, user);
                    bundle.putString(Constants.BundleKey.For, Constants.BundleKey.UserPostComment);


                    //for displaying
                    ArrayList<String> imagePath = new ArrayList<>(userPost.getPost_image_path());
                    intent.putStringArrayListExtra(Constants.BundleKey.IMAGE_PATH, imagePath);
                    intent.putExtra(Constants.BundleKey.POST_CONTENT, userPost.getPost_content());


                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
                    createNotification("Comments",
                            userPostComment.getCommented_user_name() + " Commented on your post\n" + userPostComment.getComments(), R.drawable.ic_comment_trans, pendingIntent, CHANNEL_ID_POST_COMMENTS);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case Constants.NotificationKey.UserPostCreate:
                try {
                    UserPost userPost = LoganSquare.parse(jsonObject.optString(Constants.NotificationKey.UserPost), UserPost.class);
                    String userName = jsonObject.optString(Constants.NotificationKey.UserName);

                    //  User user = LoganSquare.parse(jsonObject.optString(Constants.NotificationKey.User), User.class);
                    intent = new Intent(this, CreatePostActivity.class);
                    bundle = new Bundle();
                    bundle.putParcelable(Constants.BundleKey.USER_POST, userPost);
                    //   bundle.putParcelable(Constants.BundleKey.USER, user);
                    bundle.putString(Constants.BundleKey.For, Constants.BundleKey.UserPostCreate);
                    //for displaying
                    ArrayList<String> imagePath = new ArrayList<>(userPost.getPost_image_path());
                    intent.putStringArrayListExtra(Constants.BundleKey.IMAGE_PATH, imagePath);
                    intent.putExtra(Constants.BundleKey.POST_CONTENT, userPost.getPost_content());
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
                    createNotification(this.getString(R.string.news_feed),
                            userName + " Created post\n", R.drawable.ic_newspaper_white, pendingIntent, CHANNEL_ID_USER_POST);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case Constants.NotificationKey.UserPostLike:
                try {
                    UserPost userPost = LoganSquare.parse(jsonObject.optString(Constants.NotificationKey.UserPostLike), UserPost.class);
                    intent = new Intent(this, CreatePostActivity.class);
                    bundle = new Bundle();
                    bundle.putParcelable(Constants.BundleKey.USER_POST, userPost);
                    bundle.putString(Constants.BundleKey.For, Constants.BundleKey.UserPostLike);


                    //for displaying
                    ArrayList<String> imagePath = new ArrayList<>(userPost.getPost_image_path());
                    intent.putStringArrayListExtra(Constants.BundleKey.IMAGE_PATH, imagePath);
                    intent.putExtra(Constants.BundleKey.POST_CONTENT, userPost.getPost_content());


                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

                    createNotification("Likes",
                            jsonObject.optString(Constants.NotificationKey.LikedUserName) + " liked your post", R.drawable.ic_star_yellow_24dp, pendingIntent,/*pendingIntent*/CHANNEL_ID_POST_LIKES);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case Constants.NotificationKey.UserChat:
                try {
                    ChatMessage chatMessage = LoganSquare.parse(jsonObject.optString(Constants.NotificationKey.UserChat), ChatMessage.class);
                    User user = LoganSquare.parse(jsonObject.optString(Constants.NotificationKey.User), User.class);
                    intent = new Intent(this, HomeActivity.class);
                    bundle = new Bundle();
                    bundle.putParcelable(Constants.BundleKey.UserChat, chatMessage);
                    bundle.putParcelable(Constants.BundleKey.USER, user);
                    bundle.putString(Constants.BundleKey.For, Constants.BundleKey.UserChat);
                    intent.putExtras(bundle);
                    Log.e(TAG, intent.getStringExtra(Constants.BundleKey.For));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent1 = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
                    createNotification("Chat",
                            chatMessage.getSenderName() + " Messaged you\n" + chatMessage.getMessage(), R.drawable.ic_chat_black_24dp, pendingIntent1, CHANNEL_ID_CHAT);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

        }
    }

    private void createNotification(String title, String content, int icon, PendingIntent pendingIntent, String channelId) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createUserPostCommentChannel();
            createUserPostLikeChannel();
            createUserChatChannel();
            createUserPostChannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createUserChatChannel() {
        CharSequence name = getString(R.string.notification_channel_user_chat);
        String description = getString(R.string.notification_channel_description_user_chat);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID_CHAT, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createUserPostChannel() {
        CharSequence name = getString(R.string.notification_channel_user_post);
        String description = getString(R.string.notification_channel_description_user_post);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID_USER_POST, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createUserPostLikeChannel() {
        CharSequence name = getString(R.string.notification_channel_post_likes);
        String description = getString(R.string.notification_channel_description_post_likes);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID_POST_LIKES, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createUserPostCommentChannel() {
        CharSequence name = getString(R.string.notification_channel_post_comments);
        String description = getString(R.string.notification_channel_description_post_comments);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID_POST_COMMENTS, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
