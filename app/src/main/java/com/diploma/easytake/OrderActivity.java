package com.diploma.easytake;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.diploma.easytake.entity.ReportEntity;
import com.diploma.easytake.enums.Category;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private Button addNewOrderBtn;
    private Button addOrderReportBtn;
    private ListView orderList;
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        dbHelper = new DBHelper(this);
        addNewOrderBtn = findViewById(R.id.add_new_order_btn);
        addOrderReportBtn = findViewById(R.id.add_order_report_btn);
        orderList = findViewById(R.id.order_list);
        loadList();
    }

    private void loadList() {
        List<OrderEntity> allOrders = dbHelper.findAllOrders();
        if (adapter == null) {
            adapter = new OrderAdapter(this, R.layout.order_row, allOrders);
            orderList.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(allOrders);
            adapter.notifyDataSetChanged();
        }
    }

    public void onClickAddReport(View view) {
        final ReportEntity reportEntity = new ReportEntity();
        ArrayList<OrderEntity> allOrders = dbHelper.findAllOrders();
        final StringBuilder message = new StringBuilder("Усього замовлень:" + allOrders.size() + "\n\n");
        for (int i = 0; i < allOrders.size(); i++) {
            message.append("№").append(allOrders.get(i).getId()).append(": ");
            message.append("; Товар: ").append(allOrders.get(i).getProductEntity().getName());
            message.append("; Клієнт: ").append(allOrders.get(i).getClientEntity().getName());
            message.append("; Дата: ").append(allOrders.get(i).getDate());
            message.append("; Тривалість аренди: ").append(allOrders.get(i).getDuration()).append("год.");
            message.append("; Статус: ");
            message.append(allOrders.get(i).getActiveStatus() ? "Активний" : "Закритий");
            message.append("\n\n");
        }
        reportEntity.setText(message.toString());
        reportEntity.setCategory(Category.ORDERS);
        reportEntity.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        final androidx.appcompat.app.AlertDialog dialog = new AlertDialog.Builder(OrderActivity.this)
                .setTitle("Звіт про замовлення")
                .setMessage(message.toString())
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.insertReport(reportEntity);
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("Зберегти на пристрій", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        generateNoteOnSD(OrderActivity.this, "orders_" + sdf.format(new Date()), message.toString());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("ВІДМІНА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    private void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Reports");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            System.out.println(gpxfile.getAbsolutePath());
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("AAAAAAAAAAAAAAA");
        }
    }

    public void onClickAddOrder(final View view) {
        LayoutInflater inflater = getLayoutInflater();
        final View dialoglayout = inflater.inflate(R.layout.order_add_dialog, null);
        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setView(dialoglayout)
                .setPositiveButton("ДОДАТИ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("ВІДМІНА", null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final ArrayList<ProductEntity> allProducts = dbHelper.findAllProducts();
                List<String> prodNames = new ArrayList<>();
                for (ProductEntity allProduct : allProducts) {
                    if (allProduct.getAmount() > 0)
                        prodNames.add(allProduct.getName());
                }
                final ArrayList<ClientEntity> allClients = dbHelper.findAllClients();
                List<String> clientNames = new ArrayList<>();
                for (ClientEntity clientEntity : allClients) {
                    clientNames.add(clientEntity.getName());
                }

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, prodNames);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, clientNames);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner spinner1 = (Spinner) dialoglayout.findViewById(R.id.product_spinner);
                spinner1.setAdapter(adapter1);
                Spinner spinner2 = (Spinner) dialoglayout.findViewById(R.id.client_spinner);
                spinner2.setAdapter(adapter2);
                spinner1.setPrompt("Товар");
                spinner2.setPrompt("Клієнт");

            }
        });

        dialog.show();
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText duration = (EditText) dialoglayout.findViewById(R.id.newOrderDuration);
                Spinner spinner1 = (Spinner) dialoglayout.findViewById(R.id.product_spinner);
                Spinner spinner2 = (Spinner) dialoglayout.findViewById(R.id.client_spinner);
                final OrderEntity orderEntity = new OrderEntity();
                final ArrayList<ProductEntity> allProducts = new ArrayList<>();
                for (ProductEntity allProduct : dbHelper.findAllProducts()) {
                    if (allProduct.getAmount() > 0)
                        allProducts.add(allProduct);
                }
                final ArrayList<ClientEntity> allClients = dbHelper.findAllClients();
                if (!String.valueOf(duration.getText()).isEmpty())
                    if (spinner1.getSelectedItem() != null)
                        if (spinner2.getSelectedItem() != null) {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                orderEntity.setActiveStatus(true);
                                orderEntity.setDate(sdf.format(new Date()));
                                orderEntity.setDuration(Integer.parseInt(duration.getText().toString()));
                                orderEntity.setProductEntity(allProducts.get(spinner1.getSelectedItemPosition()));
                                orderEntity.setClientEntity(allClients.get(spinner2.getSelectedItemPosition()));
                                dbHelper.insertOrder(orderEntity);
                                ProductEntity productEntity = allProducts.get(spinner1.getSelectedItemPosition());
                                productEntity.setAmount(productEntity.getAmount() - 1);
                                dbHelper.updateProduct(productEntity);
                                loadList();
                                dialog.dismiss();
                            } catch (Exception e) {
                                Toast.makeText(OrderActivity.this, "Невірний формат!", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        } else
                            Toast.makeText(OrderActivity.this, "Виберіть клієнта!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(OrderActivity.this, "Виберіть товар!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(OrderActivity.this, "Введіть тривалість аренди!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickOrderDetail(final View view) {
        View parent = (View) view.getParent();
        TextView textId = (TextView) parent.findViewById(R.id.order_id_col);
        int id = Integer.parseInt(textId.getText().toString());
        OrderEntity orderById = dbHelper.findOrderById(id);
        String message = "Замовлення №" + id + "\nТовар: " + orderById.getProductEntity().getName() +
                "\nКлієнт: " + orderById.getClientEntity().getName() + "\nДата аренди: " +
                orderById.getDate() + "\nСрок аренди: " + orderById.getDuration() + "год.";
        final AlertDialog dialog = new AlertDialog.Builder(OrderActivity.this)
                .setTitle("Інформація про замовлення")
                .setMessage(message)
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dialog.show();
    }

    private Context getContext() {
        return this;
    }

    public void onClickMainMenu(View view) {
        Intent intent = new Intent(OrderActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
