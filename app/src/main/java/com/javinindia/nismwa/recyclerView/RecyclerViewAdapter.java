package com.javinindia.nismwa.recyclerView;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.javinindia.nismwa.R;
import com.javinindia.nismwa.modelClasses.DataModel;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayList mValues;
    Context mContext;
    protected ItemListener mListener;

    public RecyclerViewAdapter(Context context, ArrayList values, ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener = itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        DataModel item;

        public ViewHolder(View v) {

            super(v);

            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.textView);

        }

        public void setData(DataModel item) {
            this.item = item;

            textView.setText(item.text);

        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.setData((DataModel) mValues.get(position));
    }


  /*  @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));

    }*/

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(DataModel item);
    }
}