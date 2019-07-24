package com.aadu_maadu_kozhi.gregantech.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.adapter.viewholder.DoctorsListViewHolder;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateDoctorListBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;

import java.util.List;

import androidx.databinding.DataBindingUtil;


/**
 * Created by Anand on 8/27/2017.
 */

public class DoctorsListAdapter extends BaseRecyclerAdapter<User, DoctorsListViewHolder> {
    private BaseRecyclerAdapterListener<User> listener;
private boolean isToShowType;
private String viewFor;
    public DoctorsListAdapter(List<User> data, BaseRecyclerAdapterListener<User> listener, boolean isToShowType,String viewFor) {
        super(data);
        this.listener = listener;
        this.isToShowType = isToShowType;
        this.viewFor = viewFor;

    }

    @Override
    public DoctorsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InflateDoctorListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.inflate_doctor_list, parent, false);
        return new DoctorsListViewHolder(binding, listener,isToShowType,viewFor);

    }
}
