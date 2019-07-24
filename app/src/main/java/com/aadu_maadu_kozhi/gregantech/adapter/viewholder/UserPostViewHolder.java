package com.aadu_maadu_kozhi.gregantech.adapter.viewholder;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.UserPostRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateUserPostBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.Like;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserPost;
import com.aadu_maadu_kozhi.gregantech.util.DateTimeUtils;
import com.aadu_maadu_kozhi.gregantech.util.FireStoreKey;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

import androidx.core.content.ContextCompat;


/**
 * Created by Anand on 8/27/2017.
 */

public class UserPostViewHolder extends BaseViewHolder<UserPost> implements View.OnClickListener {
    private static final int MAX_LINES = 10;
    private UserPostRecyclerAdapterListener<UserPost> listener;
    private InflateUserPostBinding binding;
    private User user;

    public UserPostViewHolder(InflateUserPostBinding itemView, UserPostRecyclerAdapterListener<UserPost> listener) {
        super(itemView.getRoot());
        this.listener = listener;
        binding = itemView;
        binding.setHandlers(this);
        // binding.lbUSerPostLike.setOnClickListener(this);
        bindHolder();
    }

    private void bindHolder() {


    }


    @Override
    void populateData(final UserPost data) {
        binding.tvUserPostContent.setText(data.getPost_content());


        binding.tvUserPostContent.post(new Runnable() {
            @Override
            public void run() {
                // Past the maximum number of lines we want to display.
                if (binding.tvUserPostContent.getLineCount() > MAX_LINES) {
                    try {

                        int lastCharShown = binding.tvUserPostContent.getLayout().getLineVisibleEnd(MAX_LINES - 1);

                        binding.tvUserPostContent.setMaxLines(MAX_LINES);

                        String moreString = itemView.getContext().getString(R.string.view_more);
                        String suffix = "  " + moreString;

                        // 3 is a "magic number" but it's just basically the length of the ellipsis we're going to insert
                        String actionDisplayText = data.getPost_content().substring(0, lastCharShown - suffix.length() - 3) + "..." + suffix;

                        SpannableString truncatedSpannableString = new SpannableString(actionDisplayText);
                        int startIndex = actionDisplayText.indexOf(moreString);
                        truncatedSpannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(itemView.getContext(), android.R.color.holo_blue_dark)),
                                startIndex, startIndex + moreString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        binding.tvUserPostContent.setText(truncatedSpannableString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        /* final ViewTreeObserver vto = binding.tvUserPostContent.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                vto.removeOnGlobalLayoutListener(this);
                if (binding.tvUserPostContent.getLineCount()>10){
                    binding.tvUserPostContent.setText(Html.fromHtml( binding.tvUserPostContent.getText()+"<font color='red'> <u>View More</u></font>"));

                  //  binding.tvReadMore.setVisibility(View.VISIBLE);
                }else {
                    binding.tvReadMore.setVisibility(View.GONE);

                }

            }
        });*/

        //   TextViewUtils.getInstance().makeTextViewResizable(binding.tvUserPostContent,MAX_LINES,"View more",true);
        FirebaseFirestore.getInstance().collection(FireStoreKey.FS_COL_USER).document(data.getUser_id()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot == null) {
                    return;
                } else {
                    try {

                        user = documentSnapshot.toObject(User.class);
                        user.setUser_id(documentSnapshot.getId());
                        if (user.isIs_online()) {
                            binding.vOnline.setVisibility(View.VISIBLE);
                        } else {
                            binding.vOnline.setVisibility(View.GONE);

                        }

                        String url = user.getImage_url() == null ? itemView.getContext().getString(R.string.default_user_profile_img_url) : user.getImage_url();
                        Glide.with(itemView.getContext()).load(url)
                                .apply(RequestOptions.placeholderOf(R.drawable.profile_pic).error(R.drawable.profile_pic).circleCropTransform())
                                .into(binding.ivUserProfile);

                        String name = user.getName() == null ? "New User" : user.getName();
                        binding.tvUserName.setText(name);
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            }
        });
          /*  FirebaseFirestore.getInstance().collection(FireStoreKey.FS_COL_USER).document(data.getUser_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot == null) {
                        return;
                    }
                    User user = documentSnapshot.toObject(User.class);


                    String url = user.getImage_url() == null ? itemView.getContext().getString(R.string.default_user_profile_img_url) : user.getImage_url();
                    Glide.with(itemView.getContext()).load(url)
                            .apply(RequestOptions.placeholderOf(R.drawable.profile_pic).error(R.drawable.profile_pic).circleCropTransform())
                            .into(binding.ivUserProfile);

                    String name = user.getName() == null ? "New User" : user.getName();
                    binding.tvUserName.setText(name);
                    return;
                }
            });*/

        if (data.getUser_id().equalsIgnoreCase(SharedPref.getInstance().getUserId(itemView.getContext()))) {
            binding.ivChat.setVisibility(View.GONE);
            binding.ivTrash.setVisibility(View.VISIBLE);

        } else {
            binding.ivChat.setVisibility(View.VISIBLE);
            if (SharedPref.getInstance().getUserEmailId(itemView.getContext()) != null && SharedPref.getInstance().getUserEmailId(itemView.getContext()).equalsIgnoreCase("s.anandsaravanan@gmail.com")) {
                binding.ivTrash.setVisibility(View.VISIBLE);

            }else {
                binding.ivTrash.setVisibility(View.GONE);

            }

        }


        Date date = data.getPost_created_date_and_time().toDate();
        binding.tvPostTime.setText(DateTimeUtils.getInstance().getTimeAgo(date.getTime()));


        Like like = new Like();
        like.setLiked_user_id(SharedPref.getInstance().getStringValue(itemView.getContext(), itemView.getContext().getString(R.string.user_id)));
        like.setPost_id(data.getId());

        if (data.getPost_liked_list() != null && data.getPost_liked_list().size() > 0 &&
                data.getPost_liked_list().contains(data.getId() + "_" + like.getLiked_user_id())) {
            binding.lbUSerPostLike.setChecked(true);
            binding.lbUSerPostLike.setImageStar();
        } else {
            binding.lbUSerPostLike.setChecked(false);
            binding.lbUSerPostLike.setImageStar();

        }
        if (data.getPost_liked_list() != null && data.getPost_liked_list().size() > 0) {
            binding.tvLikeCount.setText(data.getPost_liked_list().size() + "");
        } else {
            binding.tvLikeCount.setText("");

        }


        if (data.getPost_image_path() != null && data.getPost_image_path().size() > 0) {
            binding.mosaicLayoutOne.mosaicLayout.setVisibility(View.GONE);
            binding.mosaicLayoutTwo.mosaicLayout.setVisibility(View.GONE);
            binding.mosaicLayoutThree.mosaicLayout.setVisibility(View.GONE);
            binding.mosaicLayoutFour.mosaicLayout.setVisibility(View.GONE);
            binding.mosaicLayoutFive.mosaicLayout.setVisibility(View.GONE);
            switch (data.getPost_image_path().size()) {
                case 1:
                    binding.mosaicLayoutOne.mosaicLayout.setVisibility(View.VISIBLE);

                    Glide.with(itemView.getContext()).load(data.getPost_image_path().get(0)).into(binding.mosaicLayoutOne.ivOne);


                    break;
                case 2:
                    binding.mosaicLayoutTwo.mosaicLayout.setVisibility(View.VISIBLE);
                    Glide.with(itemView.getContext()).load(data.getPost_image_path().get(0)).into(binding.mosaicLayoutTwo.ivOne);
                    Glide.with(itemView.getContext()).load(data.getPost_image_path().get(1)).into(binding.mosaicLayoutTwo.ivTwo);


                    break;
                case 3:
                    binding.mosaicLayoutThree.mosaicLayout.setVisibility(View.VISIBLE);
                    Glide.with(itemView.getContext()).load(data.getPost_image_path().get(0)).into(binding.mosaicLayoutThree.ivOne);
                    Glide.with(itemView.getContext()).load(data.getPost_image_path().get(1)).into(binding.mosaicLayoutThree.ivTwo);
                    Glide.with(itemView.getContext()).load(data.getPost_image_path().get(2)).into(binding.mosaicLayoutThree.ivThree);


                    break;
                case 4:
                    binding.mosaicLayoutFour.mosaicLayout.setVisibility(View.VISIBLE);
                    Glide.with(itemView.getContext()).load(data.getPost_image_path().get(0)).into(binding.mosaicLayoutFour.ivOne);
                    Glide.with(itemView.getContext()).load(data.getPost_image_path().get(1)).into(binding.mosaicLayoutFour.ivTwo);
                    Glide.with(itemView.getContext()).load(data.getPost_image_path().get(2)).into(binding.mosaicLayoutFour.ivThree);
                    Glide.with(itemView.getContext()).load(data.getPost_image_path().get(3)).into(binding.mosaicLayoutFour.ivfour);


                    break;
                case 5:
                    binding.mosaicLayoutFive.mosaicLayout.setVisibility(View.VISIBLE);
                    Glide.with(itemView.getContext()).load(data.getPost_image_path().get(0)).into(binding.mosaicLayoutFive.ivOne);
                    Glide.with(itemView.getContext()).load(data.getPost_image_path().get(1)).into(binding.mosaicLayoutFive.ivTwo);
                    Glide.with(itemView.getContext()).load(data.getPost_image_path().get(2)).into(binding.mosaicLayoutFive.ivThree);
                    Glide.with(itemView.getContext()).load(data.getPost_image_path().get(3)).into(binding.mosaicLayoutFive.ivfour);
                    Glide.with(itemView.getContext()).load(data.getPost_image_path().get(4)).into(binding.mosaicLayoutFive.ivfive);


                    break;
            }
               /* MosaicImageAdapter mosaicImageAdapter = new MosaicImageAdapter(itemView.getContext());
                mosaicImageAdapter.setData(data.getPost_image_path());
                // Glide.with(itemView.getContext()).load(data.getPost_image_path().get(0)).into(binding.mosaicLayout);
               *//* if (data.getPost_image_path().size()==1){
                    BlockPattern.BLOCK_PATTERN pattern[] = {
                            BlockPattern.BLOCK_PATTERN.HORIZONTAL,BlockPattern.BLOCK_PATTERN.BIG
                    };
                    binding.mosaicLayout.addPattern(pattern);
                    binding.mosaicLayout.chooseRandomPattern(false);

                }else {*//*
                binding.mosaicLayout.chooseRandomPattern(true);
                *//*}*//*
                binding.mosaicLayout.setAdapter(mosaicImageAdapter);*/
        } else {
            binding.mosaicLayoutOne.mosaicLayout.setVisibility(View.GONE);
            binding.mosaicLayoutTwo.mosaicLayout.setVisibility(View.GONE);
            binding.mosaicLayoutThree.mosaicLayout.setVisibility(View.GONE);
            binding.mosaicLayoutFour.mosaicLayout.setVisibility(View.GONE);
            binding.mosaicLayoutFive.mosaicLayout.setVisibility(View.GONE);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lbUSerPostLike:
                Like like = new Like();
                like.setLiked_user_id(SharedPref.getInstance().getStringValue(itemView.getContext(), itemView.getContext().getString(R.string.user_id)));
                like.setPost_id(data.getId());
                like.setPost_user_id(data.getUser_id());

                if (data.getPost_liked_list() != null && data.getPost_liked_list().size() > 0 && data.getPost_liked_list().contains(data.getId() + "_" + like.getLiked_user_id())) {
                    binding.lbUSerPostLike.performClickOperation();
                    data.getPost_liked_list().remove(data.getId() + "_" + like.getLiked_user_id());
                    listener.onLikeClicked(data, like, false, user);

                } else {
                    if (data.getPost_liked_list() == null) {
                        data.setPost_liked_list(new ArrayList<String>());
                    }
                    data.getPost_liked_list().add(data.getId() + "_" + like.getLiked_user_id());
                    binding.lbUSerPostLike.performClickOperation();
                    listener.onLikeClicked(data, like, true, user);

                }
                binding.tvLikeCount.setText(data.getPost_liked_list().size() > 0 ? (data.getPost_liked_list().size() + "") : "");


                break;
            case R.id.lvUSerPostComment:
                listener.onCommentClicked(data, user);

                break;
            case R.id.ivChat:
                listener.onChatClicked(data, user);

                break;
            case R.id.ivShare:
                listener.onShareClicked(data);

                break;
            case R.id.tvUserPostContent:
                listener.onClickItem(data);

                break;
            case R.id.mosaicLayout:
                listener.onClickItem(data);

                break;
            case R.id.ivTrash:
                listener.onTrashClicked(data,user);

                break;

        }
    }


}
