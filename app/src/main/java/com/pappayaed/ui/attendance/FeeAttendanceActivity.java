package com.pappayaed.ui.attendance;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pappayaed.R;
import com.pappayaed.adapter.AttadanceAdapter;
import com.pappayaed.base.BaseActivity;
import com.pappayaed.common.Utils;
import com.pappayaed.errormsg.Error;
import com.pappayaed.data.model.AttendanceList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import calendar.android.com.customcalendar.EventObjects;


public class FeeAttendanceActivity extends BaseActivity implements IAttendanceView, AttadanceAdapter.RecyclerAdapterPositionClicked {


    private static final String TAG = "FeeDetailsActivity";
    private RecyclerView recyclerView;
    private TextView error;
    private AttadanceAdapter feeAdapter;
    private ArrayList<AttendanceList> list;
    private LinearLayout layoutstartdate, layoutendate;
    private TextView startdate, enddate;
    private long id;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TabLayout tabLayout;

    private IAttendancePresenter iAttendancePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_detailsnew);

        getSupportActionBar().setTitle("Attendance Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        layoutstartdate = (LinearLayout) findViewById(R.id.layoutstartdate);
        layoutendate = (LinearLayout) findViewById(R.id.layoutenddate);
        startdate = (TextView) findViewById(R.id.startdate);
        enddate = (TextView) findViewById(R.id.enddate);
        startdate.setText(Utils.getCurrentDate(0, "dd/MM/yyyy"));
        enddate.setText(Utils.getCurrentDate(0, "dd/MM/yyyy"));
        layoutstartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(startdate);
            }
        });

        layoutendate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerEndDate(enddate);
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        error = (TextView) findViewById(R.id.error);

        list = new ArrayList<>();
        feeAdapter = new AttadanceAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feeAdapter);

        id = getIntent().getLongExtra("studentid", 0);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callServices();
//                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        iAttendancePresenter = new AttendancePresenterImpl(this, new AttendanceIntractorImpl(dataSource));


        callServices();

    }

    private void callServices() {
        mSwipeRefreshLayout.setRefreshing(true);
        iAttendancePresenter.getAttendanceList(id, startdate.getText().toString(), enddate.getText().toString());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void position(int pos, View view) {

    }

    @Override
    public void onRefresh() {

    }

    private void datePicker(final TextView textView) {
        if (textView.getText().toString().length() > 0) {
            int mYear, mMonth, mDay, mHour, mMinute;
            // Get Current Date
            final Calendar c = Calendar.getInstance();

            Date date = getDate(textView.getText().toString(),"dd/MM/yyyy");
            c.setTime(date);

            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(FeeAttendanceActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            Calendar cu = Utils.getCalendar(getDate(enddate.getText().toString(),"dd/MM/yyyy"));

                            Calendar sl = Utils.getCalendar(getDate(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth,"dd/MM/yyyy"));

                            if (sl.compareTo(cu) <= 0) {

//                                textView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                textView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);


                            } else {
                                Toast.makeText(getApplicationContext(), "Start date cannot be greater than end date ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else {
            Toast.makeText(getApplicationContext(), "Please select date", Toast.LENGTH_SHORT).show();
        }

    }

    private void datePickerEndDate(final TextView textView) {
        if (textView.getText().toString().length() > 0) {
            int mYear, mMonth, mDay, mHour, mMinute;
            // Get Current Date
            final Calendar c = Calendar.getInstance();

            Date date = getDate(textView.getText().toString(),"dd/MM/yyyy");
            c.setTime(date);

            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(FeeAttendanceActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
//                            Calendar cu = Utils.getCalendar(getDate(startdate.getText().toString()));
//
//                            Calendar sl = Utils.getCalendar(getDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
//
//                            if (cu.compareTo(sl) <= 0) {

//                            textView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            textView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

//                                try {
//                                    getTimeTable();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }

//                            } else {
//                                Toast.makeText(getApplicationContext(), "End date cannot be lesthen than start date ", Toast.LENGTH_SHORT).show();
//                            }
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else {
            Toast.makeText(getApplicationContext(), "Please select date", Toast.LENGTH_SHORT).show();
        }

    }

    private Date getDate(String s,String format) {

        DateFormat formatter;
        Date date = null;
        formatter = new SimpleDateFormat(format);
        try {
            date = formatter.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;

    }

    public void onShowAttendance(View view) {
        callServices();
    }

    @Override
    public void onFail(Throwable throwable) {

        mSwipeRefreshLayout.setRefreshing(false);
        setEmptyData();
    }

    @Override
    public void onNetworkFailure() {
        mSwipeRefreshLayout.setRefreshing(false);
        setEmptyData();
    }

    @Override
    public void showLoading() {

        Utils.showProgress(this, "Loading");

    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
        Utils.hideProgress();

    }

    @Override
    public void setData(List<AttendanceList> list) {
        mSwipeRefreshLayout.setRefreshing(false);
        feeAdapter.updateList(list);
        error.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setEmptyData() {
        mSwipeRefreshLayout.setRefreshing(false);
        feeAdapter.updateList(Collections.EMPTY_LIST);
        error.setText(Error.noattendancedata);
        recyclerView.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);

    }

    @Override
    public void setEventList(List<EventObjects> eventList) {

    }

    @Override
    public void showLodingDialog() {

    }

    @Override
    public void hideLodingDialog() {

    }
}