package com.glebdrozdov.vkhackathon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RouterActivity extends Activity {
    Button button, button3, button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router);
        button = (Button) findViewById(R.id.button);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Intent intent = new Intent(RouterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button3:
                Intent intent3 = new Intent(RouterActivity.this, ListRooms.class);
                startActivity(intent3);
                break;
            case R.id.button4:
                Intent intent4 = new Intent(RouterActivity.this, RoomsActivity.class);
                startActivity(intent4);
                break;
        }
    }
}
