package com.glebdrozdov.vkhackathon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

public class RouterActivity extends AppCompatActivity {
    Button button, button3, button4;
    CardView cv1, cv2, cv3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router);
        cv1 = (CardView)findViewById(R.id.cv1);
        cv2 = (CardView)findViewById(R.id.cv2);
        cv3 = (CardView)findViewById(R.id.cv3);
        cv1.setCardBackgroundColor(Color.parseColor("#FFEBCD"));
        cv2.setCardBackgroundColor(Color.parseColor("#FFE4C4"));
        cv3.setCardBackgroundColor(Color.parseColor("#FFDEAD"));
        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RouterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(RouterActivity.this, ListRooms.class);
                startActivity(intent3);
            }
        });
        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(RouterActivity.this, RoomsActivity.class);
                startActivity(intent4);
            }
        });
    }


}
