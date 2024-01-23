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
import com.javinindia.nismwa.modelClasses.blooddonorparsing.DonorDetail;
import com.javinindia.nismwa.modelClasses.eventnewsparsing.EventDetail;
import com.javinindia.nismwa.utility.Utility;
import com.javinindia.nismwa.volleycustomrequestSeller.VolleySingleTon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DonorAdopter extends RecyclerView.Adapter<DonorAdopter.ViewHolder> {
    List<Object> list;
    Context context;
    MyClickListener myClickListener;
    int value;

    public DonorAdopter(Context context, int i) {
        this.context = context;
        this.value = i;
    }

    public void setData(List<Object> list) {
        this.list = list;
    }

    public List<Object> getData() {
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notificaton_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final VolleySingleTon volleySingleTon = VolleySingleTon.getInstance(context);
        final DonorDetail requestDetail = (DonorDetail) list.get(position);

        if (!TextUtils.isEmpty(requestDetail.getName().trim())) {
            String Name = "<b>" + requestDetail.getName().trim() + "</b>";
            // viewHolder.txtEventName.setText(Utility.fromHtml("<b>" + Name + "</b>"));
            if (!TextUtils.isEmpty(requestDetail.getGroup_name().trim())) {
                Name = Name + "<br>(Blood Group : " + requestDetail.getGroup_name().trim() + ")";
            }

            viewHolder.txtEventName.setText(Utility.fromHtml(Name));
        }


        if (!TextUtils.isEmpty(requestDetail.getMobile().trim())) {
            String dis = requestDetail.getMobile().trim();
            viewHolder.txtDisc.setText(Utility.fromHtml(dis));
        }

        if (!TextUtils.isEmpty(requestDetail.getInsertDate().trim())) {
            String dis = requestDetail.getInsertDate().trim();
            viewHolder.txtDateTime.setText(Utility.getDateFormatAmAndPm(dis));
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtEventName, txtDateTime, txtDisc, txtAddress, txtAttachment;

        public RelativeLayout rlEventItem;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtEventName = itemLayoutView.findViewById(R.id.txtEventName);
            txtEventName.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
            txtEventName.setBackgroundColor(Utility.getColor(context, android.R.color.white));
            txtDateTime = itemLayoutView.findViewById(R.id.txtDateTime);
            txtDateTime.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
            txtDisc = itemLayoutView.findViewById(R.id.txtDisc);
            txtDisc.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
            rlEventItem = itemLayoutView.findViewById(R.id.rlEventItem);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {
        // void onEventClick(int position, DonorDetail detailEventItem);
    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void deleteItem(int index) {
        list.remove(index);
        notifyItemRemoved(index);
    }
}