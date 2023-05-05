package com.example.muszaki;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {
    private Context mContext;
    private List<OrderItem> mOrderItems;public OrderListAdapter(Context context, List<OrderItem> orderItems) {
        mContext = context;
        mOrderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItem orderItem = mOrderItems.get(position);
        holder.orderName.setText((position + 1) + orderItem.getName() + " " + orderItem.getNev() + ": " + orderItem.getAr() + "Ft");
    }

    @Override
    public int getItemCount() {
        return mOrderItems.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView orderName;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderName = itemView.findViewById(R.id.order_item_name);
            orderName.setBackgroundColor(0xFF000000); // fekete háttér beállítása
        }
    }
}