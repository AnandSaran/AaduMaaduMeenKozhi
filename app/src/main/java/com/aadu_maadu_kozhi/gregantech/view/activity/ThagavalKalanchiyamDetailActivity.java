package com.aadu_maadu_kozhi.gregantech.view.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.databinding.ActivityThagavalKalanchiyamDetailBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.Accessories;
import com.aadu_maadu_kozhi.gregantech.model.pojo.SuccessStory;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ThagavalKalanchiyam;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;


public class ThagavalKalanchiyamDetailActivity extends AppCompatActivity {
    ViewGroup parent;
    private ActivityThagavalKalanchiyamDetailBinding binding;
    private String title = "";
    private String content = "";
    private String image_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_thagaval_kalanchiyam_detail);
        binding.setHandlers(this);
        setUpAdd();
        setUpToolbar();

        if (getIntent().getExtras().containsKey(Constants.BundleKey.THAGAVALKALANCHIYAM_DATA)) {
            ThagavalKalanchiyam thagavalKalanchiyamHeading = getIntent().getExtras().getParcelable(Constants.BundleKey.THAGAVALKALANCHIYAM_DATA);
            title = thagavalKalanchiyamHeading.getTitle();
            image_url = thagavalKalanchiyamHeading.getImageUrl();
            content = thagavalKalanchiyamHeading.getContent();
        } else if (getIntent().getExtras().containsKey(Constants.BundleKey.SUCCESS_STORY_DATA)) {
            SuccessStory successStory = getIntent().getExtras().getParcelable(Constants.BundleKey.SUCCESS_STORY_DATA);
            title = successStory.getTitle();
            image_url = successStory.getImageUrl();
            content = successStory.getContent();

        }
        else if (getIntent().getExtras().containsKey(Constants.BundleKey.ACCESSORIES_DATA)) {
            Accessories accessories = getIntent().getExtras().getParcelable(Constants.BundleKey.ACCESSORIES_DATA);
            title = accessories.getTitle();
            image_url = accessories.getImageUrl();
            content = accessories.getContent();

        }
        binding.tvTitle.setText(title);
        binding.tvContent.setText(content);
        parent = (ViewGroup) binding.image.getParent();
        Glide.with(this)
                .asBitmap()
                .load(image_url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                        if (Build.VERSION.SDK_INT >= 16) {
                            parent.setBackground(new BitmapDrawable(getResources(), com.aadu_maadu_kozhi.gregantech.util.Constants.fastblur(Bitmap.createScaledBitmap(resource, 50, 50, true))));// ));
                        }
                        binding.image.setImageBitmap(resource);
                    }


                });
      /*  Glide.with(this)
                .asBitmap()

                .load(thagavalKalanchiyamHeading.getImageUrl())
                .into(new BitmapImageViewTarget(binding.image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        // Do bitmap magic here
                        super.setResource(resource);


                    }
                });*/


    }

    private void setUpAdd() {

        AdRequest adRequest = new AdRequest.Builder()
                //   .addTestDevice("821E8BF14772CBA5E715CA4AE95A189F")
                .build();
        binding.adView.loadAd(adRequest);
        binding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                binding.adView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setUpToolbar() {
        final Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout c = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.app_bar_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tool.setTitle("");
        }
        setSupportActionBar(tool);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_white_24dp);
        c.setTitleEnabled(false);

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isVisible = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    tool.setTitle(getString(R.string.app_name));
                    isVisible = true;
                } else if (isVisible) {
                    tool.setTitle("");
                    isVisible = false;
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
