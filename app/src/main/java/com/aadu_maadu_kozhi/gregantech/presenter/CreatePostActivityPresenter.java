package com.aadu_maadu_kozhi.gregantech.presenter;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.CreatePostImageAdapter;
import com.aadu_maadu_kozhi.gregantech.adapter.ThagavalKalanchiyamNameAdapter;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.databinding.ActivityCreatePostBinding;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.model.pojo.Area;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ThagavalKalanchiyam;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserPost;
import com.aadu_maadu_kozhi.gregantech.presenter.ipresenter.ICreatePostActivityPresenter;
import com.aadu_maadu_kozhi.gregantech.util.FireStoreKey;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.aadu_maadu_kozhi.gregantech.view.fragment.AreaBottomSheet;
import com.aadu_maadu_kozhi.gregantech.view.fragment.ThagavalKalanchiyamNameBottomSheet;
import com.aadu_maadu_kozhi.gregantech.view.fragment.UserPostCommentBottomSheet;
import com.aadu_maadu_kozhi.gregantech.view.iview.ICreatePostActivityView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;

import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_AREA;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_THAGAVAL_KALANCHIYAM;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_AREA_CREATED_DATE_AND_TIME;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_THAGAVAL_KALANCHIYAM_POST_CREATED_DATE_AND_TIME;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_USER_POST_IMAGE_PATH;

/**
 * Created by Anand on 8/27/2017.
 */

public class CreatePostActivityPresenter extends BasePresenter implements ICreatePostActivityPresenter, View.OnClickListener, BaseRecyclerAdapterListener<String> {
    private ICreatePostActivityView iCreatePostActivityView;
    private StorageReference storageReference;
    private Area area;
    private ThagavalKalanchiyam thagavalKalanchiyam;
    private ArrayList<String> imagePath;
    private ArrayList<String> imageDownloadUrl = new ArrayList<>();
    private CreatePostImageAdapter createPostImageAdapter;
    private int imageUploadPosition = 0;
    private List<Area> areaList = new ArrayList<>();
    private ThagavalKalanchiyamNameAdapter thagavalKalanchiyamAdapter;
    private ActivityCreatePostBinding binding;
    private List<ThagavalKalanchiyam> thagavalKalanchiyamList;

    public CreatePostActivityPresenter(ICreatePostActivityView iCreatePostActivityView, ActivityCreatePostBinding binding) {
        super(iCreatePostActivityView);
        this.iCreatePostActivityView = iCreatePostActivityView;
        this.binding = binding;
        FirebaseStorage storage = FirebaseStorage.getInstance();

        storageReference = storage.getReference();
    }

