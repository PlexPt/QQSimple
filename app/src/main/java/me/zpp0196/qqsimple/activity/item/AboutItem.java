package me.zpp0196.qqsimple.activity.item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.zpp0196.qqsimple.R;

/**
 * Created by zpp0196 on 2018/5/26 0026.
 */

@SuppressLint ("ViewConstructor")
public class AboutItem extends LinearLayout {

    private LinearLayout rootView;
    private ImageView icon;
    private TextView title;
    private TextView summary;

    public AboutItem(Context context, int resId, int title) {
        this(context, resId, title, "");
    }

    public AboutItem(Context context, int resId, int title, String summary) {
        super(context);
        LayoutInflater.from(context)
                .inflate(R.layout.item_about, this);
        this.rootView = findViewById(R.id.item_about_layout);
        this.icon = findViewById(R.id.item_about_icon);
        this.title = findViewById(R.id.item_about_title);
        this.summary = findViewById(R.id.item_about_summary);
        setIcon(resId);
        setTitle(context.getString(title));
        if (summary.isEmpty()) {
            this.summary.setVisibility(GONE);
        } else {
            setSummary(summary);
        }
    }

    public void setIcon(int resId) {
        this.icon.setImageDrawable(getResources().getDrawable(resId));
    }

    public void setTitle(CharSequence title) {
        this.title.setText(title);
    }

    public void setSummary(CharSequence summary) {
        this.summary.setText(summary);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.rootView.setOnClickListener(l);
    }
}