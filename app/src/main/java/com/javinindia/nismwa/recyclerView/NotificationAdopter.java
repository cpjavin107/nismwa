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
import com.javinindia.nismwa.modelClasses.eventnewsparsing.EventDetail;
import com.javinindia.nismwa.modelClasses.notificationparsing.NotificationDetail;
import com.javinindia.nismwa.utility.Utility;
import com.javinindia.nismwa.volleycustomrequestSeller.VolleySingleTon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ashis on 9/23/2017.
 */

public class NotificationAdopter extends RecyclerView.Adapter<NotificationAdopter.ViewHolder> {
    List<Object> list;
    Context context;
    MyClickListener myClickListener;
    int value;

    public NotificationAdopter(Context context, int i) {
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
    public NotificationAdopter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notificaton_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NotificationAdopter.ViewHolder viewHolder, final int position) {
        final VolleySingleTon volleySingleTon = VolleySingleTon.getInstance(context);
        final NotificationDetail requestDetail = (NotificationDetail) list.get(position);

        if (!TextUtils.isEmpty(requestDetail.getTitle().trim())) {
            String Name = requestDetail.getTitle().trim();
            viewHolder.txtEventName.setText(Utility.fromHtml(Name));
        }


        if (!TextUtils.isEmpty(requestDetail.getDescription().trim())) {
            String dis = requestDetail.getDescription().trim();
            viewHolder.txtDisc.setText(Utility.fromHtml(dis));
        }

        if (!TextUtils.isEmpty(requestDetail.getInsertDate().trim())) {
            String dis = requestDetail.getInsertDate().trim();
            viewHolder.txtDateTime.setText(Utility.getDateFormatAmAndPm(dis));
        }


        viewHolder.rlEventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickListener.onEventClick(position, requestDetail);
            }
        });

        viewHolder.rlEventItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myClickListener.onLongClick(position, requestDetail);
                return false;
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtEventName, txtDateTime, txtDisc;

        public RelativeLayout rlEventItem;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtEventName = itemLayoutView.findViewById(R.id.txtEventName);
            txtEventName.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(context).getTypeFace());
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
        void onEventClick(int position, NotificationDetail detailEventItem);

        void onLongClick(int position, NotificationDetail detailEventItem);
    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void deleteItem(int index) {
        list.remove(index);
        notifyItemRemoved(index);
    }
}