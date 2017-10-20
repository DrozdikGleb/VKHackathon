package com.glebdrozdov.vkhackathon;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private FloorsPageAdapter floorsPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floorsPageAdapter = new FloorsPageAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);
        TabLayout floorTabLayout = (TabLayout) findViewById(R.id.tabs);
        floorTabLayout.setupWithViewPager(viewPager);
        floorTabLayout.getTabAt(0).setText("1Floor");
        floorTabLayout.getTabAt(1).setText("2Floor");
        floorTabLayout.getTabAt(2).setText("3Floor");

    }

    private void setupViewPager(ViewPager viewPager) {
        FloorsPageAdapter adapter = new FloorsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FirstFloorFragment());
        adapter.addFragment(new SecondFloorFragment());
        adapter.addFragment(new ThirdFloorFragment());
        viewPager.setAdapter(adapter);
    }
}
