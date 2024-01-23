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
import com.javinindia.nismwa.modelClasses.countryStateCityparsing.CityDetail;
import com.javinindia.nismwa.utility.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashish on 10-10-2016.
 */
public class CityDialogAdopter extends RecyclerView.Adapter<CityDialogAdopter.ViewHolder> {

    List<CityDetail> list;
    Context context;
    MyClickListener myClickListener;
    ArrayList<CityDetail> shopCategoryListArrayList;

    public CityDialogAdopter(Context context) {
        this.context = context;
    }

    public void setData(List<CityDetail> list) {
        this.list = list;
        this.shopCategoryListArrayList = new ArrayList<>();
        this.shopCategoryListArrayList.addAll(list);
    }

    public List<CityDetail> getData() {
        return list;
    }

    @Override
    public CityDialogAdopter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coustom_dialog_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final CityDetail cityDetail = (CityDetail) list.get(position);
        if (!TextUtils.isEmpty(cityDetail.getCityName().trim())) {
            String cityName = cityDetail.getCityName().trim();
            viewHolder.txtItem.setText(Utility.fromHtml(cityName));
        }

        viewHolder.txtItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickListener.onItemClick(position, cityDetail);
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
        void onItemClick(int position, CityDetail model);

    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

}