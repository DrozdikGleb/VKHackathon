package com.glebdrozdov.vkhackathon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListRooms extends AppCompatActivity {


    private List<Wings> persons;
    private RecyclerView rv;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_rooms);

        rv = (RecyclerView) findViewById(R.id.rv);

        btn = (Button) findViewById(R.id.button123);

        LinearLayoutManager llm = new LinearLayoutManager(this);

        rv.setLayoutManager(llm);


        persons = new ArrayList<>();
        persons.add(new Wings("Ann"));
        for (int i = 0; i < 100; i++) {
            persons.add(new Wings("item " + i));
        }
        persons.add(new Wings("Jack"));
        persons.add(new Wings("Antohhhhha"));
        initializeAdapter();
    }

    RecyclerAdapter adapter;

    private void initializeAdapter() {
        adapter = new RecyclerAdapter(persons);
        rv.setAdapter(adapter);
    }

    public void GOq(View view) {
        String s = "";
        for (String i : adapter.qInd) {
            s += adapter.q.get(i) + " ";
        }
        btn.setText(s);

    }
}
