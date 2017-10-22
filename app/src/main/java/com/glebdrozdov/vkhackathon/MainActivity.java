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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.glebdrozdov.vkhackathon.Consts.ROOMS_COUNT;

public class MainActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private FloorsPageAdapter floorsPageAdapter;
    TextView tv;
    static int currentToExitRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            FileInputStream inputStream = openFileInput("coords.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        floorsPageAdapter = new FloorsPageAdapter(getSupportFragmentManager());
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
        d.getWindow().setLayout((6 * width) / 7, (4 * height) / 5);
        final NumberPicker np1 = (NumberPicker) d.findViewById(R.id.numberPicker1);
        final NumberPicker np2 = (NumberPicker) d.findViewById(R.id.numberPicker2);
        final NumberPicker np3 = (NumberPicker) d.findViewById(R.id.numberPicker3);
        Button b1 = (Button) d.findViewById(R.id.button1);
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
                int room = parseIntPicker(np1.getValue(), np2.getValue(), np3.getValue());
                currentToExitRoom = room;
                Log.i("room-",String.valueOf(currentToExitRoom));
                boolean[] toVisit = new boolean[ROOMS_COUNT];
                toVisit[currentToExitRoom - 1] = true;
                RoomsActivity.makeRoute(toVisit);
                Intent CategoryToMain = new Intent(MainActivity.this, MainActivity.class);
                startActivity(CategoryToMain);
                d.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {

    }

    public int parseIntPicker(int picker1, int picker2, int picker3) {
        int hundred = 0;
        int tens = 0;
        if (picker1 != 0) {
            hundred = picker1;
        }
        if (picker2 != 0) {
            tens = picker2;
        }
        int myValue = hundred * 100 + tens * 10 + picker3;

        return myValue;


    }


}
