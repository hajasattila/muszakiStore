package com.example.muszaki;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends FirebaseRecyclerAdapter<Model,MyAdapter.myviewholder> {
    public MyAdapter(@NonNull FirebaseRecyclerOptions<Model> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyAdapter.myviewholder holder, @SuppressLint("RecyclerView") int position, @NonNull Model model) {
        holder.nev.setText(model.getNev());
        holder.ar.setText(model.getAr());
//        holder.kategoria.setText(model.getKategoria());
        Glide.with(holder.img.getContext()).load(model.getUrl()).into(holder.img);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.activity_update_shop))
                        .setExpanded(true,1400)
                        .create();

                View myview=dialogPlus.getHolderView();
                final EditText url=myview.findViewById(R.id.ukepurl);
                final EditText nev=myview.findViewById(R.id.unev);
                final EditText ar=myview.findViewById(R.id.uar);
                final EditText kategoria=myview.findViewById(R.id.ukategoria);
                Button submit=myview.findViewById(R.id.modosit);

                url.setText(model.getUrl());
                nev.setText(model.getNev());
                ar.setText(model.getAr());
                kategoria.setText(model.getKategoria());

                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("url",url.getText().toString());
                        map.put("nev",nev.getText().toString());
                        map.put("kategoria",kategoria.getText().toString());
                        map.put("ar",ar.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Shop")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialogPlus.dismiss();
                                        Toast.makeText(holder.img.getContext(), "Sikeres módosítás", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                        Toast.makeText(holder.img.getContext(), "Sikertelen módosítás", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });


            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.img.getContext());
                builder.setTitle("Elem törlése");
                builder.setMessage("Biztosan törlöd ezt az elemet?");

                builder.setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Shop")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Nem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }
        });
    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder
    {
        CircleImageView img;
        //ImageView edit,delete;
        TextView edit,delete;
        TextView nev, ar, kategoria;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            img=(CircleImageView) itemView.findViewById(R.id.img);
            nev =(TextView)itemView.findViewById(R.id.nev);
            ar =(TextView)itemView.findViewById(R.id.ar);
           // kategoria =(TextView)itemView.findViewById(R.id.kategoria);

            edit  = (TextView) itemView.findViewById(R.id.editicon);
            delete = (TextView) itemView.findViewById(R.id.deleteicon);
        }
    }
}
