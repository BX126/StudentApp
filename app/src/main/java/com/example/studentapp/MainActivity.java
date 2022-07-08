package com.example.studentapp;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private List<Person> persons;
    private RecyclerView rv;

    Button btnDm, btnCe,btnAdd;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDB = new DatabaseHelper(this);


        setContentView(R.layout.recyclerview_activity);

        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        btnDm = findViewById(R.id.dmP);
        btnCe = findViewById(R.id.ceP);
        btnAdd = findViewById(R.id.Add);

        btnDm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewByDM();
            }
        });
        btnCe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewByCE();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, ModifyActivity.class);
                myIntent.putExtra("name","");
                MainActivity.this.startActivity(myIntent);
            }
        });


        initializeData();
//        initializeAdapter();
        viewByDM();

    }

    private void initializeData(){
//        Intent i = getIntent();
//        persons = (List<Person>) i.getSerializableExtra("data");

        Cursor cur = myDB.getAllData();
        if(cur.getCount() == 0){
            showMessage("Error","No Data Found");
            return;
        }
        persons = new ArrayList<>();

        while (cur.moveToNext()){
            persons.add(new Person(cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4)));
        }

    }

    private void viewByDM(){
        if(persons == null){
            return;
        }
        List<Person> temp = new ArrayList<>();
        for (Person person:persons) {
            if (person.tg.equals("Dressmaking")){
                temp.add(person);
            }
        }
        if(temp.size() == 0){
            showMessage("Error","No dressmaking student");
        }
        initializeAdapter(temp);

    }

    private void viewByCE(){
        if(persons == null){
            return;
        }
        List<Person> temp = new ArrayList<>();
        for (Person person:persons) {
            if (person.tg.equals("Civic Education")){
                temp.add(person);
            }
        }
        if(temp.size() == 0){
            showMessage("Error","No civic education student");
        }
        initializeAdapter(temp);

    }

    private void initializeAdapter(List<Person> input){
        RVAdapter adapter = new RVAdapter(input);
        rv.setAdapter(adapter);
    }

    // Method to show database table
    private void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}
