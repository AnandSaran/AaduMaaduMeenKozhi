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
import com.aadu_maadu_kozhi.gregantech.adapter.ThagavalKalanchiyamNameAdapter;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.databinding.BottomSheetThagvalKalanchiyamNameBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ThagavalKalanchiyam;
import com.aadu_maadu_kozhi.gregantech.view.activity.CreatePostActivity;

import java.util.List;


public class ThagavalKalanchiyamNameBottomSheet extends BottomSheetDialogFragment implements BaseRecyclerAdapterListener<ThagavalKalanchiyam> {
    private BottomSheetThagvalKalanchiyamNameBinding binding;
    private View rootView;

    public ThagavalKalanchiyamNameBottomSheet() {
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

            binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_thagval_kalanchiyam_name, container, false);
            rootView = binding.getRoot();
            rootView.setTag(binding);
        } else {
            binding = (BottomSheetThagvalKalanchiyamNameBinding) rootView.getTag();
        }
        //   binding.setHandlers(this);
        List<ThagavalKalanchiyam> areaList = getArguments().getParcelableArrayList(Constants.BundleKey.THAGAVALKALANCHIYAM_LIST);
        setAdapter(areaList);
        return rootView;


    }

    private void setAdapter(List<ThagavalKalanchiyam> thagavalKalanchiyamList) {
        ThagavalKalanchiyamNameAdapter areaBottomSheetAdapter = new ThagavalKalanchiyamNameAdapter(thagavalKalanchiyamList, this);
        binding.rvThagavalKalanchiyam.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvThagavalKalanchiyam.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //  RecyclerViewMargin decoration = new RecyclerViewMargin(20, 1);
        //  binding.rvArea.addItemDecoration(decoration);
        binding.rvThagavalKalanchiyam.setItemAnimator(new DefaultItemAnimator());
        binding.rvThagavalKalanchiyam.setAdapter(areaBottomSheetAdapter);


    }

    @Override
    public void onClickItem(ThagavalKalanchiyam data) {
        ((CreatePostActivity) this.getActivity()).getPresenter().setThagavalKalanchiyamData(data);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();

            }
        }, 200);

    }

    @Override
    public void onClickItem(View itemView, ThagavalKalanchiyam data) {

    }
}