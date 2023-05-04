package com.example.muszaki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText editTextEmail, editTextPassword;
    private Button signIn, toShop;
    private FirebaseAuth mAuth;
    private boolean loggedIn = false;
    private static final int BACK_PRESS_TIME_INTERVAL = 2000;
    private long backPressTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.sample_anim);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            loggedIn = savedInstanceState.getBoolean("loggedIn", false);
        }

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.signin);
        signIn.setOnClickListener(this);

        toShop = (Button) findViewById(R.id.toshop);
        toShop.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextEmail.setOnClickListener(this);

        editTextPassword = (EditText) findViewById(R.id.password);
        editTextPassword.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();


        register.startAnimation(animation);
        signIn.startAnimation(animation);
        toShop.startAnimation(animation);
        editTextPassword.startAnimation(animation2);
        editTextEmail.startAnimation(animation2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Sikeres bejelentkezés!", "bejelentkezés", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("loggedIn", loggedIn);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        loggedIn = savedInstanceState.getBoolean("loggedIn", false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                startActivity(new Intent(this, RegisterUserActivity.class));
                break; // Hiányzott a break utasítás itt
            case R.id.signin:
                userLogin();
                break;
            case R.id.toshop:
                if (loggedIn) {
                    startActivity(new Intent(this, ActivityShop.class));
                } else {
                    Toast.makeText(this, "Először jelentkezz be!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHome:
                // Az Option 1 menüelemre kattintás eseménykezelése
                Toast.makeText(this, "Option 1 kiválasztva", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menuKijelentkezes:
                if (loggedIn) {
                    startActivity(new Intent(this, ActivityShop.class));
                } else {
                    Toast.makeText(this, "Először jelentkezz be!", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void userLogin() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("E-mail hiányzik");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Jelszó hiányzik");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    loggedIn = true;

                    String email = editTextEmail.getText().toString().trim();
                    int atIndex = email.indexOf('@');
                    String username = email.substring(0, atIndex);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "Sikeres bejelentkezés!");
                    builder.setContentTitle("Bejelentkezés");
                    builder.setContentText("Bejelentkeztél a Shopba! \nÜdvözlünk: " + username + " !");
                    builder.setAutoCancel(true);
                    builder.setSmallIcon(R.drawable.ic_baseline_add_circle_outline_24);
                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    managerCompat.notify(1, builder.build());
                    startActivity(new Intent(MainActivity.this, LogoutActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Sikeretelen bejelentkezés", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onBackPressed() {
        if (backPressTime + BACK_PRESS_TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            Toast.makeText(getApplicationContext(), "Biztosan kilépsz?", Toast.LENGTH_SHORT).show();
        }
        backPressTime = System.currentTimeMillis();
    }
}
