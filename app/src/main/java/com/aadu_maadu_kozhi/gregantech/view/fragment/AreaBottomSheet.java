package com.aadu_maadu_kozhi.gregantech.view.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.AreaBottomSheetAdapter;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.databinding.BottomSheetAreaBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.Area;
import com.aadu_maadu_kozhi.gregantech.view.activity.CreatePostActivity;

import java.util.List;


public class AreaBottomSheet extends BottomSheetDialogFragment implements BaseRecyclerAdapterListener<Area> {
    private BottomSheetAreaBinding binding;
    private View rootView;

    public AreaBottomSheet() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {

            binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_area, container, false);
            rootView = binding.getRoot();
            rootView.setTag(binding);
        } else {
            binding = (BottomSheetAreaBinding) rootView.getTag();
        }
        binding.setHandlers(this);
        List<Area> areaList = getArguments().getParcelableArrayList(Constants.BundleKey.AREA_LIST);
        setAdapter(areaList);// fragment's context
        // Inflate the layout for this fragment
        return rootView;


    }

    private void setAdapter(List<Area> areaList) {
        AreaBottomSheetAdapter areaBottomSheetAdapter = new AreaBottomSheetAdapter(areaList, this);
        binding.rvArea.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvArea.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //  RecyclerViewMargin decoration = new RecyclerViewMargin(20, 1);
        //  binding.rvArea.addItemDecoration(decoration);
        binding.rvArea.setItemAnimator(new DefaultItemAnimator());
        binding.rvArea.setAdapter(areaBottomSheetAdapter);


    }

    @Override
    public void onClickItem(Area data) {
        ((CreatePostActivity) this.getActivity()).uploadData(data);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();

            }
        },200);

    }

    @Override
    public void onClickItem(View itemView, Area data) {

    }
}