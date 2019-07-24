package com.aadu_maadu_kozhi.gregantech.adapter.viewholder;

import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ThagavalKalanchiyam;


/**
 * Created by Anand on 4/1/2017.
 */

public class TypeListCheckBoxViewHolder extends BaseViewHolder<ThagavalKalanchiyam> implements View.OnClickListener {
    boolean checkAll_flag = false;
    boolean checkItem_flag = false;
    private BaseRecyclerAdapterListener<ThagavalKalanchiyam> listener;
    private CheckBox txtName;
    private SparseBooleanArray mCheckedItems;

    public TypeListCheckBoxViewHolder(View itemView, BaseRecyclerAdapterListener<ThagavalKalanchiyam> listener) {
        super(itemView);
        this.listener = listener;
        mCheckedItems = new SparseBooleanArray();
        bindHolder();

    }

    private void bindHolder() {
        txtName = (CheckBox) itemView.findViewById(R.id.name);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    void populateData(final ThagavalKalanchiyam data) {
        txtName.setText(data.getTitle());

        txtName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                data.setSelected(buttonView.isChecked()); // Set the value of checkbox to maintain its state.

            }
        });
        txtName.setTag(getAdapterPosition()); // This line is important.

        //  mCheckedItems.put(getAdapterPosition(),data.isSelected());
        txtName.setChecked(data.isSelected());


    }


}
