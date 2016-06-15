package com.haozhn.imageselector;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * BaseActivity new
 */
public abstract class BaseActivity extends FragmentActivity{

    protected Activity context;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getBundle(getIntent().getExtras() == null ? new Bundle() : getIntent().getExtras());
        if (getInflateLayout() > 0) {
            View view = View.inflate(this, getInflateLayout(), null);
            setContentView(view);
            setUpView(view);
        }
    }

    protected final <T extends View> T $(int resId) {
        return (T) findViewById(resId);
    }

    protected abstract void setUpView(View view);

    protected abstract int getInflateLayout();

    protected void getBundle(Bundle bundle) {
    }

    /**
     * 显示Toast
     *
     * @param message 显示内容
     */
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示Toast
     *
     * @param resId
     */
    public void showToast(@StringRes int resId) {
        showToast(getString(resId));
    }

}
