package com.pappayaed.ui.showprofile;

/**
 * Created by yasar on 26/3/18.
 */

public interface IProfileIntractor {

    interface OnProfileListener {

        void displayProfile(String profileName, String userType, String profileImage);
    }

    interface OnFinishedListener {

        void onSuccss(ProfileAndUserDetails profileAndUserDetails);

        void onFail(Throwable throwable);

        void onNetworkFailure();


    }


    void getProfile(OnProfileListener onProfileListener);

    void getAllProfile(OnFinishedListener onFinishedListener);

}
