package com.example.kringlan.sololev.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kringlan.sololev.R;
import com.example.kringlan.sololev.model.Customer;
import com.example.kringlan.sololev.model.Order;
import com.example.kringlan.sololev.view.OrderActivity;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private Order[] orders;

    public OrderAdapter(Order[] orders) {
        this.orders = orders;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_item, parent, false);
        OrderViewHolder viewHolder = new OrderViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        holder.bindOrder(orders[position]);
    }

    @Override
    public int getItemCount() {
        return orders.length;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView orderIdText;
        public TextView addressText;
        public TextView nameText;
        public TextView phoneText;

        public OrderViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();
            orderIdText = (TextView) itemView.findViewById(R.id.order_item_order_id_value);
            addressText = (TextView) itemView.findViewById(R.id.order_item_costumer_address_value);
            nameText = (TextView) itemView.findViewById(R.id.order_item_costumer_name_value);
            phoneText = (TextView) itemView.findViewById(R.id.order_item_costumer_phone_value);

            itemView.setOnClickListener(this);
        }

        public void bindOrder(Order order) {
            Customer customer = order.getCustomer();

            orderIdText.setText(order.getOrderID() + "");
            if(customer != null) {
                addressText.setText(customer.getAddress());
                nameText.setText(customer.getName());
                phoneText.setText(customer.getPhoneNumber());
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, OrderActivity.class);
            context.startActivity(intent);
        }
    }
}
