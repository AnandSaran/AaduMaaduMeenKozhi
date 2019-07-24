package com.aadu_maadu_kozhi.gregantech.customview;

import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aadu_maadu_kozhi.gregantech.R;

import java.util.ArrayList;
import java.util.List;


public class GRadioGroup {
    private INavigationRadioButtonClick iNavigationRadioButtonClick;
    List<RadioButton> radios = new ArrayList<RadioButton>();
    View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            // let's deselect all radios in group
            for (RadioButton rb : radios) {
                if (rb.isChecked()) {
                    scalDownView(((ViewGroup) rb.getParent()));
                }
                ViewParent p = rb.getParent();
                if (p.getClass().equals(RadioGroup.class)) {
                    // if RadioButton belongs to RadioGroup,
                    // then deselect all radios in it
                    RadioGroup rg = (RadioGroup) p;
                    rg.clearCheck();

                    ((ViewGroup) rb.getParent()).setBackground(ContextCompat.getDrawable(rb.getContext(), R.drawable.bg_circle_rb_un_select));

                } else {
                    // if RadioButton DOES NOT belong to RadioGroup,
                    // just deselect it
                    rb.setChecked(false);
                    float scalingFactor = 1f; // scale down to half the size
                    ((ViewGroup) rb.getParent()).setScaleX(scalingFactor);
                    ((ViewGroup) rb.getParent()).setScaleY(scalingFactor);
                    ((ViewGroup) rb.getParent()).setBackground(ContextCompat.getDrawable(rb.getContext(), R.drawable.bg_circle_rb_un_select));
                }
            }

            // now let's select currently clicked RadioButton
            if (v.getClass().equals(AppCompatRadioButton.class)) {
                RadioButton rb = (RadioButton) v;
                rb.setChecked(true);
                scalUpView(((ViewGroup) rb.getParent()));
                ((ViewGroup) rb.getParent()).setBackground(ContextCompat.getDrawable(rb.getContext(), R.drawable.bg_circle_rb_select));
                iNavigationRadioButtonClick.onRadioButtonSelect(rb.getId());

            }

        }
    };

    /**
     * Constructor, which allows you to pass number of RadioButton instances,
     * making a group.
     *
     * @param radios One RadioButton or more.
     */
    public GRadioGroup(INavigationRadioButtonClick iNavigationRadioButtonClick,RadioButton... radios) {
        super();
       this. iNavigationRadioButtonClick=iNavigationRadioButtonClick;
        for (RadioButton rb : radios) {
            this.radios.add(rb);
            rb.setOnClickListener(onClick);
        }
    }

   /* public GRadioGroup(View activity, int... radiosIDs) {
        super();

        for (int radioButtonID : radiosIDs) {
            RadioButton rb = (RadioButton) activity.findViewById(radioButtonID);
            if (rb != null) {
                this.radios.add(rb);
                rb.setOnClickListener(onClick);
            }
        }
    }*/

    private void scalUpView(ViewGroup parent) {

        ScaleAnimation fade_in =  new ScaleAnimation(1f, 1.2f, 1f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(500);     // animation duration in milliseconds
        fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
        parent.startAnimation(fade_in);
    }

    private void scalDownView(ViewGroup parent) {

        ScaleAnimation fade_in =  new ScaleAnimation(1.2f, 1f, 1.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(500);     // animation duration in milliseconds
        fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
        parent.startAnimation(fade_in);
    }
    public interface INavigationRadioButtonClick {
         void onRadioButtonSelect(int id);
    }
}
