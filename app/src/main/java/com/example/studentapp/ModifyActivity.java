package com.example.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class ModifyActivity extends AppCompatActivity {

    Button btnInsertData, btnDeleteData, btnUpdateData, btnReadData;
    EditText name;
    RadioGroup pg,pr;
    String tg;

    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myDB = new DatabaseHelper(this);  // created object of DatabaseHelper class
        //myDB.getWritableDatabase(); // for checking db is created or not.

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

                boolean isInserted = myDB.insertData(name.getText().toString(),tg,pgb,pfb);

                // Show toast when data inserted successfully
                if(isInserted){
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
//                Cursor cur = myDB.getAllData();
//                if(cur.getCount() == 0){
//                    showMessage("Error","No Data Found");
//                    return;
//                }
//                List<Person> persons = new ArrayList<>();
//
//                while (cur.moveToNext()){
//                    persons.add(new Person(cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4)));
//                }
//
//                myIntent.putExtra("data", (Serializable) persons);
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
                selectedItem= pr.getCheckedRadioButtonId();
                radioButton= findViewById (selectedItem);
                String pfb = radioButton.getText().toString();

                boolean isUpdated = myDB.updateData(name.getText().toString(), tg, pgb,pfb);

                if(isUpdated){
                    Toast.makeText(ModifyActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ModifyActivity.this, "Data Not Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });



        btnDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer isDeleted  = myDB.deleteData(name.getText().toString());

                if(isDeleted > 0){
                    Toast.makeText(ModifyActivity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ModifyActivity.this, "Data Not Deleted", Toast.LENGTH_SHORT).show();
                }
                name.setText("");
            }
        });

        Intent intent = new Intent(getIntent());
        String sname = intent.getStringExtra("name");
        if(!sname.isEmpty()){
            name.setText(sname);
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

