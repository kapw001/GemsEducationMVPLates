package com.pappayaed.ui.homework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.pappayaed.R;

public class HomeWorkActivity extends AppCompatActivity {

    private static final String TAG = "HomeWorkActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work);

        getSupportActionBar().setTitle("Home Work");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (getIntent() != null) {

            long student_id = getIntent().getLongExtra("studentid", 0);

            HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);

            homeFragment.loadData(student_id);

        } else {

            Log.e(TAG, "onCreate: Fragment not found ");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
