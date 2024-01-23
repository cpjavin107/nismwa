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
import com.javinindia.nismwa.utility.Utility;
import com.javinindia.nismwa.volleycustomrequestSeller.VolleySingleTon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ashish on 30-03-2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    List<Object> list;
    Context context;
    MyClickListener myClickListener;
    int value;

    public EventAdapter(Context context, int i) {
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
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item_layout, parent, false);

        EventAdapter.ViewHolder viewHolder = new EventAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder viewHolder, final int position) {
        final VolleySingleTon volleySingleTon = VolleySingleTon.getInstance(context);
        final EventDetail requestDetail = (EventDetail) list.get(position);

        if (requestDetail.getImageDetailArrayList().size() > 0) {
            viewHolder.txtAttachment.setVisibility(View.VISIBLE);
        } else {
            viewHolder.txtAttachment.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(requestDetail.getTitle().trim())) {
            String Name = requestDetail.getTitle().trim();
            viewHolder.txtEventName.setText(Utility.fromHtml(Name));
        }


        if (!TextUtils.isEmpty(requestDetail.getDescription().trim())) {
            String dis = requestDetail.getDescription().trim();
            viewHolder.txtDisc.setText(Utility.fromHtml(dis));
        }
        if (value == 1) {
            if (!TextUtils.isEmpty(requestDetail.getAddress().trim())) {
                String address = requestDetail.getAddress().trim();
                viewHolder.txtAddress.setText(Utility.fromHtml("<b>Venue : </b>" + address));
                viewHolder.txtAddress.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.txtAddress.setVisibility(View.GONE);
        }

        final ArrayList<String> data = new ArrayList<>();
        if (!TextUtils.isEmpty(requestDetail.getDate().trim())) {
            data.add(requestDetail.getDate().trim());
        }
        if (!TextUtils.isEmpty(requestDetail.getTime().trim())) {
            data.add("<b>Time : </b>" + requestDetail.getTime().trim());
        }


        if (data.size() > 0) {
            String str = Arrays.toString(data.toArray());
            String test = str.replaceAll("[\\[\\](){}]", "");
            viewHolder.txtDateTime.setText(Utility.fromHtml("<b>Date : </b>" + test));
        }

        viewHolder.rlEventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickListener.onEventClick(position, requestDetail);
            }
        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtEventName, txtDateTime, txtDisc, txtAddress, txtAttachment;

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
            txtAttachment = itemLayoutView.findViewById(R.id.txtAttachment);
            txtAttachment.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
            rlEventItem = itemLayoutView.findViewById(R.id.rlEventItem);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {
        void onEventClick(int position, EventDetail detailEventItem);
    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void deleteItem(int index) {
        list.remove(index);
        notifyItemRemoved(index);
    }
}