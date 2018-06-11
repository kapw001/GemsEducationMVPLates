package com.pappayaed.ui.attendance;

import android.util.Log;

import com.google.gson.Gson;
import com.pappayaed.common.Utils;
import com.pappayaed.data.DataSource;
import com.pappayaed.data.helper.L;
import com.pappayaed.data.listener.DataListener;
import com.pappayaed.data.model.ResultResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yasar on 27/3/18.
 */

public class AttendanceIntractorImpl implements IAttendanceIntractor {

    //    {"params":{"password":"a","user_type":"Parent","from_date":"2018-1-1","to_date":"2018-04-04","student_id":9244,"login":"ram"}}
    private DataSource dataSource;


    private Map<Long, ResultResponse> cached = new HashMap<>();

    public AttendanceIntractorImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void getStudentFeeList(final long student_id, String start_date, String end_date, final OnFinishedListener onFinishedListener) {

        if (cached.get(student_id) != null) {

            ResultResponse response = cached.get(student_id);

            onFinishedListener.onSuccss(response);

            return;

        }


        start_date = start_date.length() > 0 ? Utils.convertDateToString(start_date, "dd/MM/yyyy", "yyyy-MM-dd") : "";
        end_date = end_date.length() > 0 ? Utils.convertDateToString(end_date, "dd/MM/yyyy", "yyyy-MM-dd") : "";

        Map<Object, Object> json = new HashMap<>();
        Map<Object, Object> params = new HashMap<>();
        params.put("login", dataSource.getEmailOrUsername());
        params.put("password", dataSource.getPassword());
        params.put("user_type", dataSource.getUserType());
        params.put("student_id", student_id);
        params.put("from_date", start_date);
        params.put("to_date", end_date);

        json.put("params", params);

        final String js = new JSONObject(json).toString();

        L.loge("params :  " + js);

        dataSource.getStudentAtttendanceList(js, new DataListener() {
            @Override
            public void onSuccess(Object object) {

                ResultResponse response = (ResultResponse) object;

                cached.put(student_id, response);
                onFinishedListener.onSuccss(response);


                Gson gson = new Gson();

                Log.e("Response  ", "onSuccess: " + gson.toJson(response).toString());

//                if (response.getResult().getAttendanceList() != null && !response.getResult().getAttendanceList().isEmpty()) {
//                    List<AttendanceList> list = response.getResult().getAttendanceList();
//                    onFinishedListener.onSuccss(list);
//                }


            }

            @Override
            public void onFail(Throwable throwable) {
                cached.put(student_id, null);
                onFinishedListener.onFail(throwable);
            }

            @Override
            public void onNetworkFailure() {

                cached.put(student_id, null);
                onFinishedListener.onNetworkFailure();

            }
        });


    }
}
