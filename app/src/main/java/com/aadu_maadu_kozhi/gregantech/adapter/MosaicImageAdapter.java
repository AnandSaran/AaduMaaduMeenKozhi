package com.aadu_maadu_kozhi.gregantech.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.aadu_maadu_kozhi.gregantech.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class MosaicImageAdapter extends ArrayAdapter<Object> {
    private Context context;
    private List<String> values;

    public MosaicImageAdapter(Context context) {
        super(context, R.layout.inflate_mosaic_item);
        this.context = context;
    }

    public void setData(List<String> values) {
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.inflate_mosaic_item, parent, false);
        ImageView image = (ImageView) rowView.findViewById(R.id.image);
      Glide.with(context).load(values.get(position)).into(image);

        return rowView;

    }
}