package com.javinindia.nismwa.recyclerView;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.javinindia.nismwa.R;
import com.javinindia.nismwa.font.FontRobotoCondensedBoldSingleTonClass;
import com.javinindia.nismwa.font.FontRobotoCondensedRegularSingleTonClass;
import com.javinindia.nismwa.modelClasses.commentparsing.CommentDetail;
import com.javinindia.nismwa.picasso.CircleTransform;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.utility.Utility;
import com.javinindia.nismwa.volleycustomrequestSeller.VolleySingleTon;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ashish on 17-06-2016.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    List<Object> list;
    Context context;
    MyClickListener myClickListener;

    public CommentAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Object> list) {
        this.list = list;
    }

    public List<Object> getData() {
        return list;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder viewHolder, final int position) {
        final VolleySingleTon volleySingleTon = VolleySingleTon.getInstance(context);
        final CommentDetail viewDetail = (CommentDetail) list.get(position);

        String firstName = "", empType = "", middleName = "", lastName = "";

        if (!TextUtils.isEmpty(viewDetail.getMemberDetailArrayList().get(0).getName())) {
            firstName = viewDetail.getMemberDetailArrayList().get(0).getName().trim();
        }

        if (!TextUtils.isEmpty(firstName)) {
            viewHolder.namePerson.setText(firstName);
        } else {
            viewHolder.namePerson.setText("Name not found");
        }

        if (!TextUtils.isEmpty(viewDetail.getMemberDetailArrayList().get(0).getImageUrl().trim())) {
            String pic = viewDetail.getMemberDetailArrayList().get(0).getImageUrl().trim();
            Picasso.with(context).load(pic).error(R.drawable.profile_icon).placeholder(R.drawable.profile_icon).into(viewHolder.imgUser);
        } else {
            Picasso.with(context).load(R.drawable.profile_icon).transform(new CircleTransform()).into(viewHolder.imgUser);
        }

        if (!TextUtils.isEmpty(viewDetail.getDescription().trim())) {
            viewHolder.comment.setText(viewDetail.getDescription().trim());
        } else {
            viewHolder.comment.setText("No comments");
        }

        if (!TextUtils.isEmpty(viewDetail.getInsertDate().trim())) {
            String date = viewDetail.getInsertDate().trim();
            viewHolder.txt_date_time.setText(Utility.getDateFormatAmAndPm(date));
        }

        final String uid = SharedPreferencesManager.getUserID(context);
        final String wishUid = viewDetail.getMemberId();
        if (uid.equals(wishUid)) {
            viewHolder.layout_post_detail.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    myClickListener.onLongItemClick(position, viewDetail);
                    return true;
                }
            });
        }
        viewHolder.imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onUserPicClick(position, viewDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgUser;
        public RelativeLayout layout_post_detail;
        public AppCompatTextView namePerson, comment, txt_date_time;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            layout_post_detail = itemLayoutView.findViewById(R.id.layout_post_detail);
            imgUser = itemLayoutView.findViewById(R.id.imgView_post_profile);
            namePerson = itemLayoutView.findViewById(R.id.txt_profile_name);
            namePerson.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(context).getTypeFace());
            comment = itemLayoutView.findViewById(R.id.txt_comment);
            comment.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
            txt_date_time = itemLayoutView.findViewById(R.id.txt_date_time);
            txt_date_time.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(context).getTypeFace());
        }
    }

    public interface MyClickListener {
        void onLongItemClick(int position, CommentDetail viewDetail);

        void onUserPicClick(int position, CommentDetail viewDetail);

    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void deleteItem(int index) {
        list.remove(index);
        notifyItemRemoved(index);
    }

    public void addItem(int position, CommentDetail viewDetail) {
        list.add(position, viewDetail);
        notifyItemInserted(position);
    }
}