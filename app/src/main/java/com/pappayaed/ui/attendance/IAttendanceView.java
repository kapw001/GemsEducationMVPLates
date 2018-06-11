package com.pappayaed.ui.attendance;

import com.pappayaed.base.BaseView;
import com.pappayaed.data.model.AttendanceList;

import java.util.List;

import calendar.android.com.customcalendar.EventObjects;

/**
 * Created by yasar on 27/3/18.
 */

public interface IAttendanceView extends BaseView {

    void setData(List<AttendanceList> list);

    void setEmptyData();

    void setEventList(List<EventObjects> eventList);

    void showLodingDialog();

    void hideLodingDialog();

}
