package com.aadu_maadu_kozhi.gregantech.view.fragment;


import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.customview.GRadioGroup;
import com.aadu_maadu_kozhi.gregantech.databinding.FragmentHomeBinding;
import com.aadu_maadu_kozhi.gregantech.presenter.HomeFragmentPresenter;
import com.aadu_maadu_kozhi.gregantech.presenter.ipresenter.IHomeFragmentPresenter;
import com.aadu_maadu_kozhi.gregantech.view.activity.HomeActivity;
import com.aadu_maadu_kozhi.gregantech.view.iview.IHomeFragmentView;


/**
 */
public class HomeFragment extends BaseFragment implements IHomeFragmentView, GRadioGroup.INavigationRadioButtonClick  {
    private View rootView;
    private FragmentHomeBinding binding;
    private IHomeFragmentPresenter iHomeFragmentPresenter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
            rootView = binding.getRoot();
            rootView.setTag(binding);
        } else {
            binding = (FragmentHomeBinding) rootView.getTag();
        }
        binding.setHandlers(this); // fragment's context
        iHomeFragmentPresenter = new HomeFragmentPresenter(this, binding);
        iHomeFragmentPresenter.onCreatePresenter(getArguments());
        GRadioGroup gr = new GRadioGroup(this,
                binding.rbGhot, binding.rbFish, binding.rbChicken);
        return rootView;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabCreatePost:
                ((HomeActivity) getActivity()).picImage();
                break;
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iHomeFragmentPresenter.getUSerPost(null);

    }

    public void getPostByAreaName(String areaName) {
        binding.swipeRefreshLayout.setRefreshing(true);
        iHomeFragmentPresenter.getUSerPost(areaName);

    }
    @Override
    public void onRadioButtonSelect(int id) {

    }
}
