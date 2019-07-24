package com.aadu_maadu_kozhi.gregantech.view.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.DoctorsListAdapter;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.databinding.FragmentSellerListBinding;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;
import com.aadu_maadu_kozhi.gregantech.util.FireStoreKey;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_AREA;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerListFragment extends BaseFragment implements BaseRecyclerAdapterListener<User> {
    private FragmentSellerListBinding binding;
    private View rootView;


    public SellerListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {

            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller_list, container, false);
            rootView = binding.getRoot();
            rootView.setTag(binding);
        } else {
            binding = (FragmentSellerListBinding) rootView.getTag();
        }
        binding.setHandlers(this); // fragment's context
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorPrimary);
        binding.swipeRefreshLayout.setRefreshing(true);
        setSwipeToRefreshListner();
        // Inflate the layout for this fragment
        return rootView;
    }
    private void setSwipeToRefreshListner() {
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSellersList();
            }
        });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSellersList();

    }
    private void getSellersList() {
        getFireStoreDb().collection(FireStoreKey.FS_COL_USER).whereEqualTo(FireStoreKey.FS_KEY_IS_SELLER_APPROVED, true).orderBy(FS_KEY_AREA, Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                binding.swipeRefreshLayout.setRefreshing(false);

                if (task.isSuccessful()) {
                    List<User> userList = task.getResult().toObjects(User.class);
                    binding.rvSellersList.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.rvSellersList.setItemAnimator(new DefaultItemAnimator());
                    binding.rvSellersList.setAdapter(new DoctorsListAdapter(userList, SellerListFragment.this,true,Constants.BundleKey.ForSellersLogin));
                    if (userList.size() == 0) {
                        binding.tvSellerEmptyMessage.setVisibility(View.VISIBLE);

                    }
                } else {
                    Log.e(TAG, task.getException().toString());
                    binding.tvSellerEmptyMessage.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onClickItem(User data) {

    }

    @Override
    public void onClickItem(View itemView, User data) {

    }
}
