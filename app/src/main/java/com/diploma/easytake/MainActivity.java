package com.diploma.easytake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button products;
    private Button clients;
    private Button currentOrders;
    private Button orders;
    private Button reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        products = findViewById(R.id.products);
        clients = findViewById(R.id.clients);
        currentOrders = findViewById(R.id.currentOrders);
        orders = findViewById(R.id.orders);
        reports = findViewById(R.id.reports);
    }

    public void onClickProducts(View view){
        Intent intent = new Intent(MainActivity.this, ProductActivity.class);
        startActivity(intent);
    }
    public void onClickClients(View view){
        Intent intent = new Intent(MainActivity.this, ClientActivity.class);
        startActivity(intent);
    }
    public void onClickCurrentOrders(View view){
        Intent intent = new Intent(MainActivity.this, CurrentOrderActivity.class);
        startActivity(intent);
    }
    public void onClickOrders(View view){
        Intent intent = new Intent(MainActivity.this, OrderActivity.class);
        startActivity(intent);
    }
    public void onClickReports(View view){
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
        startActivity(intent);
    }
}
