package com.example.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;


public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText name;

    DatabaseHelper myDB;
    FirebaseFirestore cloudDB;
    boolean spinnerTouched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        myDB = new DatabaseHelper(this);
        cloudDB = FirebaseFirestore.getInstance();

        btnLogin = findViewById(R.id.Login);
        name = findViewById(R.id.textUserName);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                myIntent.putExtra("User", name.getText().toString());
                LoginActivity.this.startActivity(myIntent);
            }
        });
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

