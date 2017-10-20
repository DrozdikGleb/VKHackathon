package com.glebdrozdov.vkhackathon;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        tv = (TextView)findViewById(R.id.roomNumber);
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);
        TabLayout floorTabLayout = (TabLayout) findViewById(R.id.tabs);
        floorTabLayout.setupWithViewPager(viewPager);
        floorTabLayout.getTabAt(0).setText("1Floor");
        floorTabLayout.getTabAt(1).setText("2Floor");
        floorTabLayout.getTabAt(2).setText("3Floor");
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.what_route:
                        showMyDialog();
                        item.setChecked(true);
                        break;
                    case R.id.ic_want_to_visit:
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
        d.getWindow().setLayout((6 * width)/7, (4 * height)/5);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(400);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText(String.valueOf(np.getValue()));
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
