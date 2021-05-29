package com.diploma.easytake.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.diploma.easytake.R;
import com.diploma.easytake.entity.ClientEntity;

import java.util.List;

public class ClientAdapter extends ArrayAdapter<ClientEntity> {

    private LayoutInflater inflater;
    private int layout;
    private List<ClientEntity> clientEntities;

    public ClientAdapter(Context context, int resource, List<ClientEntity> clientEntities){
        super(context, resource, clientEntities);
        this.clientEntities = clientEntities;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView client_name_col = (TextView) view.findViewById(R.id.client_name_col);
        TextView client_phone_col = (TextView) view.findViewById(R.id.client_phone_col);
        TextView client_passport_col = (TextView) view.findViewById(R.id.client_passport_col);

        ClientEntity clientEntity = clientEntities.get(position);

        client_name_col.setText(clientEntity.getName());
        client_phone_col.setText(clientEntity.getPhone());
        client_passport_col.setText(clientEntity.getPassport());

        return view;
    }
}
