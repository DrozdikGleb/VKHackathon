package com.glebdrozdov.vkhackathon;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PersonViewHolder>{
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_1, viewGroup, false);
        return new PersonViewHolder(v);
    }

    private List<Wings> persons;
    RecyclerAdapter(List<Wings> persons){
        this.persons = persons;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public HashMap<String, Integer> q = new HashMap<>();
    public HashSet<String> qInd = new HashSet<>();
    @Override
    public void onBindViewHolder(final PersonViewHolder holder, final int i) {
        holder.personName.setText(persons.get(i).name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id  = holder.personName.getText().toString();
                if(q.containsKey(id)){
                    q.remove(id);
                    qInd.remove(id);
                    holder.cv.setBackgroundColor(Color.parseColor("#ffffff"));
                }else{
                    q.put(id, i);
                    qInd.add(id);
                    holder.cv.setBackgroundColor(Color.parseColor("#ffecb3"));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.categories);
        }
    }
}