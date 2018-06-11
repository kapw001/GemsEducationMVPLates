package com.pappayaed.ui.attendance;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pappayaed.R;
import com.pappayaed.base.BaseActivity;
import com.pappayaed.common.Utils;
import com.pappayaed.data.model.AttendanceList;
import com.pappayaed.interfaces.CallBackStudentID;
import com.pappayaed.ui.calendarandlistview.CalendarAndListFragment;
import com.pappayaed.ui.showprofile.StudentList;
import com.pappayaed.ui.studentfee.StudentFeeFragment;
import com.vlonjatg.progressactivity.ProgressFrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import calendar.android.com.customcalendar.EventHighlight;
import calendar.android.com.customcalendar.EventObjects;

public class AttendanceActivity extends BaseActivity implements CallBackStudentID, IAttendanceView, CalendarAndListFragment.OnItemClickCallback {

    private static final String TAG = "AttendanceActivity";

    @BindView(R.id.laytest)
    ProgressFrameLayout laytest;


    private IAttendancePresenter iAttendancePresenter;

    private CalendarAndListFragment calendarAndListFragment;
    private StudentFeeFragment studentFeeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        ButterKnife.bind(this);


        setCustomView("Attendance");

        studentFeeFragment = (StudentFeeFragment) getSupportFragmentManager().findFragmentById(R.id.studentFeeFragment);

        calendarAndListFragment = (CalendarAndListFragment) getSupportFragmentManager().findFragmentById(R.id.calendarAndListFragment);

        calendarAndListFragment.setEventHighlight(EventHighlight.RECTANGLE);


        iAttendancePresenter = (IAttendancePresenter) getLastCustomNonConfigurationInstance();

        if (iAttendancePresenter == null) {

            iAttendancePresenter = new AttendancePresenterImpl(this, new AttendanceIntractorImpl(dataSource));
        }


        callNetwork();


    }


    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return iAttendancePresenter;
    }

    @Override
    public void loadStudentID(StudentList studentList) {

//        Toast.makeText(this, "" + studentList.getId(), Toast.LENGTH_SHORT).show();
        hideLoading();


        iAttendancePresenter.getAttendanceList(studentList, "", "");

    }


    @Override
    public void callNetwork() {
        studentFeeFragment.call();
    }

    @Override
    public void onFail(Throwable throwable) {

//        setEmptyData();

        Toast.makeText(this, "Called", Toast.LENGTH_SHORT).show();

        progressBarStateCall(laytest, R.drawable.somethingwentwrong, "error");

    }

    @Override
    public void onNetworkFailure() {
//        setEmptyData();


        progressBarStateCall(laytest, R.drawable.nointernet, "nointernet");
    }

    @Override
    public void showLoading() {

        progressBarStateCall(laytest, R.drawable.nointernet, "loading");

    }

    @Override
    public void hideLoading() {

        progressBarStateCall(laytest, R.drawable.nointernet, "content");

    }


    @Override
    public void showSnackBar(View view, String msg) {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void setData(List<AttendanceList> list) {

        calendarAndListFragment.updateAttendance(list);

    }

    @Override
    public void setEmptyData() {

        calendarAndListFragment.showErrorOrEmptyView(null, true);

    }

    @Override
    public void setEventList(List<EventObjects> eventList) {
        Log.e(TAG, "setEventList: " + eventList.size());

        calendarAndListFragment.updateEventList(eventList);
    }

    @Override
    public void showLodingDialog() {

        super.showLoading();
    }

    @Override
    public void hideLodingDialog() {

        super.hideLoading();
    }

    @Override
    public void onItemClick(View view, int position, Object o) {


        iAttendancePresenter.onItemClick(view, position, o);


    }
}
