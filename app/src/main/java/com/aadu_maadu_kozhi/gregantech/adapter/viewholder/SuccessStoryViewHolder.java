package com.aadu_maadu_kozhi.gregantech.adapter.viewholder;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.SuccessStoryRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateSuccessStoryBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.Like;
import com.aadu_maadu_kozhi.gregantech.model.pojo.SuccessStory;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;

public class SuccessStoryViewHolder extends BaseViewHolder<SuccessStory> implements View.OnClickListener {
    private static final int MAX_LINES = 10;
    private SuccessStoryRecyclerAdapterListener<SuccessStory> listener;
    private InflateSuccessStoryBinding binding;

    public SuccessStoryViewHolder(InflateSuccessStoryBinding itemView, SuccessStoryRecyclerAdapterListener<SuccessStory> listener) {
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
    void populateData(final SuccessStory data) {
        String url = data.getImageUrl();
        binding.mosaicLayoutOne.mosaicLayout.setVisibility(View.VISIBLE);

        Glide.with(itemView.getContext()).load(url)
                .apply(RequestOptions.placeholderOf(R.drawable.bg).error(R.drawable.logo))
                .into(binding.mosaicLayoutOne.ivOne);

        binding.tvContent.setText(data.getContent());
        binding.tvTitle.setText(data.getTitle());

        binding.tvContent.post(new Runnable() {
            @Override
            public void run() {
                // Past the maximum number of lines we want to display.
                if (binding.tvContent.getLineCount() > MAX_LINES) {
                    try {

                        int lastCharShown = binding.tvContent.getLayout().getLineVisibleEnd(MAX_LINES - 1);

                        binding.tvContent.setMaxLines(MAX_LINES);

                        String moreString = itemView.getContext().getString(R.string.view_more);
                        String suffix = "  " + moreString;

                        // 3 is a "magic number" but it's just basically the length of the ellipsis we're going to insert
                        String actionDisplayText = data.getContent().substring(0, lastCharShown - suffix.length() - 3) + "..." + suffix;

                        SpannableString truncatedSpannableString = new SpannableString(actionDisplayText);
                        int startIndex = actionDisplayText.indexOf(moreString);
                        truncatedSpannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(itemView.getContext(), android.R.color.holo_blue_dark)),
                                startIndex, startIndex + moreString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        binding.tvContent.setText(truncatedSpannableString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
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

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvParent:
                listener.onClickItem(itemView, data);
                break;
            case R.id.tvContent:
                listener.onClickItem(itemView,data);

                break;
                case R.id.tvTitle:
                listener.onClickItem(itemView,data);

                break;
            case R.id.mosaicLayout:
                listener.onClickItem(itemView,data);

                break;
            case R.id.ivShare:
                listener.onShareClicked(data);

                break;
            case R.id.lbUSerPostLike:
                Like like = new Like();
                like.setLiked_user_id(SharedPref.getInstance().getStringValue(itemView.getContext(), itemView.getContext().getString(R.string.user_id)));
                like.setPost_id(data.getId());
                like.setPost_user_id(SharedPref.getInstance().getUserId(itemView.getContext()));

                if (data.getPost_liked_list() != null && data.getPost_liked_list().size() > 0 && data.getPost_liked_list().contains(data.getId() + "_" + like.getLiked_user_id())) {
                    binding.lbUSerPostLike.performClickOperation();
                    data.getPost_liked_list().remove(data.getId() + "_" + like.getLiked_user_id());
                    listener.onLikeClicked(data, like, false);

                } else {
                    if (data.getPost_liked_list() == null) {
                        data.setPost_liked_list(new ArrayList<String>());
                    }
                    data.getPost_liked_list().add(data.getId() + "_" + like.getLiked_user_id());
                    binding.lbUSerPostLike.performClickOperation();
                    listener.onLikeClicked(data, like, true);

                }
                binding.tvLikeCount.setText(data.getPost_liked_list().size() > 0 ? (data.getPost_liked_list().size() + "") : "");


                break;
        }

    }
}
