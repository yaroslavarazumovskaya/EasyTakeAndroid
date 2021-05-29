package com.diploma.easytake.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.diploma.easytake.R;
import com.diploma.easytake.entity.OrderEntity;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<OrderEntity> {

    private LayoutInflater inflater;
    private int layout;
    private List<OrderEntity> orderEntities;

    public OrderAdapter(Context context, int resource, List<OrderEntity> orderEntities){
        super(context, resource, orderEntities);
        this.orderEntities = orderEntities;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView order_id_col = (TextView) view.findViewById(R.id.order_id_col);
        TextView order_product_col = (TextView) view.findViewById(R.id.order_product_col);
        TextView order_client_col = (TextView) view.findViewById(R.id.order_client_col);

        OrderEntity orderEntity = orderEntities.get(position);

        order_id_col.setText(orderEntity.getId().toString());
        order_product_col.setText(orderEntity.getProductEntity().getName());
        order_client_col.setText(orderEntity.getClientEntity().getName());

        return view;
    }
}
