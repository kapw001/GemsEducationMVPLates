package com.pappayaed.ui.studentprofile;

import com.pappayaed.R;
import com.pappayaed.RecyclerViewCommon.ViewLayout;

/**
 * Created by yasar on 11/4/18.
 */

public class Header implements ViewLayout {

    private String title;

    public Header(String title) {
        this.title = title;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.row_header;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
