package com.pappayaed.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BaselineLayout;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.pappayaed.R;
import com.pappayaed.base.BaseActivity;
import com.pappayaed.common.ActivityUtils;
import com.pappayaed.common.BottomNavigationViewHelper;
import com.pappayaed.common.Config;
import com.pappayaed.common.NotificationUtils;
import com.pappayaed.data.listener.DataListener;
import com.pappayaed.fragmentnavigation.FragmentNavigationManager;
import com.pappayaed.fragmentnavigation.NavigationManager;
import com.pappayaed.ui.attendance.AttendanceActivity;
import com.pappayaed.ui.circular.CircularActivity;
import com.pappayaed.ui.feedetails.FeeDetailsActivity;
import com.pappayaed.ui.heartrate.HeartRateActivity;
import com.pappayaed.ui.login.LoginActivity;
import com.pappayaed.ui.whereismystudent.WhereisMyStudentActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity implements IMainView {

    private Fragment fragment = null;
    private static final String TAG = "MainActivity";

    private NavigationManager navigationManager;
    private IMainPresenter iMainPresenter;
    private BottomNavigationView navigation;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(android.view.Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);

        setCustomView(getResources().getString(R.string.title_dashboard));


        navigationManager = FragmentNavigationManager.obtain(this);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);

//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);


//        for (int i = 0; i < menuView.getChildCount(); i++) {
//
////            View item = (BottomNavigationItemView) menuView.getChildAt(i);
////
//////           v itemTitle = item.getChildAt(1);
////
////            ((TextView) item).setTypeface(Typeface.DEFAULT);
//
//
//            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
//            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
//            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//            // set your height here
//            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
//            // set your width here
//            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
//            iconView.setLayoutParams(layoutParams);
//        }

//        BottomNavigationViewHelper.removeShiftMode(navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        iMainPresenter = new MainPresenterImpl(dataSource);

        iMainPresenter.onAttach(this);
        iMainPresenter.loadMoreFragment();

//        iMainPresenter.bottomNavigationViewPosition(navigation.getMenu().findItem(R.id.txtDate));
        navigation.getMenu().findItem(R.id.circular).setCheckable(false);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(context, "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };

        displayFirebaseRegId();

    }


    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {


//            Map<Object, Object> json = new HashMap<>();
//            Map<Object, Object> params = new HashMap<>();
//            params.put("login", dataSource.getEmailOrUsername());
//            params.put("password", dataSource.getPassword());
//            params.put("user_type", dataSource.getUserType());
//            params.put("device_toten", regId);
//
//            json.put("params", params);
//
//            final String js = new JSONObject(json).toString();
//
//            dataSource.putDeviceToken(js, new DataListener() {
//                @Override
//                public void onSuccess(Object object) {
//
//                    Log.e(TAG, "onSuccess: " + object.toString());
//
//                }
//
//                @Override
//                public void onFail(Throwable throwable) {
//
//                    Log.e(TAG, "onFail: " + throwable.getMessage());
//                }
//
//                @Override
//                public void onNetworkFailure() {
//
//                    Log.e(TAG, "onNetworkFailure: ");
//
//                }
//            });


            Log.e(TAG, "displayFirebaseRegId: " + "Firebase Reg Id: " + regId);
        } else
            Log.e(TAG, "displayFirebaseRegId: Firebase Reg Id is not received yet! ");


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment = navigationManager.getFragment();

    }


    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    @Override
    public void inflateBottomViewParent() {

//        navigation.getMenu().clear();
//        navigation.inflateMenu(R.menu.buttomtab);

    }


    @Override
    public void moveToEventsActivity() {

//        navigationManager.Circular(getString(R.string.title_circular));
//        setTitle(getString(R.string.title_circular));


//        startActivity(new Intent(this, CircularActivity.class));

        ActivityUtils.startActivity(this, CircularActivity.class, new Bundle());

    }

    @Override
    public void moveToAttendanceActivity() {

//        navigationManager.StudentFeeFragment(getString(R.string.title_fee));
//        setTitle(getString(R.string.title_childern));


        ActivityUtils.startActivity(this, AttendanceActivity.class, new Bundle());
    }


    @Override
    public void moveToMoreFragment() {

        navigationManager.MoreFragment(getString(R.string.title_more));
        setTitle(getString(R.string.title_dashboard));


    }

    @Override
    public void moveToFeeDetailsActivity() {

        ActivityUtils.startActivity(this, FeeDetailsActivity.class, new Bundle());
    }

    @Override
    public void moveToHeartRateActivity() {

        ActivityUtils.startActivity(this, HeartRateActivity.class, new Bundle());
    }

    @Override
    public void moveToWhereIsActivity() {

        ActivityUtils.startActivity(this, WhereisMyStudentActivity.class, new Bundle());
    }

    @Override
    public void logout() {
        startActivity(new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iMainPresenter.onDetach();
    }

    private void setTitle(String title) {

        setActionBarTitle(title, false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:

                iMainPresenter.logout();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            item.setCheckable(false);
            iMainPresenter.bottomNavigationViewPosition(item);

            return true;
        }

    };
}
