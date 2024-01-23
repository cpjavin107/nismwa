package com.javinindia.nismwa.recyclerView;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.javinindia.nismwa.R;
import com.javinindia.nismwa.font.FontRobotoCondensedBoldSingleTonClass;
import com.javinindia.nismwa.font.FontRobotoCondensedRegularSingleTonClass;
import com.javinindia.nismwa.modelClasses.calenderparsing.CalenderDetail;
import com.javinindia.nismwa.modelClasses.usefullInfoparsing.UsefullMasterDetail;
import com.javinindia.nismwa.utility.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashish on 17-08-2017.
 */

public class CalenderAdopter extends RecyclerView.Adapter<CalenderAdopter.ViewHolder> {

    List<CalenderDetail> list;
    Context context;
    MyClickListener myClickListener;
    ArrayList<CalenderDetail> shopCategoryListArrayList;

    public CalenderAdopter(Context context) {
        this.context = context;
    }

    public void setData(List<CalenderDetail> list) {
        this.list = list;
        this.shopCategoryListArrayList = new ArrayList<>();
        this.shopCategoryListArrayList.addAll(list);
    }

    public List<CalenderDetail> getData() {
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final CalenderDetail categoryDetail = (CalenderDetail) list.get(position);
        String cityName = "";
        if (!TextUtils.isEmpty(categoryDetail.getDate().trim())) {
            cityName = categoryDetail.getDate().trim();
            viewHolder.txtEventName.setText(cityName);
        }

        if (!TextUtils.isEmpty(categoryDetail.getDay().trim())) {
            String day = categoryDetail.getDay().trim();
            viewHolder.txtEventName.setText(cityName + " (" + day + ")");
        }
        if (!TextUtils.isEmpty(categoryDetail.getOccasion().trim())) {
            String dis = categoryDetail.getOccasion().trim();
            viewHolder.txtDisc.setText(Utility.fromHtml(dis));
        }

        viewHolder.rlEventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickListener.onItemCalenderClick(position, categoryDetail);
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView txtEventName, txtDateTime, txtDisc, txtAddress;

        public RelativeLayout rlEventItem;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtEventName = itemLayoutView.findViewById(R.id.txtEventName);
            txtEventName.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(context).getTypeFace());
            txtDateTime = itemLayoutView.findViewById(R.id.txtDateTime);
            txtDateTime.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
            txtDisc = itemLayoutView.findViewById(R.id.txtDisc);
            txtDisc.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
            txtAddress = itemLayoutView.findViewById(R.id.txtAddress);
            txtAddress.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
            rlEventItem = itemLayoutView.findViewById(R.id.rlEventItem);
            txtDateTime.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {
        void onItemCalenderClick(int position, CalenderDetail model);

    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

}