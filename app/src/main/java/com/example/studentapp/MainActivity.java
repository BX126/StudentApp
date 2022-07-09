package com.example.studentapp;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {

    private List<Person> persons;
    private RecyclerView rv;

    Button btnDm, btnCe,btnAdd;
    DatabaseHelper myDB;
    FirebaseFirestore cloudDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDB = new DatabaseHelper(this);
        cloudDB = FirebaseFirestore.getInstance();

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
        viewByDM();

        if(isNetworkAvailable()){
            sendDataToCloud(cloudDB);
        }

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void sendDataToCloud(FirebaseFirestore cloudDB){

        Map<String,Object> dataList = new HashMap<>();
        for (Person person:persons) {
            dataList.put("Student Name",person.name);
            dataList.put("Training Group",person.tg);
            dataList.put("Priority", person.pr);
            dataList.put("Progress",person.pg);
            cloudDB.document("students/"+person.name)
                    .set(dataList)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "DocumentSnapshot added");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
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
