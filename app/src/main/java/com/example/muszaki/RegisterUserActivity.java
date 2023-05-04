package com.example.muszaki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView banner, registerUser;
    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword, editTextPasswordAgain ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = findViewById(R.id.reg);
        banner.setOnClickListener(this);

        registerUser = findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextEmail = findViewById(R.id.emailReg);
        editTextPassword = findViewById(R.id.passwordReg);
        editTextPasswordAgain = findViewById(R.id.passwordRegAgain);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reg:
                startActivity(new Intent(this, MainActivity.class));
                finish();
            case R.id.registerUser:
                registerUser();

        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String passwordAgain = editTextPasswordAgain.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email hiányzik");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Nem létező email");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Jelszó hiányzik");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Nem elég hosszú jelszó");
            editTextPassword.requestFocus();
            return;
        }

        if (!password.equals(passwordAgain)) {
            editTextPasswordAgain.setError("Nem egyezik a két jelszó!");
            editTextPasswordAgain.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterUserActivity.this, "Sikeres regisztráció", Toast.LENGTH_LONG).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    finish();
                    //TODO:
                    //User elmentése
                } else {
                    Toast.makeText(RegisterUserActivity.this, "Sikertelen regisztráció", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}