    @Override
    public void onCreatePresenter(Bundle bundle) {

        if (bundle != null && bundle.containsKey(Constants.BundleKey.For)) {
            Log.e(TAG, bundle.getString(Constants.BundleKey.For));
            switch (bundle.getString(Constants.BundleKey.For)) {

                case Constants.BundleKey.UserPostComment:
                    showUserPostComment((User) bundle.getParcelable(Constants.BundleKey.User), (UserPost) bundle.getParcelable(Constants.BundleKey.USER_POST));
                    break;
                case Constants.BundleKey.UserPostLike:
                    break;
                    case Constants.BundleKey.UserPostCreate:
                    break;

            }
        }


        imagePath = bundle.getStringArrayList(Constants.BundleKey.IMAGE_PATH);
        createAdapter();

        String post_content = bundle.getString(Constants.BundleKey.POST_CONTENT, null);
        if (post_content == null) {
            getAreaFromFireStore();
            getThagavalKalachiyamFromFireStore();

            for (int i = 0; i < imagePath.size(); i++) {
                File f = new File(imagePath.get(i));
                long size = f.length();
                if (size >= 287472) {

                    Log.e(TAG, "Size: " + size);
                    final int finalI = i;
                    new AsyncTask<String, String, String>() {
                        @Override
                        protected String doInBackground(String... strings) {
                            try {
                                imagePath.set(finalI, compressImage(imagePath.get(finalI)));
                                iCreatePostActivityView.getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                createPostImageAdapter.notifyItemChanged(finalI, imagePath.get(finalI));

                                            }
                                        }, 100);
                                    }
                                });
                                return null;


                            } catch (Exception ex) {
                                //  Log.e("Exception:", ex.toString());
                                return null;

                            }
                        }
                    }.execute();
                }
            }

        } else {
            iCreatePostActivityView.setUserPostTitile();
            iCreatePostActivityView.setPostContent(post_content);


        }

    }

    private void showUserPostComment(User user, UserPost userPost) {
        UserPostCommentBottomSheet userPostCommentBottomSheet = new UserPostCommentBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BundleKey.USER_POST_ID, userPost.getId());
        bundle.putParcelable(Constants.BundleKey.USER, user);
        bundle.putParcelable(Constants.BundleKey.USER_POST, userPost);
        userPostCommentBottomSheet.setArguments(bundle);
        userPostCommentBottomSheet.show(iCreatePostActivityView.getActivity().getSupportFragmentManager(), userPostCommentBottomSheet.getTag());

    }

    private void getThagavalKalachiyamFromFireStore() {
        iCreatePostActivityView.getFireStoreDb().collection(FS_COL_THAGAVAL_KALANCHIYAM).orderBy(FS_KEY_THAGAVAL_KALANCHIYAM_POST_CREATED_DATE_AND_TIME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    thagavalKalanchiyamList = task.getResult().toObjects(ThagavalKalanchiyam.class);
                    ThagavalKalanchiyam thagavalKalanchiyam = new ThagavalKalanchiyam();
                    thagavalKalanchiyam.setTitle("மற்றவை");
                    thagavalKalanchiyamList.add(thagavalKalanchiyam);
                } else {

                }

            }
        });

    }

    private void getAreaFromFireStore() {
        iCreatePostActivityView.getFireStoreDb().collection(FS_COL_AREA).orderBy(FS_KEY_AREA_CREATED_DATE_AND_TIME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    areaList = task.getResult().toObjects(Area.class);

                } else {

                }
            }
        });
    }

    private void createAdapter() {
        createPostImageAdapter = new CreatePostImageAdapter(imagePath, this);
        iCreatePostActivityView.setAdapter(createPostImageAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


    @Override
    public void onClickItem(String data) {
        createPostImageAdapter.removeItem(imagePath.indexOf(data));


    }

    @Override
    public void onClickItem(View itemView, String data) {

    }

    @Override
    public void uploadImage(final int imagePosition) {

        if (imagePosition == imagePath.size()) {
            addPostToFireStore();
            return;
        }
        // imageUploadPosition = imagePostion;
        Uri finalbitmapUri = Uri.fromFile(new File(imagePath.get(imagePosition)));
        String path = FS_USER_POST_IMAGE_PATH + "UserId:" + SharedPref.getInstance().getUserId(iCreatePostActivityView.getActivity()) + " Date:" + new Date() + ".jpg";

        final StorageReference riversRef = storageReference.child(path);
        UploadTask uploadTask = riversRef.putFile(finalbitmapUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();

                }

                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imageDownloadUrl.add(downloadUri.toString());
                    uploadImage(imagePosition + 1);

                } else {
                    iCreatePostActivityView.showMessage(iCreatePostActivityView.getActivity().getString(R.string.something_went_wrong));
                    iCreatePostActivityView.getActivity().onBackPressed();
                    // Handle failures
                    // ...
                }
            }
        });
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + ((progress + (imagePosition * 100)) / imagePath.size()) + "% done");
                iCreatePostActivityView.updateProgress(((progress + (imagePosition * 100)) / imagePath.size()));
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        });


    }

    @Override
    public void showAreaBottomSheet() {
        AreaBottomSheet areaBottomSheet = new AreaBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.BundleKey.AREA_LIST, (ArrayList<? extends Parcelable>) areaList);
        areaBottomSheet.setArguments(bundle);
        areaBottomSheet.show(iCreatePostActivityView.getActivity().getSupportFragmentManager(), areaBottomSheet.getTag());
    }

    @Override
    public void setArea(Area area) {
        this.area = area;

    }

    @Override
    public void setThagavalKalanchiyamData(ThagavalKalanchiyam data) {
        thagavalKalanchiyam = data;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showAreaBottomSheet();

            }
        }, 400);
    }

    @Override
    public void showThagavalKalanchiyamBottomSheet() {
        ThagavalKalanchiyamNameBottomSheet thagavalKalanchiyamNameBottomSheet = new ThagavalKalanchiyamNameBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.BundleKey.THAGAVALKALANCHIYAM_LIST, (ArrayList<? extends Parcelable>) thagavalKalanchiyamList);
        thagavalKalanchiyamNameBottomSheet.setArguments(bundle);
        thagavalKalanchiyamNameBottomSheet.show(iCreatePostActivityView.getActivity().getSupportFragmentManager(), thagavalKalanchiyamNameBottomSheet.getTag());
    }

    private void addPostToFireStore() {
        final UserPost userPost = new UserPost();
        userPost.setArea_name(area.getArea_name());
        userPost.setAnimal_type(thagavalKalanchiyam.getTitle());
        userPost.setUser_id(SharedPref.getInstance().getStringValue(iCreatePostActivityView.getActivity(), iCreatePostActivityView.getActivity().getString(R.string.user_id)));
        userPost.setPost_content(iCreatePostActivityView.getPostText());
        userPost.setPost_image_path(imageDownloadUrl);
        iCreatePostActivityView.getFireStoreDb().collection(FireStoreKey.FS_COL_USER_POST).add(userPost).
                addOnSuccessListener(iCreatePostActivityView.getActivity(), new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        iCreatePostActivityView.setUploadSucess(userPost);
                    }
                });
    }


    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = iCreatePostActivityView.getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}

