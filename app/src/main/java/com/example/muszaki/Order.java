package com.example.muszaki;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.List;

public class Order extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private OrderListAdapter mAdapter;

    private TextView emptyOrderTextView;
    private RecyclerView orderRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emptyOrderTextView = findViewById(R.id.emptyOrderTextView);
        orderRecyclerView = findViewById(R.id.order_list);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new OrderListAdapter(this, new ArrayList<OrderItem>());
        orderRecyclerView.setAdapter(mAdapter);

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("Shop")
                .whereEqualTo("user_id", userId) // itt szűrünk az aktuális felhasználó UID-jére
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<OrderItem> orderItems = new ArrayList<>();
                        int orderNumber = 1;
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String nev = documentSnapshot.getString("nev");
                            String ar = documentSnapshot.getString("ar");
                            orderItems.add(new OrderItem(" Rendelés - ", nev, ar));
                            orderNumber++;
                        }

                        if (orderItems.isEmpty()) {
                            emptyOrderTextView.setVisibility(View.VISIBLE);
                            Toast.makeText(Order.this, "Még nincs rendelés leadva.", Toast.LENGTH_SHORT).show();
                        } else {
                            emptyOrderTextView.setVisibility(View.GONE);
                            OrderListAdapter adapter = new OrderListAdapter(Order.this, orderItems);
                            orderRecyclerView.setAdapter(adapter);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Order.this, "Hiba történt a rendelések lekérésekor.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}