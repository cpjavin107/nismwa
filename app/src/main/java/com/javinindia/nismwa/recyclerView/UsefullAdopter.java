package com.javinindia.nismwa.recyclerView;

import android.content.Context;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.javinindia.nismwa.R;
import com.javinindia.nismwa.font.FontRobotoCondensedBoldSingleTonClass;

import com.javinindia.nismwa.font.FontRobotoCondensedRegularSingleTonClass;
import com.javinindia.nismwa.modelClasses.usefullInfoparsing.UsefullDetail;
import com.javinindia.nismwa.utility.Utility;
import com.javinindia.nismwa.volleycustomrequestSeller.VolleySingleTon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ashish on 17-07-2017.
 */

public class UsefullAdopter extends RecyclerView.Adapter<UsefullAdopter.ViewHolder> {
    List<Object> list;
    Context context;
    MyClickListener myClickListener;

    public UsefullAdopter(Context context) {
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
                .inflate(R.layout.usefull_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final VolleySingleTon volleySingleTon = VolleySingleTon.getInstance(context);
        final UsefullDetail requestDetail = (UsefullDetail) list.get(position);

        if (!TextUtils.isEmpty(requestDetail.getAddress().trim())) {
            String Name = requestDetail.getAddress().trim();
            viewHolder.txtName.setText(Utility.fromHtml(Name));
        }

        if (!TextUtils.isEmpty(requestDetail.getContactNumber())) {
            String contact = requestDetail.getContactNumber().trim();
            String[] items = contact.split(",");
            ArrayList list1 = new ArrayList<>(Arrays.asList(items));
            int size = list1.size();

            if (size > 0) {
                viewHolder.llContact.removeAllViews();
                for (int i = 0; i < items.length; i++) {
                    String model = " ", vType = " ", vNum = " ";
                    if (!TextUtils.isEmpty(list1.get(i).toString())) {
                        vType = list1.get(i).toString().trim();
                    }

                    RelativeLayout relativeLayout = new RelativeLayout(context);

                    TextView tv = new TextView(context);
                    ImageView imageView = new ImageView(context);
                    imageView.setImageResource(android.R.drawable.ic_menu_call);

                    tv.setText(Utility.fromHtml(vType));
                    tv.setBackgroundResource(R.drawable.button_white_border_gray);
                    tv.setPadding(16, 8, 16, 8);
                    tv.setTextSize(16);
                    tv.setTextColor(Utility.getColor(context, android.R.color.black));
                    tv.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
                    tv.setId(i + size);

                    final String finalVType = vType;
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myClickListener.onContactItemClick(position, requestDetail, finalVType);
                        }
                    });

                    final RelativeLayout.LayoutParams cb =
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    cb.addRule(RelativeLayout.BELOW, i + size);
                    relativeLayout.addView(tv, cb);

                    final RelativeLayout.LayoutParams cb1 = new RelativeLayout.LayoutParams(40, 40);
                    cb1.setMargins(10, 10, 10, 10);
                    cb1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, i + size);
                    relativeLayout.addView(imageView, cb1);


                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBarOverlayLayout.LayoutParams.MATCH_PARENT, ActionBarOverlayLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 10, 10, 10);
                    relativeLayout.setLayoutParams(params);

                    viewHolder.llContact.addView(relativeLayout);
                }
            } else {
                TextView tv = new TextView(context);
                tv.setText("No Vehicle");
                tv.setPadding(16, 8, 16, 8);
                tv.setTextSize(16);
                tv.setTextColor(Utility.getColor(context, android.R.color.black));
                tv.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
                tv.setId(0);
                viewHolder.llContact.addView(tv);
            }
        }


        viewHolder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickListener.onEventClick(position, requestDetail);
            }
        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtName;

        public LinearLayout llMain, llContact;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtName = itemLayoutView.findViewById(R.id.txtName);
            txtName.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(context).getTypeFace());

            llMain = itemLayoutView.findViewById(R.id.llMain);
            llContact = itemLayoutView.findViewById(R.id.llContact);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {
        void onEventClick(int position, UsefullDetail detailEventItem);

        void onContactItemClick(int position, UsefullDetail detailEventItem, String num);
    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void deleteItem(int index) {
        list.remove(index);
        notifyItemRemoved(index);
    }
}
