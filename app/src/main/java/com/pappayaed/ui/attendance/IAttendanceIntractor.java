package com.pappayaed.ui.attendance;

import com.pappayaed.data.model.ResultResponse;

/**
 * Created by yasar on 27/3/18.
 */

public interface IAttendanceIntractor {


    interface OnFinishedListener {

        void onSuccss(ResultResponse response);

        void onFail(Throwable throwable);

        void onNetworkFailure();
    }

    void getStudentFeeList(long student_id, String start_date, String end_date, final OnFinishedListener onFinishedListener);

}
