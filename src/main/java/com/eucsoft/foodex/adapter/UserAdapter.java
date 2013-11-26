package com.eucsoft.foodex.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eucsoft.foodex.R;
import com.eucsoft.foodex.db.model.FoodPair;

public class UserAdapter extends PagerAdapter {

    private FoodPair.User user;

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return  view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imageView = new ImageView(container.getContext());
        // int padding = ssContext.getResources().getDimensionPixelSize(0x7f040002);
        imageView.setPadding(0, 0, 0, 0);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (position == 0){
            imageView.setImageResource(R.drawable.f);
        } else {
            imageView.setImageResource(R.drawable.f);
        }
        container.addView(imageView, 0);

        return imageView;
    }


}
