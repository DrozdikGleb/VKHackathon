package com.glebdrozdov.vkhackathon;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private FloorsPageAdapter floorsPageAdapter;
    TextView tv;
    static Dialog d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floorsPageAdapter = new FloorsPageAdapter(getSupportFragmentManager());
        tv = (TextView) findViewById(R.id.roomNumber);
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);
        TabLayout floorTabLayout = (TabLayout) findViewById(R.id.tabs);
        floorTabLayout.setupWithViewPager(viewPager);
        floorTabLayout.getTabAt(0).setText("1Floor");
        floorTabLayout.getTabAt(1).setText("2Floor");
        floorTabLayout.getTabAt(2).setText("3Floor");
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.what_route:
                        showMyDialog();
                        item.setChecked(true);
                        break;
                    case R.id.ic_want_to_visit:
                        Intent mainToRouteActivity = new Intent(MainActivity.this, RouterActivity.class);
                        startActivity(mainToRouteActivity);
                        item.setChecked(true);
                        break;

                }
                return false;
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        FloorsPageAdapter adapter = new FloorsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FirstFloorFragment());
        adapter.addFragment(new SecondFloorFragment());
        adapter.addFragment(new ThirdFloorFragment());
        viewPager.setAdapter(adapter);
    }

    public void showMyDialog() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        d.getWindow().setLayout((5 * width) / 7, (3 * height) / 5);
        final NumberPicker np1 = (NumberPicker) d.findViewById(R.id.numberPicker1);
        final NumberPicker np2 = (NumberPicker) d.findViewById(R.id.numberPicker2);
        final NumberPicker np3 = (NumberPicker) d.findViewById(R.id.numberPicker3);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        np1.setMaxValue(4);
        np1.setMinValue(0);
        np1.setWrapSelectorWheel(false);
        np1.setOnValueChangedListener(this);
        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if (i1 == 4) {
                    np2.setValue(0);
                    np2.setEnabled(false);
                    np3.setValue(0);
                    np3.setEnabled(false);
                } else {
                    np2.setEnabled(true);
                    np3.setEnabled(true);
                }
            }
        });
        np2.setMaxValue(9);
        np2.setMinValue(0);
        np2.setWrapSelectorWheel(false);
        np2.setOnValueChangedListener(this);
        np3.setMaxValue(9);
        np3.setMinValue(0);
        np3.setWrapSelectorWheel(false);
        np3.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String room = String.valueOf(np1.getValue()) + String.valueOf(np2.getValue()) + String.valueOf(np3.getValue());
                tv.setText(room);
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {

    }
}
