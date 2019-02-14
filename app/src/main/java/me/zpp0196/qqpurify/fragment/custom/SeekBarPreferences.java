package me.zpp0196.qqpurify.fragment.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import me.zpp0196.qqpurify.R;

/**
 * Created by zpp0196 on 2019/2/12.
 */

public class SeekBarPreferences extends DialogPreference implements SeekBar.OnSeekBarChangeListener {

    private int minValue;
    private int maxValue;
    private int defaultValue;
    private int currentValue;

    private SeekBar seekBar;
    private TextView valueText;

    public SeekBarPreferences(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.me_zpp0196_qqpurify_fragment_custom_SeekBarPreferences);
        maxValue = array.getInteger(R.styleable.me_zpp0196_qqpurify_fragment_custom_SeekBarPreferences_maxValue, 100);
        minValue = array.getInteger(R.styleable.me_zpp0196_qqpurify_fragment_custom_SeekBarPreferences_minValue, 0);
        defaultValue = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "defaultValue", 50);
        array.recycle();
    }

    @SuppressLint ("InflateParams")
    @Override
    protected View onCreateDialogView() {
        currentValue = getPersistedInt(defaultValue);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        if (inflater != null) {
            view = inflater.inflate(R.layout.preference_dialog_seekbar, null);
            seekBar = view.findViewById(R.id.seekbar);
            seekBar.setMax(maxValue - minValue);
            seekBar.setProgress(currentValue - minValue);
            seekBar.setOnSeekBarChangeListener(this);
            valueText = view.findViewById(R.id.value);
            valueText.setText(String.valueOf(currentValue));
        }
        return view;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            if (callChangeListener(currentValue)) {
                persistInt(currentValue);
                notifyChanged();
            }
        }
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        currentValue = progress + minValue;
        valueText.setText(String.valueOf(currentValue));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
