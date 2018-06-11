package com.pappayaed.ui.homework;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pappayaed.R;
import com.pappayaed.adapter.SubmissionListAdapter;
import com.pappayaed.base.BaseFragment;
import com.pappayaed.data.model.AssignmentList;
import com.pappayaed.data.model.AssignmentSubLine;


import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yasar on 2/5/17.
 */

public class HomeFragment extends BaseFragment implements SubmissionListAdapter.RecyclerAdapterPositionClicked, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private SubmissionListAdapter leaveListAdapter;
    private FloatingActionButton floatingActionButton;

    private ArrayList<AssignmentSubLine> list;
    private static final String TAG = "LeaveFragment";
    private TextView error;

    private View view;
    private boolean add = false;
    private Paint p = new Paint();
    private int edit_position;
    private AssignmentList assignmentList;
    //    private AlertDialog.Builder alertDialog;
//    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_fragment1, container, false);

        list = new ArrayList<>();

        error = (TextView) view.findViewById(R.id.error);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);


        leaveListAdapter = new SubmissionListAdapter(this, list, recyclerView);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(leaveListAdapter);
        error.setVisibility(View.GONE);
        onRefresh();
        return view;
    }

    public static HomeFragment getInstance(AssignmentList assignmentList) {
        HomeFragment submissionFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("assignmentlist", assignmentList);
        submissionFragment.setArguments(bundle);
        return submissionFragment;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (leaveListAdapter != null) {
            leaveListAdapter.saveStates(outState);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (leaveListAdapter != null) {
            leaveListAdapter.restoreStates(savedInstanceState);
        }

    }

    @Override
    public void position(int pos, View view) {

    }


    public void loadData(long student_id) {


        String s = dataSource.getHomeWork();




    }

    @Override
    public void onRefresh() {

        List<AssignmentSubLine> subLinesList = new ArrayList<>();

        AssignmentSubLine subLine = new AssignmentSubLine();

        subLine.setDescription("Write about APJ Abdul Kalam ");
        subLine.setGrade("Grade I");
        subLine.setSubject("English");
        subLine.setSubmissiondate("29/03/2018");

        subLinesList.add(subLine);

        list.clear();
        list.addAll(subLinesList);
        leaveListAdapter.updateList(subLinesList);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
