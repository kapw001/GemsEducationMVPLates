package com.pappayaed.ui.showprofile;

/**
 * Created by yasar on 26/3/18.
 */

public class ProfilePresenterImpl implements IProfilePresenter, IProfileIntractor.OnFinishedListener, IProfileIntractor.OnProfileListener {


    private IProfileView iProfileView;

    private IProfileIntractor iProfileIntractor;

    public ProfilePresenterImpl(IProfileView iProfileView, IProfileIntractor iProfileIntractor) {

        this.iProfileView = iProfileView;
        this.iProfileIntractor = iProfileIntractor;

    }

    @Override
    public void displayProfile() {

        iProfileIntractor.getProfile(this);

    }

    @Override
    public void getAllProfile() {


        iProfileIntractor.getAllProfile(this);

    }

    @Override
    public void onSuccss(ProfileAndUserDetails profileAndUserDetails) {

        iProfileView.setData(profileAndUserDetails.getMap());

    }

    @Override
    public void onFail(Throwable throwable) {

        iProfileView.setError(throwable.getMessage());
    }

    @Override
    public void onNetworkFailure() {

        iProfileView.setEmptyData();

    }

    @Override
    public void displayProfile(String profileName, String userType, String profileImage) {

        iProfileView.displayProfile(profileName, userType, profileImage);

    }
}
