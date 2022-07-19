package com.example.studentapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Date;


public class ModifyActivity extends AppCompatActivity {

    Button btnInsertData, btnDeleteData, btnUpdateData, btnReadData;
    EditText name;
    RadioGroup pg,pr;
    String tg;
    int id;

    DatabaseHelper myDB;
    FirebaseFirestore cloudDB;
    boolean spinnerTouched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);
        cloudDB = FirebaseFirestore.getInstance();

        btnInsertData = findViewById(R.id.btnInsertData);
        btnDeleteData = findViewById(R.id.btnDeleteData);
        btnUpdateData = findViewById(R.id.btnUpdateData);
        btnReadData = findViewById(R.id.btnReadData);

        final Spinner spinner = (Spinner) findViewById(R.id.textTG);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tgroup, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        tg = String.valueOf(adapterView.getItemAtPosition(i));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }

                });
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    spinnerTouched = true;
                }

                return false;
            }
        });
        name = findViewById(R.id.textName);
        pg = findViewById(R.id.textProgress);
        pr = findViewById(R.id.textPR);

        btnInsertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Insert data into database

                int selectedItem= pg.getCheckedRadioButtonId();
                RadioButton radioButton= findViewById (selectedItem);
                String pgb = radioButton.getText().toString();
                selectedItem= pr.getCheckedRadioButtonId();
                radioButton= findViewById (selectedItem);
                String pfb = radioButton.getText().toString();

                String time = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(new Date());
                boolean isInserted = myDB.insertData(name.getText().toString(),tg,pgb,pfb);
                boolean isInsertedR = myDB.insertRecord(name.getText().toString(),time,"1","0",tg,"0",pgb,"0",pfb,"0");

                // Show toast when data inserted successfully
                if(isInserted && isInsertedR){
                    Toast.makeText(ModifyActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ModifyActivity.this, "Data Not Inserted", Toast.LENGTH_SHORT).show();
                }

                // After inserting make edit box empty
                name.setText("");
                pg.clearCheck();
                pr.clearCheck();
            }
        });


        btnReadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ModifyActivity.this, MainActivity.class);
                ModifyActivity.this.startActivity(myIntent);
            }
        });


        btnUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check emptiness of edit boxes
                if(name.getText().toString().isEmpty() ){
                    showMessage("Error","Please fill the all fields to Updating");
                    return;
                }

                int selectedItem= pg.getCheckedRadioButtonId();
                RadioButton radioButton= findViewById (selectedItem);
                String pgb = radioButton.getText().toString();

                if(pgb.isEmpty() ){
                    showMessage("Error","Please fill the all fields to Updating");
                    return;
                }

                selectedItem= pr.getCheckedRadioButtonId();
                radioButton= findViewById (selectedItem);
                String pfb = radioButton.getText().toString();

                if(pfb.isEmpty() ){
                    showMessage("Error","Please fill the all fields to Updating");
                    return;
                }

                boolean isUpdatedR;
                String time = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(new Date());
                if(spinnerTouched){
                    isUpdatedR = myDB.insertRecord(name.getText().toString(),time,"0","0",tg,"1",pgb,"1",pfb,"1");
                }else{
                    isUpdatedR = myDB.insertRecord(name.getText().toString(),time,"0","0",tg,"0",pgb,"1",pfb,"1");
                }


                boolean isUpdated = myDB.updateData(id,name.getText().toString(), tg, pgb,pfb);


                if(isUpdated && isUpdatedR ){
                    Toast.makeText(ModifyActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ModifyActivity.this, "Data Not Updated", Toast.LENGTH_SHORT).show();
                }

            }
        });



        btnDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer isDeleted  = myDB.deleteData(id);
                cloudDB.document("students/"+name.getText().toString())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });

                String time = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(new Date());
                boolean isInsertedR = myDB.insertRecord(name.getText().toString(),time,"0","1",null,"0",null,"0",null,"0");

                // Show toast when data inserted successfully
                if(isDeleted > 0 && isInsertedR){
                    Toast.makeText(ModifyActivity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ModifyActivity.this, "Data Not Deleted", Toast.LENGTH_SHORT).show();
                }
                name.setText("");
            }
        });

        Intent intent = new Intent(getIntent());
        String sname = intent.getStringExtra("name");
        String stg = intent.getStringExtra("tg");
        String sid = intent.getStringExtra("id");
        if(!sid.isEmpty()){
            id = Integer.parseInt(intent.getStringExtra("id"));
        }
        if(!sname.isEmpty()){
            name.setText(sname);
        }
        if(!stg.isEmpty()){
            if(stg.equals("Dressmaking")){
                spinner.setSelection(0);
            }else{
                spinner.setSelection(1);
            }

        }

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

