package com.javinindia.nismwa.recyclerView;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.javinindia.nismwa.R;
import com.javinindia.nismwa.font.FontRobotoCondensedBoldSingleTonClass;
import com.javinindia.nismwa.font.FontRobotoCondensedRegularSingleTonClass;
import com.javinindia.nismwa.modelClasses.managementlistparsing.ManagementDetail;
import com.javinindia.nismwa.modelClasses.memberloginparsing.MemberDetail;
import com.javinindia.nismwa.picasso.CircleTransform;
import com.javinindia.nismwa.utility.Utility;
import com.javinindia.nismwa.volleycustomrequestSeller.VolleySingleTon;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ashish on 06-07-2017.
 */

public class ManagementAdopter extends RecyclerView.Adapter<ManagementAdopter.ViewHolder> {
    private List<Object> list;
    private Context context;
    MyClickListener myClickListener;

    public ManagementAdopter(Context context) {
        this.context = context;

    }

    public void setData(List<Object> list) {
        this.list = list;
    }

    public List<Object> getData() {
        return list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView txtName, txtProfession;
        public ImageView imgProfile;
        public RelativeLayout llmain;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            llmain = itemLayoutView.findViewById(R.id.llmain);
            txtName = itemLayoutView.findViewById(R.id.txtName);
            txtName.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(context).getTypeFace());
            txtProfession = itemLayoutView.findViewById(R.id.txtProfession);
            txtProfession.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
            imgProfile = itemLayoutView.findViewById(R.id.imgProfile);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mamgement_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        VolleySingleTon volleySingleTon = VolleySingleTon.getInstance(context);
        String firstName = "";

        final ManagementDetail viewDetail = (ManagementDetail) list.get(position);

        if (!TextUtils.isEmpty(viewDetail.getImage().toString().trim())) {
            String pic = viewDetail.getImage().toString().trim();
            Picasso.with(context).load(pic).error(R.drawable.profile_icon).placeholder(R.drawable.profile_icon).transform(new CircleTransform()).into(viewHolder.imgProfile);
        } else {
            Picasso.with(context).load(R.drawable.profile_icon).transform(new CircleTransform()).into(viewHolder.imgProfile);
        }

        if (!TextUtils.isEmpty(viewDetail.getMc_name().trim())) {
            firstName = viewDetail.getMc_name().trim();
        }


        if (!TextUtils.isEmpty(firstName)) {
            viewHolder.txtName.setText(Utility.fromHtml(firstName));
        } else {
            viewHolder.txtName.setText("Name not found");
        }


        if (!TextUtils.isEmpty(viewDetail.getDesignationName().trim())) {
            String proName = viewDetail.getDesignationName().trim();
            viewHolder.txtProfession.setText(Utility.fromHtml(proName));
        } else {
            viewHolder.txtProfession.setText("Designation not found");
        }


        viewHolder.llmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(position, viewDetail);
            }
        });


    }

    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public interface MyClickListener {

        void onItemClick(int position, ManagementDetail detailsList);

    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

}