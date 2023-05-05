package com.example.muszaki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends AppCompatActivity {

    private Button logout, kezdo, order;
    private static final int BACK_PRESS_TIME_INTERVAL = 2000;
    private long backPressTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogoutActivity.this, ActivityShop.class));
            }
        });

        kezdo = (Button) findViewById(R.id.kezdo);
        kezdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutConfirmationDialog();
            }
        });

        order = (Button) findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogoutActivity.this, Order.class));
            }
        });
    }

    public void onBackPressed() {
        if (backPressTime + BACK_PRESS_TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            FirebaseAuth.getInstance().signOut();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Biztosan visszalépsz?", Toast.LENGTH_SHORT).show();
        }
        backPressTime = System.currentTimeMillis();
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kijelentkezés");
        builder.setMessage("Biztosan ki szeretnél jelentkezni?");
        builder.setPositiveButton("Igen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LogoutActivity.this, MainActivity.class));
            }
        });
        builder.setNegativeButton("Mégse", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}





