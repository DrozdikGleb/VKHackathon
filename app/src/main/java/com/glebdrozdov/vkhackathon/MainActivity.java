package com.glebdrozdov.vkhackathon;

import android.app.Dialog;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private FloorsPageAdapter floorsPageAdapter;
    TextView tv;
    static Dialog d;
    String myJSON;
    int times[] = new int[401];
    int people[] = new int[401];

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
        d.getWindow().setLayout((6 * width) / 7, (4 * height) / 5);
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

    private void getData() {
        class dataTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                Response response = null;

                try {
                    OkHttpClient client = new OkHttpClient();

                    FormBody.Builder formBuilder = new FormBody.Builder()
                            .add("null", "null");
                    RequestBody formBody = formBuilder.build();
                    Request request = new Request.Builder()
                            .url("https://telegrambotdrozd.000webhostapp.com/data.php")
                            .post(formBody)
                            .build();

                    response = client.newCall(request).execute();
                    return response.body().string();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                String s = null;
                try {
                    s = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                if (!s.trim().equals("{\"rooms\":[]}") && !s.trim().equals(null)) {
                    myJSON = s.trim();
                    parseList();
                    //tv1.setText("Data");
                } else {

                }
            }

            @Override
            protected void onCancelled() {

            }
        }
        dataTask DataTask = new dataTask();
        DataTask.execute();
    }

    private void parseList() {
        try {
            if (myJSON.contains("{")) {
                JSONObject jsonObj = new JSONObject(myJSON.substring(myJSON.indexOf("{"), myJSON.lastIndexOf("}") + 1));
                JSONArray p = jsonObj.getJSONArray("rooms");
                String s = "";
                for (int i = 0; i < p.length(); i++) {
                    JSONObject c = p.getJSONObject(i);
                    String time = c.getString("time");
                    String n = c.getString("people");
                    times[i] = Integer.parseInt(time);
                    people[i] = Integer.parseInt(n);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
