package com.pappayaed.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import com.pappayaed.R;
import com.pappayaed.base.BaseActivity;
import com.pappayaed.ui.login.LoginActivity;
import com.pappayaed.ui.main.MainActivity;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends BaseActivity implements ISplashView {


    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    private SplashPresenterImpl splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        ButterKnife.bind(this);

        splashPresenter = new SplashPresenterImpl(dataSource);

        splashPresenter.onAttach(this);

        avi.smoothToShow();
    }


    @Override
    protected void onResume() {
        super.onResume();

        splashPresenter.moveToNextActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        splashPresenter.cancelHandler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        splashPresenter.onDetach();

    }

    @Override
    public void gotoMainActivity() {
//        avi.smoothToHide();
        startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

    }

    @Override
    public void gotoLoginActivity() {
//        avi.smoothToHide();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

    }
}
