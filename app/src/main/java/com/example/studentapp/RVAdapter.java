package com.example.studentapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView personName;
        TextView personTG;
        ImageView personPG;
        ImageView personPR;
        ImageView user;
        View cardV;

        PersonViewHolder(View itemView) {
            super(itemView);
            cardV = itemView;
            cv = (CardView)itemView.findViewById(R.id.cv);
            user = (ImageView) itemView.findViewById(R.id.person_photo);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personTG = (TextView)itemView.findViewById(R.id.person_tg);
            personPG = (ImageView) itemView.findViewById(R.id.imgPg);
            personPR = (ImageView) itemView.findViewById(R.id.imgPR);
        }
    }

    List<Person> persons;

    RVAdapter(List<Person> persons){
        this.persons = persons;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.personName.setText(persons.get(i).name);
        personViewHolder.personTG.setText(persons.get(i).tg);

        if(persons.get(i).pg.equals("Done")){
            personViewHolder.personPG.setImageResource(R.drawable.finish);
        }else if(persons.get(i).pg.equals("Stuck")){
            personViewHolder.personPG.setImageResource(R.drawable.stuck);
        }else if(persons.get(i).pg.equals("Working on it")){
            personViewHolder.personPG.setImageResource(R.drawable.working);
        }else if(persons.get(i).pg.equals("Waiting Review")){
            personViewHolder.personPG.setImageResource(R.drawable.waiting);
        }

        if(persons.get(i).pr.equals("High")){
            personViewHolder.personPR.setImageResource(R.drawable.high);
        }else if(persons.get(i).pr.equals("Mid")){
            personViewHolder.personPR.setImageResource(R.drawable.mid);
        }else if(persons.get(i).pr.equals("Low")){
            personViewHolder.personPR.setImageResource(R.drawable.low);
        }

        final String Sname = persons.get(i).name;
        final String Stg = persons.get(i).tg;
        final String id = Integer.toString(persons.get(i).id);
        personViewHolder.cardV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ModifyActivity.class);
                intent.putExtra("name",Sname);
                intent.putExtra("tg",Stg);
                intent.putExtra("id",id);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }
}
