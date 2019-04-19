package com.cseaeventmanagement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SlideAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.icon_1,
            R.drawable.icon_2,
            R.drawable.icon_3,
    };

    public String[] slide_headings = {
            "EAT",
            "SLEEP",
            "CODE",
    };

    public String[] slide_descr = {
            "You’ve seen the craze for learning code. But what exactly is coding? Coding is what makes it possible for us to create computer software, apps and websites. Your browser, your OS," +
                    " the apps on your phone, Facebook, and this website – they’re all made with code.",
            "Many coding tutorials use that command as their very first example, because it’s one of the simplest examples of code you can have " +
                    "– it ‘prints’ (displays) the text ‘Hello, world!’ onto the screen.",
            "If you don’t know the first thing about coding, you’ve come to the right place. We’ve put together a beginner’s tutorial which will give you all" +
                    " the background information you need on coding, before you start learning it for real.",
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_imageView);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideText = (TextView) view.findViewById(R.id.slide_text);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideText.setText(slide_descr[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
