package me.zpp0196.qqsimple.activity.item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.zpp0196.qqsimple.Common;
import me.zpp0196.qqsimple.R;

import static me.zpp0196.qqsimple.util.CommUtil.isInstalled;

/**
 * Created by zpp0196 on 2018/5/28 0027.
 */

public class CardItem extends LinearLayout {

    private Context context;
    private TextView title;
    private LinearLayout itemLayout;

    public CardItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context)
                .inflate(R.layout.item_card, this);
        this.context = context;
        this.title = findViewById(R.id.item_card_title);
        this.itemLayout = findViewById(R.id.item_card_layout);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CardItem);
        this.title.setText(array.getString(R.styleable.CardItem_title));
        array.recycle();
    }

    public void setTitle(CharSequence title) {
        this.title.setText(title);
    }

    public void addItem(Item... items) {
        itemLayout.removeAllViews();
        for (Item item : items) {
            addItem(item);
        }
    }

    public void addItem(Item item) {
        if (item != null) {
            if (item.getTitle()
                        .contains("VirtualXposed") &&
                !isInstalled(context, Common.PACKAGE_NAME_VXP)) {
                return;
            }
            itemLayout.addView(item);
        }
    }

    @SuppressLint ("ViewConstructor")
    public static class Item extends LinearLayout {

        private LinearLayout rootView;
        private ImageView icon;
        private TextView title;

        public Item(Context context, int resId, int title) {
            this(context, resId, context.getString(title));
        }

        public Item(Context context, int resId, CharSequence title) {
            super(context);
            LayoutInflater.from(context)
                    .inflate(R.layout.item_card_item, this);
            this.rootView = findViewById(R.id.item_card_item_layout);
            this.icon = findViewById(R.id.item_card_item_icon);
            this.title = findViewById(R.id.item_card_item_text);
            setIcon(resId);
            setTitle(title);
        }

        public void setIcon(int resId) {
            this.icon.setImageDrawable(getResources().getDrawable(resId));
        }

        public String getTitle() {
            return this.title.getText()
                    .toString();
        }

        public void setTitle(CharSequence title) {
            this.title.setText(title);
        }

        public TextView getTitleView() {
            return this.title;
        }

        @Override
        public void setOnClickListener(@Nullable OnClickListener l) {
            super.setOnClickListener(l);
            this.rootView.setOnClickListener(l);
        }

        @Override
        public void setOnLongClickListener(@Nullable OnLongClickListener l) {
            super.setOnLongClickListener(l);
            this.rootView.setOnLongClickListener(l);
        }
    }
}
