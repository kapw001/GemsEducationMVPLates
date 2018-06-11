package com.pappayaed.ui.parentprofile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pappayaed.App.App;
import com.pappayaed.R;
import com.pappayaed.RecyclerViewCommon.DividerItemDecoration;
import com.pappayaed.RecyclerViewCommon.RecyclerViewAdapter;
import com.pappayaed.base.BaseActivity;
import com.pappayaed.common.ActivityUtils;
import com.pappayaed.common.Utils;
import com.pappayaed.ui.showprofile.IProfileIntractor;
import com.pappayaed.ui.showprofile.IProfilePresenter;
import com.pappayaed.ui.showprofile.IProfileView;
import com.pappayaed.ui.showprofile.ProfileIntractorImpl;
import com.pappayaed.ui.showprofile.ProfilePresenterImpl;
import com.pappayaed.ui.showprofile.StudentList;
import com.pappayaed.ui.showprofile.UserDetails;
import com.pappayaed.ui.studentprofile.StudentProfileActivity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ParentActivity extends BaseActivity implements IProfileView, RecyclerViewAdapter.OnRecyclerViewItemClickListener<StudentList> {

    private static final String TAG = "ParentActivity";

    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.profile_name)
    TextView profileName;
    @BindView(R.id.profile_id)
    TextView profileId;
    @BindView(R.id.recyclerviewChild)
    RecyclerView recyclerviewChild;
    @BindView(R.id.recyclerviewProfile)
    RecyclerView recyclerviewProfile;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.childprofileview)
    LinearLayout childprofileview;

    private RecyclerViewAdapter<UserDetails> profileAdapter;
    private RecyclerViewAdapter<StudentList> childAdapter;

    private IProfilePresenter iProfilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setActionBarTitle("Profile", true);

        init();


        Map<String, String> profileCached = App.getApp().getProfileCached();

        IProfileIntractor iProfileIntractor = new ProfileIntractorImpl(dataSource, profileCached);

        iProfilePresenter = new ProfilePresenterImpl(this, iProfileIntractor);

        callServices();


    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//
//                super.onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @OnClick(R.id.back)
    public void onBack() {

        super.onBackPressed();

    }

    private void callServices() {

        if (iProfilePresenter != null) {
            showLoading();

            iProfilePresenter.displayProfile();
            iProfilePresenter.getAllProfile();
        } else Log.e(TAG, "Presenter not initialized........................................ ");
    }

    private void init() {

        recyclerviewChild.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewChild.setHasFixedSize(true);
        recyclerviewProfile.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewProfile.setHasFixedSize(true);

        childAdapter = new RecyclerViewAdapter<StudentList>(Collections.EMPTY_LIST, R.layout.custom_child_profile_row);
        profileAdapter = new RecyclerViewAdapter<UserDetails>(Collections.EMPTY_LIST, R.layout.custom_profile_row);

        recyclerviewProfile.setAdapter(profileAdapter);
        recyclerviewChild.setAdapter(childAdapter);

        recyclerviewChild.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));
        recyclerviewProfile.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));

        childAdapter.setOnItemClickListener(this);

    }

    @Override
    public void displayProfile(String name, String userType, String image) {

        profileName.setText(name);
        profileId.setText(userType);
        Bitmap imagebmp = Utils.decodeBitmap(this, image);

        profileImage.setImageBitmap(imagebmp);
        Utils.setBorderColor(profileImage);
    }

    @Override
    public void gotoStudentProfileActivity() {

    }

    @Override
    public void setData(List<UserDetails> list) {

        hideLoading();

    }

    @Override
    public void setData(Map<Object, Object> map) {
        hideLoading();

        List<UserDetails> profileModelList = (List<UserDetails>) map.get("profile");

        profileAdapter.updateData(profileModelList);

        List<StudentList> childList = (List<StudentList>) map.get("child");

        if (childList.size() > 0) {
            childAdapter.updateData(childList);
        } else {
            childprofileview.setVisibility(View.GONE);
        }


    }

    @Override
    public void showLoading() {

        Utils.showProgress(this, "Loading");

    }

    @Override
    public void hideLoading() {

        Utils.hideProgress();
    }

    @Override
    public void setError(String msg) {
        hideLoading();

    }

    @Override
    public void setEmptyData() {
        hideLoading();

    }

    @Override
    public void onItemClick(View view, StudentList studentList, int position) {


        Bundle bundle = new Bundle();
        bundle.putSerializable("studentlist", studentList);

        ActivityUtils.startActivity(this, StudentProfileActivity.class, bundle);

    }
}
