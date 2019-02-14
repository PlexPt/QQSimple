package me.zpp0196.qqpurify.fragment.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by zpp0196 on 2019/2/12.
 */

public class EditTextPreference extends android.preference.EditTextPreference {

    public EditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Adds the EditText widget of this preference to the dialog's view.
     *
     * @param dialogView The dialog view.
     */
    @Override
    @SuppressLint ("PrivateApi")
    protected void onAddEditTextToDialogView(View dialogView, EditText editText) {
        try {
            Class clazz = Class.forName("com.android.internal.R$id");
            Field edittext_container = clazz.getDeclaredField("edittext_container");
            edittext_container.setAccessible(true);
            Field message = clazz.getDeclaredField("message");
            message.setAccessible(true);
            ViewGroup container = dialogView.findViewById((int) edittext_container.get(null));
            if (container != null) {
                TextView messageTextView = container.findViewById((int) message.get(null));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 0, 0);
                messageTextView.setLayoutParams(layoutParams);
                container.addView(editText, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
