package com.diploma.easytake.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.diploma.easytake.R;
import com.diploma.easytake.entity.ProductEntity;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<ProductEntity> {

    private LayoutInflater inflater;
    private int layout;
    private List<ProductEntity> productEntities;

    public ProductAdapter(Context context, int resource, List<ProductEntity> productEntities){
        super(context, resource, productEntities);
        this.productEntities = productEntities;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView product_name_col = (TextView) view.findViewById(R.id.product_name_col);
        TextView product_price_col = (TextView) view.findViewById(R.id.product_price_col);
        TextView product_cost_col = (TextView) view.findViewById(R.id.product_cost_col);
        TextView product_amount_col = (TextView) view.findViewById(R.id.product_amount_col);

        ProductEntity productEntity = productEntities.get(position);

        product_name_col.setText(productEntity.getName());
        product_price_col.setText(productEntity.getPrice().toString());
        product_cost_col.setText(productEntity.getCost().toString());
        product_amount_col.setText(productEntity.getAmount().toString());

        return view;
    }
}
