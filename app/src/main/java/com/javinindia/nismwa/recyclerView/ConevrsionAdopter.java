package com.javinindia.nismwa.recyclerView;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javinindia.nismwa.R;
import com.javinindia.nismwa.font.FontRobotoCondensedRegularSingleTonClass;
import com.javinindia.nismwa.modelClasses.convrsionparsing.ConversionDetail;
import com.javinindia.nismwa.modelClasses.usefullInfoparsing.UsefullMasterDetail;
import com.javinindia.nismwa.utility.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashis on 9/14/2017.
 */

public class ConevrsionAdopter extends RecyclerView.Adapter<ConevrsionAdopter.ViewHolder> {

    List<ConversionDetail> list;
    Context context;
    MyClickListener myClickListener;
    ArrayList<ConversionDetail> shopCategoryListArrayList;

    public ConevrsionAdopter(Context context) {
        this.context = context;
    }

    public void setData(List<ConversionDetail> list) {
        this.list = list;
        this.shopCategoryListArrayList = new ArrayList<>();
        this.shopCategoryListArrayList.addAll(list);
    }

    public List<ConversionDetail> getData() {
        return list;
    }

    @Override
    public ConevrsionAdopter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conversion_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        String cityName = "";
        final ConversionDetail categoryDetail = (ConversionDetail) list.get(position);
        if (!TextUtils.isEmpty(categoryDetail.getConversion().trim())) {
            cityName = categoryDetail.getConversion().trim();
            viewHolder.txtItem.setText(Utility.fromHtml(cityName));
        }
        if (!TextUtils.isEmpty(categoryDetail.getMultiply().trim())) {
            String multiple = categoryDetail.getMultiply().trim();
            viewHolder.txtItemValue.setText(Utility.fromHtml(multiple));
        }


        viewHolder.txtItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickListener.onItemUseClick(position, categoryDetail);
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtItem, txtItemValue;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtItem = itemLayoutView.findViewById(R.id.txtItem);
            txtItem.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
            txtItemValue = itemLayoutView.findViewById(R.id.txtItemValue);
            txtItemValue.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {
        void onItemUseClick(int position, ConversionDetail model);

    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

}