package com.javinindia.nismwa.recyclerView;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
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
import com.javinindia.nismwa.font.FontRobotoCondensedRegularSingleTonClass;
import com.javinindia.nismwa.modelClasses.NavigationModel;
import com.javinindia.nismwa.picasso.CircleTransform;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Ashish Tiwari on 12/27/2016.
 */
public class NavigationRecycler extends RecyclerView.Adapter<NavigationRecycler.ViewHolder> implements View.OnClickListener {
    private Context activity;
    private List<NavigationModel> navigationModelList;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    NavItemListener navItemListener;

    public NavigationRecycler(Context activity, List<NavigationModel> navigationModelList) {
        this.activity = activity;
        this.navigationModelList = navigationModelList;
    }

    public void setListener(NavItemListener navItemListener) {
        this.navItemListener = navItemListener;
    }

    @Override
    public NavigationRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View navigationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_row_layout, parent, false);
            ViewHolder navigationViewHolder = new ViewHolder(navigationView, viewType);
            return navigationViewHolder;
        } else if (viewType == TYPE_HEADER) {
            View navHeaderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_navigation, parent, false);
            ViewHolder navigationHeader = new ViewHolder(navHeaderView, viewType);
            return navigationHeader;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final NavigationRecycler.ViewHolder holder, final int position) {
        if (holder.viewTypeId == TYPE_ITEM) {

            final NavigationModel navigationModel = navigationModelList.get(position - 1);
            holder.navTitle.setText(navigationModel.getNavTitle());
            holder.navIcon.setImageDrawable(navigationModel.getNavIcons());
           /* if (navigationModel.getNavTitle().equalsIgnoreCase("eleven")) {
                holder.navArrowIcon.setVisibility(View.VISIBLE);
            }else {
                holder.navArrowIcon.setVisibility(View.GONE);
                holder.navArrowIconUp.setVisibility(View.GONE);
            }*/

            /*if (position%2==0){
                holder.parentLayout.setBackgroundColor(Utility.getColor(activity,R.color.colorPrimary));
            }else {
                holder.parentLayout.setBackgroundColor(Utility.getColor(activity,R.color.colorAccent));
            }*/

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navItemListener.onItemClickPosition(position);
                   /* if (navigationModel.getNavTitle().equalsIgnoreCase("eleven")) {
                        if (holder.subMenuItemParent.getVisibility() == View.VISIBLE) {
                            holder.subMenuItemParent.setVisibility(View.GONE);
                            holder.navArrowIcon.setVisibility(View.VISIBLE);
                            holder.navArrowIconUp.setVisibility(View.GONE);
                        } else {
                            holder.subMenuItemParent.setVisibility(View.VISIBLE);
                            holder.navArrowIcon.setVisibility(View.GONE);
                            holder.navArrowIconUp.setVisibility(View.VISIBLE);
                        }
                    } else {
                        navItemListener.onItemClickPosition(position);
                    }*/
                }
            });

        } else {
            if (!TextUtils.isEmpty(SharedPreferencesManager.getProfileImage(activity)) && !SharedPreferencesManager.getProfileImage(activity).equalsIgnoreCase("null")) {
                Picasso.with(activity).load(SharedPreferencesManager.getProfileImage(activity)).error(R.drawable.profile_icon).placeholder(R.drawable.profile_icon).transform(new CircleTransform()).into(holder.profilePic);
                // Picasso.with(activity).load(SharedPreferencesManager.getProfileImage(activity)).error(R.drawable.profile_icon).transform(new CircleTransform()).into(holder.profilePic);
            } else {
                Picasso.with(activity).load(R.drawable.profile_icon).transform(new CircleTransform()).into(holder.profilePic);
            }

            if (!TextUtils.isEmpty(SharedPreferencesManager.getUsername(activity))) {
                holder.name.setText(Utility.fromHtml(SharedPreferencesManager.getUsername(activity)));
            }
            holder.backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navItemListener.onBackButtonClick();
                }
            });
            holder.llEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navItemListener.onEditClick();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return navigationModelList != null ? navigationModelList.size() + 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personalTitle:
                navItemListener.myProfileCallBack("sub 1");
                break;
            case R.id.educationalTitle:
                navItemListener.myProfileCallBack("sub 2");
                break;
            case R.id.teachingTitle:
                navItemListener.myProfileCallBack("sub 3");
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private int viewTypeId;
        private ImageView navIcon, navArrowIcon, navArrowIconUp, profilePic;
        private TextView navTitle, ratingGraphColor, ratingGraphBg, personalTitle, educationalTitle, teachingTitle;
        private RelativeLayout parentLayout;
        private LinearLayout subMenuItemParent, llEditProfile;
        private AppCompatTextView idNumber, name, txtEditProfile;
        private AppCompatImageView backButton;

        public ViewHolder(View navigationView, int ViewType) {
            super(navigationView);
            if (ViewType == TYPE_ITEM) {
                navIcon = navigationView.findViewById(R.id.navIcon);
                navArrowIcon = navigationView.findViewById(R.id.navArrowIcon);
                navArrowIconUp = navigationView.findViewById(R.id.navArrowIconUp);
                navTitle = navigationView.findViewById(R.id.navTitle);
                navTitle.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
                personalTitle = navigationView.findViewById(R.id.personalTitle);
                educationalTitle = navigationView.findViewById(R.id.educationalTitle);
                teachingTitle = navigationView.findViewById(R.id.teachingTitle);
                subMenuItemParent = navigationView.findViewById(R.id.subMenuItemParent);
                parentLayout = navigationView.findViewById(R.id.parentLayout);
                personalTitle.setOnClickListener(NavigationRecycler.this);
                educationalTitle.setOnClickListener(NavigationRecycler.this);
                teachingTitle.setOnClickListener(NavigationRecycler.this);
                viewTypeId = TYPE_ITEM;
            } else {
                profilePic = navigationView.findViewById(R.id.profilePic);
                llEditProfile = navigationView.findViewById(R.id.llEditProfile);
                txtEditProfile = navigationView.findViewById(R.id.txtEditProfile);
                txtEditProfile.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
                name = navigationView.findViewById(R.id.name);
                name.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
                backButton = navigationView.findViewById(R.id.backButton);
                viewTypeId = TYPE_HEADER;
            }
        }
    }

    public interface NavItemListener {
        void onItemClickPosition(int position);

        void onBackButtonClick();

        void onEditClick();

        void myProfileCallBack(String title);
    }
}
