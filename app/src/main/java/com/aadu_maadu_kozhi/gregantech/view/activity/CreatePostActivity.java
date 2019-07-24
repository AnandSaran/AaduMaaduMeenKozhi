package com.aadu_maadu_kozhi.gregantech.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.CreatePostImageAdapter;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.databinding.ActivityCreatePostBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.Area;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserPost;
import com.aadu_maadu_kozhi.gregantech.presenter.CreatePostActivityPresenter;
import com.aadu_maadu_kozhi.gregantech.presenter.ipresenter.ICreatePostActivityPresenter;
import com.aadu_maadu_kozhi.gregantech.view.iview.ICreatePostActivityView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;


public class CreatePostActivity extends BaseActivity implements ICreatePostActivityView {
    private ActivityCreatePostBinding binding;
    private ICreatePostActivityPresenter iCreatePostActivityPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_post);
        binding.setHandlers(this);

        iCreatePostActivityPresenter = new CreatePostActivityPresenter(this, binding);
        iCreatePostActivityPresenter.onCreatePresenter(getIntent().getExtras());


    }

    @Override
    public void setAdapter(CreatePostImageAdapter createPostImageAdapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rvImage.setLayoutManager(linearLayoutManager);
        binding.rvImage.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        binding.rvImage.addItemDecoration(dividerItemDecoration);
        binding.rvImage.setAdapter(createPostImageAdapter);
    }

    @Override
    public void updateProgress(double v) {
        binding.elasticDownloadView.setProgress((float) v);

    }

    @Override
    public String getPostText() {
        return binding.edtPostContent.getText().toString();
    }

    @Override
    public void setUploadSucess(final UserPost userPost) {
        binding.elasticDownloadView.success();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent returnIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.BundleKey.USER_POST, userPost);
                returnIntent.putExtras(bundle);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }, 2000);
    }

    @Override
    public void setUserPostTitile() {
        binding.tvTitle.setText("Post");
        binding.btnSubmit.setVisibility(View.GONE);
    }

    @Override
    public void setPostContent(String post_content) {
        binding.tvPostContent.setText(post_content);
        //  binding.edtPostContent.setGravity(Gravity.LEFT);
        binding.tvPostContent.setPadding(5, 5, 5, 5);
        binding.edtPostContent.setVisibility(View.GONE);
        binding.tvPostContent.setVisibility(View.VISIBLE);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.tvPostContent.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        } else {
         */  // justify(binding.tvPostContent);

        /* }*/


    }

    @Override
    public void onBackPressed() {
        if (binding.rlProgressLayout.getVisibility() != View.VISIBLE)
            super.onBackPressed();
        // mRevealAnimation.unRevealActivity();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                if (binding.edtPostContent.getText().toString().isEmpty()) {
                    binding.edtPostContent.setError("Enter content");
                } else {
                    iCreatePostActivityPresenter.showThagavalKalanchiyamBottomSheet();

                    /*iCreatePostActivityPresenter.uploadImage(0);
                    binding.btnSubmit.setVisibility(View.GONE);
                    binding.rlProgressLayout.setVisibility(View.VISIBLE);
                    binding.elasticDownloadView.startIntro();*/
                }

                break;
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    public void uploadData(Area area) {
        iCreatePostActivityPresenter.setArea(area);
        iCreatePostActivityPresenter.uploadImage(0);
        binding.btnSubmit.setVisibility(View.GONE);
        binding.toolbar.setVisibility(View.GONE);
        binding.rlProgressLayout.setVisibility(View.VISIBLE);
        binding.elasticDownloadView.startIntro();
    }

    public ICreatePostActivityPresenter getPresenter() {
        return iCreatePostActivityPresenter;
    }

  /*  public void setThagavalKalanchiyamData(ThagavalKalanchiyam data) {
       iCreatePostActivityPresenter.setThagavalKalanchiyamData(data);
    }*/
}
