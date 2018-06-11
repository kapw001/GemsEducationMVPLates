package com.pappayaed.ui.attendance;

import android.graphics.Color;
import android.view.View;

import com.pappayaed.common.NullCheckUtils;
import com.pappayaed.common.Utils;
import com.pappayaed.data.model.AttendanceList;
import com.pappayaed.data.model.Circular;
import com.pappayaed.data.model.ResultResponse;
import com.pappayaed.ui.showprofile.StudentList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import calendar.android.com.customcalendar.CalendarUtils;
import calendar.android.com.customcalendar.EventObjects;

/**
 * Created by yasar on 27/3/18.
 */

public class AttendancePresenterImpl implements IAttendancePresenter, IAttendanceIntractor.OnFinishedListener {


    private IAttendanceView iAttendanceView;
    private IAttendanceIntractor iAttendanceIntractor;
    private ResultResponse resultResponse;
    private StudentList studentList;

    public AttendancePresenterImpl(IAttendanceView iAttendanceView, IAttendanceIntractor iAttendanceIntractor) {
        this.iAttendanceView = iAttendanceView;
        this.iAttendanceIntractor = iAttendanceIntractor;
    }


    @Override
    public void getAttendanceList(long student_ID, String start_date, String end_date) {

        if (this.resultResponse == null) {

            iAttendanceView.showLodingDialog();
            iAttendanceIntractor.getStudentFeeList(student_ID, start_date, end_date, this);
        } else {
            onSuccss(this.resultResponse);
        }

    }

    @Override
    public void getAttendanceList(StudentList studentList, String start_date, String end_date) {
        this.studentList = studentList;

        if (this.resultResponse == null) {

            iAttendanceView.showLodingDialog();
            iAttendanceIntractor.getStudentFeeList(studentList.getId(), start_date, end_date, this);
        } else {
            onSuccss(this.resultResponse);
        }
    }

    @Override
    public void onItemClick(View view, int position, Object o) {

        if (o == null) {

            if (!NullCheckUtils.isEmpty(resultResponse) && !NullCheckUtils.isEmpty(resultResponse.getResult())) {

                if (resultResponse != null && resultResponse.getResult().getAttendanceList() != null && resultResponse.getResult().getAttendanceList().size() > 0) {


                    List<EventObjects> eventObjectsList = new ArrayList<>();

                    List<AttendanceList> list = resultResponse.getResult().getAttendanceList();
//                list.get(0).setPresentMorning(false);

                    for (int i = 0; i < list.size(); i++) {

                        AttendanceList attendanceList = list.get(i);
                        if (attendanceList.getPresentMorning()) {
                            EventObjects objects = new EventObjects(attendanceList.getRemark(), CalendarUtils.convertStringToDate1(attendanceList.getAttendanceDate()), studentList.getColor());
                            eventObjectsList.add(objects);
                        } else {
                            EventObjects objects = new EventObjects(attendanceList.getRemark(), CalendarUtils.convertStringToDate1(attendanceList.getAttendanceDate()), Color.parseColor("#F06363"));
                            eventObjectsList.add(objects);
                            list.get(i).setColor(Color.parseColor("#F06363"));

                        }
                    }

                    iAttendanceView.setEventList(eventObjectsList);

                    iAttendanceView.setData(list);


                }
            } else iAttendanceView.onFail(new Throwable("Something went wrong"));
        } else {

            Date mDate = (Date) o;
            if (!NullCheckUtils.isEmpty(resultResponse) && !NullCheckUtils.isEmpty(resultResponse.getResult())) {
                if (resultResponse != null && resultResponse.getResult().getAttendanceList() != null && resultResponse.getResult().getAttendanceList().size() > 0) {

                    List<AttendanceList> list = Utils.getParticularDateEventsForAttendance(mDate, resultResponse.getResult().getAttendanceList());

                    if (list.size() > 0)
                        iAttendanceView.setData(list);
                    else iAttendanceView.setData(Collections.EMPTY_LIST);
                    ;


                }
            } else iAttendanceView.onFail(new Throwable("Something went wrong"));


        }

    }

    @Override
    public void onSuccss(ResultResponse response) {

        this.resultResponse = response;

        iAttendanceView.hideLodingDialog();

        if (!NullCheckUtils.isEmpty(resultResponse) && !NullCheckUtils.isEmpty(resultResponse.getResult())) {

            if (response.getResult().getAttendanceList() != null && !response.getResult().getAttendanceList().isEmpty()) {
                List<AttendanceList> list = response.getResult().getAttendanceList();
                for (int i = 0; i < list.size(); i++) {

                    list.get(i).setColor(studentList.getColor());

                }

                iAttendanceView.setData(list);

                onItemClick(null, 0, null);

            } else iAttendanceView.setEmptyData();
        } else iAttendanceView.setEmptyData();

    }

    @Override
    public void onFail(Throwable throwable) {

        iAttendanceView.hideLodingDialog();
        iAttendanceView.setEmptyData();
        iAttendanceView.onFail(throwable);

    }

    @Override
    public void onNetworkFailure() {

        iAttendanceView.hideLodingDialog();
        iAttendanceView.setEmptyData();
        iAttendanceView.onNetworkFailure();

    }
}
