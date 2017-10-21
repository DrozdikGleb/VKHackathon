package com.glebdrozdov.vkhackathon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Илья on 21.10.2017.
 */

public class RoomsActivity extends Activity {
    final String[] catNames = new String[400];
    boolean[] toVisit = new boolean[400];
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        ListView listView = (ListView)findViewById(R.id.listView);
        btn = (Button)findViewById(R.id.btn);
// определяем массив типа String

        bar();

// используем адаптер данных
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, catNames);

        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView check = (CheckedTextView)view;
                check.setChecked(!check.isChecked());
                boolean click = !check.isChecked();
                check.setChecked(click);
                if (click) {
                    toVisit[position] = true;
                } else {
                    toVisit[position] = false;
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    void bar(){
        for(int i = 0; i < 400; i++){
            catNames[i] = String.valueOf(i + 1);
        }
    }
}
