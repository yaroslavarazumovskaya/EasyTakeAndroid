package com.diploma.easytake;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.diploma.easytake.adapters.ProductAdapter;
import com.diploma.easytake.db.DBHelper;
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
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private Button addNewProductBtn;
    private Button addProductReportBtn;
    private ListView productList;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        dbHelper = new DBHelper(this);
        addNewProductBtn = findViewById(R.id.add_new_product_btn);
        addProductReportBtn = findViewById(R.id.add_product_report_btn);
        productList = findViewById(R.id.product_list);
        loadList();
    }

    private void loadList() {
        List<ProductEntity> allProducts = dbHelper.findAllProducts();
        if (adapter == null) {
            adapter = new ProductAdapter(this, R.layout.product_row, allProducts);
            productList.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(allProducts);
            adapter.notifyDataSetChanged();
        }
    }

    public void onClickAddReport(View view) {
        final ReportEntity reportEntity = new ReportEntity();
        ArrayList<ProductEntity> allProducts = dbHelper.findAllProducts();
        final StringBuilder message = new StringBuilder("Усього товарів:" + allProducts.size() + "\n\n");
        for (int i = 0; i < allProducts.size(); i++) {
            message.append(i + 1).append(": ");
            message.append(allProducts.get(i).getName());
            message.append("; Ціна/год: ").append(allProducts.get(i).getPrice()).append("₴");
            message.append("; Вартість: ").append(allProducts.get(i).getCost()).append("₴");
            message.append("; Залишок: ").append(allProducts.get(i).getAmount()).append("шт.");
            message.append("\n\n");
        }
        reportEntity.setText(message.toString());
        reportEntity.setCategory(Category.PRODUCTS);
        reportEntity.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        final androidx.appcompat.app.AlertDialog dialog = new AlertDialog.Builder(ProductActivity.this)
                .setTitle("Звіт про продукти")
                .setMessage(message.toString())
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.insertReport(reportEntity);
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("Зберегти на пристрій",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        generateNoteOnSD(ProductActivity.this, "products_"+sdf.format(new Date()), message.toString());
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

    public void onClickAddProduct(final View view) {
        LayoutInflater inflater = getLayoutInflater();
        final View dialoglayout = inflater.inflate(R.layout.product_add_dialog, null);
        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setView(dialoglayout)
                .setPositiveButton("ДОДАТИ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("ВІДМІНА", null)
                .create();
        dialog.show();

        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText name = (EditText) dialoglayout.findViewById(R.id.newProductName);
                final EditText price = (EditText) dialoglayout.findViewById(R.id.newProductPrice);
                final EditText cost = (EditText) dialoglayout.findViewById(R.id.newProductCost);
                final EditText amount = (EditText) dialoglayout.findViewById(R.id.newProductAmount);
                if (!String.valueOf(name.getText()).isEmpty())
                    if (!String.valueOf(price.getText()).isEmpty())
                        if (!String.valueOf(cost.getText()).isEmpty())
                            if (!String.valueOf(amount.getText()).isEmpty()) {
                                try {
                                    ProductEntity productEntity = new ProductEntity();
                                    productEntity.setName(name.getText().toString());
                                    productEntity.setPrice(Float.parseFloat(price.getText().toString()));
                                    productEntity.setCost(Float.parseFloat(cost.getText().toString()));
                                    productEntity.setAmount(Integer.parseInt(amount.getText().toString()));
                                    dbHelper.insertProduct(productEntity);
                                    loadList();
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    Toast.makeText(ProductActivity.this, "Неверно заполнены поля!", Toast.LENGTH_SHORT).show();

                                }
                            } else
                                Toast.makeText(ProductActivity.this, "Введите значение количества!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(ProductActivity.this, "Введите значение стоимости товара!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(ProductActivity.this, "Введите значение цены!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ProductActivity.this, "Введите наименование товара!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onClickMainMenu(View view) {
        Intent intent = new Intent(ProductActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
