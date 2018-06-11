package com.pappayaed.ui.splash;

import android.os.Handler;

import com.pappayaed.base.BasePresenter;
import com.pappayaed.data.DataSource;

/**
 * Created by yasar on 26/3/18.
 */

public class SplashPresenterImpl<V extends ISplashView> extends BasePresenter<V> implements ISplashPresenter<V> {

    private static final int AUTOTIME = 3000;

    private Handler handler;

    public SplashPresenterImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void moveToNextActivity() {

        if (handler == null) {

            handler = new Handler();

            handler.postDelayed(runnable, AUTOTIME);
        }

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (getDataSource().isLoggedIn()) getMvpView().gotoMainActivity();
            else getMvpView().gotoLoginActivity();

        }
    };


    @Override
    public void cancelHandler() {

        if (handler != null) {

            handler.removeCallbacks(runnable);
            handler = null;
        }

    }
}
