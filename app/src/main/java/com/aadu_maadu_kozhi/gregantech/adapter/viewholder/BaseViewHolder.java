package com.aadu_maadu_kozhi.gregantech.adapter.viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;



public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder{

    public T data;
    String TAG = getClass().getSimpleName();

    BaseViewHolder(View itemView) {
        super(itemView);
    }

    public void setData(T data) {
        this.data = data;
        populateData(data);
    }

    abstract void populateData(T data);
}
