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
import com.javinindia.nismwa.modelClasses.bloodparsing.BloodGroupDetail;
import com.javinindia.nismwa.modelClasses.usefullInfoparsing.UsefullMasterDetail;
import com.javinindia.nismwa.utility.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashis on 3/27/2018.
 */

public class BloodGroupAdopter extends RecyclerView.Adapter<BloodGroupAdopter.ViewHolder> {

    List<BloodGroupDetail> list;
    Context context;
    MyClickListener myClickListener;
    ArrayList<BloodGroupDetail> shopCategoryListArrayList;

    public BloodGroupAdopter(Context context) {
        this.context = context;
    }

    public void setData(List<BloodGroupDetail> list) {
        this.list = list;
        this.shopCategoryListArrayList = new ArrayList<>();
        this.shopCategoryListArrayList.addAll(list);
    }

    public List<BloodGroupDetail> getData() {
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coustom_dialog_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final BloodGroupDetail categoryDetail = (BloodGroupDetail) list.get(position);
        if (!TextUtils.isEmpty(categoryDetail.getGroup_name().trim())) {
            String cityName = categoryDetail.getGroup_name().trim();
            viewHolder.txtItem.setText(Utility.fromHtml(cityName));
        }

        viewHolder.txtItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickListener.onItemUseClick(position, categoryDetail);
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtItem;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtItem = itemLayoutView.findViewById(R.id.txtItem);
            txtItem.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {
        void onItemUseClick(int position, BloodGroupDetail model);

    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

}