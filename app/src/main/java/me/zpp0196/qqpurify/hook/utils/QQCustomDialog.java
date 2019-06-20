package me.zpp0196.qqpurify.hook.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import me.zpp0196.library.xposed.XMethod;

/**
 * Created by zpp0196 on 2019/5/18.
 */
@SuppressWarnings("WeakerAccess")
public class QQCustomDialog implements QQClasses {

    public static class Builder {

        private Dialog mDialog;
        private XMethod mXMethod;

        public Builder(Activity activity) {
            this.mDialog = XMethod.create(QQConfigUtils.findClass(DialogUtil), activity.getClassLoader())
                    .exact(QQConfigUtils.findClass(QQCustomDialog), "a")
                    .types(Context.class, int.class)
                    .invoke(activity, 0);
            this.mXMethod = XMethod.create(mDialog);
        }

        public Builder title(String title) {
            mXMethod.name("setTitle").types(String.class).invoke(title);
            return this;
        }

        public Builder message(CharSequence message) {
            mXMethod.name("setMessage").types(CharSequence.class).invoke(message);
            return this;
        }

        public Builder positive(String text, DialogInterface.OnClickListener onClickListener) {
            mXMethod.name("setPositiveButton")
                    .types(String.class, DialogInterface.OnClickListener.class)
                    .invoke(text, onClickListener);
            return this;
        }

        public Builder negative(String text, DialogInterface.OnClickListener onClickListener) {
            mXMethod.name("setNegativeButton")
                    .types(String.class, DialogInterface.OnClickListener.class)
                    .invoke(text, onClickListener);
            return this;
        }

        public Builder cancel(boolean cancelable) {
            mDialog.setCancelable(cancelable);
            return this;
        }

        public Dialog build() {
            return mDialog;
        }
    }
}
