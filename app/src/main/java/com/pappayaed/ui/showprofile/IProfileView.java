package com.pappayaed.ui.showprofile;

import java.util.List;
import java.util.Map;

/**
 * Created by yasar on 26/3/18.
 */

public interface IProfileView {


    void displayProfile(String profileName, String userType, String profileImage);

    void gotoStudentProfileActivity();

    void setData(List<UserDetails> list);

    void setData(Map<Object, Object> map);

    void setError(String msg);

    void setEmptyData();

}
