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
import com.javinindia.nismwa.modelClasses.suggestionparsing.SuggestionDetail;
import com.javinindia.nismwa.utility.Utility;
import com.javinindia.nismwa.volleycustomrequestSeller.VolleySingleTon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ashis on 11/21/2017.
 */

public class SuggestionAdopter extends RecyclerView.Adapter<SuggestionAdopter.ViewHolder> {
    List<Object> list;
    Context context;
    MyClickListener myClickListener;
    int value;

    public SuggestionAdopter(Context context) {
        this.context = context;
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
                .inflate(R.layout.event_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final VolleySingleTon volleySingleTon = VolleySingleTon.getInstance(context);
        final SuggestionDetail requestDetail = (SuggestionDetail) list.get(position);

        if (!TextUtils.isEmpty(requestDetail.getC_title().trim())) {
            String Name = requestDetail.getC_title().trim();
            viewHolder.txtEventName.setText(Utility.fromHtml(Name));
        }


        if (!TextUtils.isEmpty(requestDetail.getComplain().trim())) {
            String dis = requestDetail.getComplain().trim();
            viewHolder.txtDateTime.setText(Utility.fromHtml(dis));
        }

        if (!TextUtils.isEmpty(requestDetail.getInsertDate().trim())) {
            String address = requestDetail.getInsertDate().trim();
            viewHolder.txtAddress.setText(Utility.getDateFormatAmAndPm(address));
            viewHolder.txtAddress.setVisibility(View.VISIBLE);
        }
        if (requestDetail.getSuggestionReplyArrayList().size() > 0) {

            int size = requestDetail.getSuggestionReplyArrayList().size();
            final ArrayList<String> data = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                if (!TextUtils.isEmpty(requestDetail.getSuggestionReplyArrayList().get(i).getReplyDescription().trim())) {
                    data.add("<strong>Nismwa : </strong>" + requestDetail.getSuggestionReplyArrayList().get(i).getReplyDescription().trim() + "<br>");
                }
            }

          /*  if (!TextUtils.isEmpty(requestDetail.getTime().trim())) {
                data.add("<b>Time : </b>" + requestDetail.getTime().trim());
            }
*/

            if (data.size() > 0) {
                String str = Arrays.toString(data.toArray());
                String test = str.replaceAll("[\\[\\](){},]", "");
                // test = str.replaceAll("[,]", "");
                viewHolder.txtDisc.setText(Utility.fromHtml(test));
            }
        }

        viewHolder.rlEventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickListener.onEventClick(position, requestDetail);
            }
        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtEventName, txtDateTime, txtDisc, txtAddress;

        public RelativeLayout rlEventItem;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtEventName = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtEventName);
            txtEventName.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(context).getTypeFace());
            txtDateTime = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtDateTime);
            txtDateTime.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
            txtDisc = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtDisc);
            txtDisc.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
            txtAddress = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtAddress);
            txtAddress.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
            rlEventItem = (RelativeLayout) itemLayoutView.findViewById(R.id.rlEventItem);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {
        void onEventClick(int position, SuggestionDetail detailEventItem);
    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void deleteItem(int index) {
        list.remove(index);
        notifyItemRemoved(index);
    }
}
