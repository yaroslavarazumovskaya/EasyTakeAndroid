package com.diploma.easytake;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.diploma.easytake.adapters.OrderAdapter;
import com.diploma.easytake.db.DBHelper;
import com.diploma.easytake.entity.ClientEntity;
import com.diploma.easytake.entity.OrderEntity;
import com.diploma.easytake.entity.ProductEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CurrentOrderActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private ListView activeOrderList;
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);
        dbHelper = new DBHelper(this);
        activeOrderList = findViewById(R.id.current_order_list);
        loadList();
    }

    private void loadList() {
        List<OrderEntity> allOrders = new ArrayList<>();
        for (OrderEntity allOrder : dbHelper.findAllOrders()) {
            if(allOrder.getActiveStatus())
                allOrders.add(allOrder);
        }
        if (adapter == null) {
            adapter = new OrderAdapter(this, R.layout.order_row, allOrders);
            activeOrderList.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(allOrders);
            adapter.notifyDataSetChanged();
        }
    }

    public void onClickOrderDetail(final View view) {
        View parent = (View) view.getParent();
        TextView textId = (TextView) parent.findViewById(R.id.order_id_col);
        int id = Integer.parseInt(textId.getText().toString());
        final OrderEntity orderById = dbHelper.findOrderById(id);
        String message = "Завмовлення №"+id+"\nТовар: "+orderById.getProductEntity().getName()+
                "\nКлієнт: "+orderById.getClientEntity().getName()+"\nДата аренди: "+
                orderById.getDate()+"\nСрок аренди: "+orderById.getDuration()+"год.";
        final AlertDialog dialog = new AlertDialog.Builder(CurrentOrderActivity.this)
                .setTitle("Інформація про замовлення")
                .setMessage(message)
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNeutralButton("Закрити замовлення",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                orderById.setActiveStatus(false);
                                ProductEntity productEntity = orderById.getProductEntity();
                                productEntity.setAmount(productEntity.getAmount()+1);
                                dbHelper.updateOrder(orderById);
                                dbHelper.updateProduct(productEntity);
                                loadList();
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("ВІДМІНА", null)
                .create();
        dialog.show();
    }

    private Context getContext(){
        return this;
    }


    public void onClickMainMenu(View view) {
        Intent intent = new Intent(CurrentOrderActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
