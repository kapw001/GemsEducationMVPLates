package com.pappayaed.ui.attendance;

import android.view.View;

import com.pappayaed.ui.showprofile.StudentList;

/**
 * Created by yasar on 27/3/18.
 */

public interface IAttendancePresenter {


    void getAttendanceList(long student_ID, String start_date, String end_date);


    void getAttendanceList(StudentList studentList, String start_date, String end_date);


    void onItemClick(View view, int position, Object o);
}
