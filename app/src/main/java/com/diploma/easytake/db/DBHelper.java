package com.diploma.easytake.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.diploma.easytake.entity.ClientEntity;
import com.diploma.easytake.entity.OrderEntity;
import com.diploma.easytake.entity.ProductEntity;
import com.diploma.easytake.entity.ReportEntity;
import com.diploma.easytake.enums.Category;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String db_name = "easyTake";
    private static final int db_version = 1;

    private static final String db_table_clients = "clients";
    private static final String db_column_clients_name = "clientsName";
    private static final String db_column_clients_phone = "clientsPhone";
    private static final String db_column_clients_passport = "clientsPassport";

    private static final String db_table_products = "products";
    private static final String db_column_products_name = "productsName";
    private static final String db_column_products_price = "productsPrice";
    private static final String db_column_products_cost = "productsCost";
    private static final String db_column_products_amount = "productsAmount";

    private static final String db_table_orders = "orders";
    private static final String db_column_orders_date = "ordersDate";
    private static final String db_column_orders_duration = "ordersDuration";
    private static final String db_column_orders_status = "ordersStatus";

    private static final String db_table_reports = "reports";
    private static final String db_column_reports_text = "reportsText";
    private static final String db_column_reports_category = "reportsCategory";
    private static final String db_column_reports_date = "reportsDate";

    public DBHelper(Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");

        String queryClients = String.format(
                "CREATE TABLE %s (" +
                        "ID_client INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT NOT NULL, " +
                        "%s TEXT NOT NULL, " +
                        "%s TEXT NOT NULL, " +
                        "UNIQUE(%s, %s) ON CONFLICT REPLACE);",
                db_table_clients, db_column_clients_name,
                db_column_clients_phone, db_column_clients_passport,
                db_column_clients_phone, db_column_clients_passport);
        db.execSQL(queryClients);

        String queryProducts = String.format(
                "CREATE TABLE %s (" +
                        "ID_product INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT NOT NULL, " +
                        "%s FLOAT NOT NULL, " +
                        "%s FLOAT NOT NULL, " +
                        "%s INTEGER NOT NULL, " +
                        "UNIQUE(%s) ON CONFLICT REPLACE);",
                db_table_products, db_column_products_name,
                db_column_products_price, db_column_products_cost,
                db_column_products_amount, db_column_products_name);
        db.execSQL(queryProducts);

        String queryOrders = String.format(
                "CREATE TABLE %s (" +
                        "ID_order INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "ID_product INTEGER NOT NULL, " +
                        "ID_client INTEGER NOT NULL, " +
                        "%s TEXT NOT NULL, " +
                        "%s INTEGER NOT NULL, " +
                        "%s INTEGER NOT NULL, " +
                        "FOREIGN KEY (ID_product) REFERENCES %s(ID_product), " +
                        "FOREIGN KEY (ID_client) REFERENCES %s(ID_client));",
                db_table_orders, db_column_orders_date,
                db_column_orders_duration, db_column_orders_status,
                db_table_products, db_table_clients);
        db.execSQL(queryOrders);

        String queryReports = String.format(
                "CREATE TABLE %s (" +
                        "ID_report INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT NOT NULL, " +
                        "%s TEXT NOT NULL, " +
                        "%s TEXT NOT NULL);",
                db_table_reports, db_column_reports_text, db_column_reports_category,
                db_column_reports_date);
        db.execSQL(queryReports);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query1 = String.format("DELETE TABLE IF EXISTS %s", db_table_clients);
        db.execSQL(query1);
        String query2 = String.format("DELETE TABLE IF EXISTS %s", db_table_products);
        db.execSQL(query2);
        String query3 = String.format("DELETE TABLE IF EXISTS %s", db_table_orders);
        db.execSQL(query3);
        String query4 = String.format("DELETE TABLE IF EXISTS %s", db_table_reports);
        db.execSQL(query4);
        onCreate(db);
    }

    public void insertReport(ReportEntity reportEntity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(db_column_reports_text, reportEntity.getText());
        values.put(db_column_reports_category, reportEntity.getCategory().getName());
        values.put(db_column_reports_date, reportEntity.getDate());
        db.insertWithOnConflict(db_table_reports, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void insertClient(ClientEntity clientEntity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(db_column_clients_name, clientEntity.getName());
        values.put(db_column_clients_phone, clientEntity.getPhone());
        values.put(db_column_clients_passport, clientEntity.getPassport());
        db.insertWithOnConflict(db_table_clients, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void insertProduct(ProductEntity productEntity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(db_column_products_name, productEntity.getName());
        values.put(db_column_products_price, productEntity.getPrice());
        values.put(db_column_products_cost, productEntity.getCost());
        values.put(db_column_products_amount, productEntity.getAmount());
        db.insertWithOnConflict(db_table_products, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void insertOrder(OrderEntity orderEntity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(db_column_orders_date, orderEntity.getDate());
        values.put(db_column_orders_duration, orderEntity.getDuration());
        int status = orderEntity.getActiveStatus() ? 1 : 0;
        values.put(db_column_orders_status, status);
        values.put("ID_product", orderEntity.getProductEntity().getId());
        values.put("ID_client", orderEntity.getClientEntity().getId());
        db.insertWithOnConflict(db_table_orders, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ArrayList<ReportEntity> findAllReports() {
        ArrayList<ReportEntity> all = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT ID_report, " + db_column_reports_text + ", " + db_column_reports_category + ", " + db_column_reports_date +
                        " FROM " + db_table_reports +
                        " ORDER BY ID_report DESC", null);
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex("ID_report");
            int index2 = cursor.getColumnIndex(db_column_reports_text);
            int index3 = cursor.getColumnIndex(db_column_reports_category);
            int index4 = cursor.getColumnIndex(db_column_reports_date);
            String string = cursor.getString(index3);
            Category category = Category.getValue(string);
            all.add(new ReportEntity(cursor.getInt(index1), cursor.getString(index2), category,  cursor.getString(index4)));
        }
        cursor.close();
        db.close();

        return all;
    }

    public ArrayList<ClientEntity> findAllClients() {
        ArrayList<ClientEntity> all = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT ID_client, " + db_column_clients_name + ", " + db_column_clients_phone +
                        ", " + db_column_clients_passport + " FROM " + db_table_clients +
                        " ORDER BY ID_client DESC", null);
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex("ID_client");
            int index2 = cursor.getColumnIndex(db_column_clients_name);
            int index3 = cursor.getColumnIndex(db_column_clients_phone);
            int index4 = cursor.getColumnIndex(db_column_clients_passport);
            all.add(new ClientEntity(cursor.getInt(index1), cursor.getString(index2),
                    cursor.getString(index3), cursor.getString(index4)));
        }
        cursor.close();
        db.close();

        return all;
    }

    public ArrayList<ProductEntity> findAllProducts() {
        ArrayList<ProductEntity> all = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT ID_product, " + db_column_products_name + ", " + db_column_products_price +
                        ", " + db_column_products_cost + ", " + db_column_products_amount +
                        " FROM " + db_table_products +
                        " ORDER BY ID_product DESC", null);
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex("ID_product");
            int index2 = cursor.getColumnIndex(db_column_products_name);
            int index3 = cursor.getColumnIndex(db_column_products_price);
            int index4 = cursor.getColumnIndex(db_column_products_cost);
            int index5 = cursor.getColumnIndex(db_column_products_amount);
            all.add(new ProductEntity(cursor.getInt(index1), cursor.getString(index2),
                    cursor.getFloat(index3), cursor.getFloat(index4), cursor.getInt(index5)));
        }
        cursor.close();
        db.close();

        return all;
    }

    public ArrayList<OrderEntity> findAllOrders() {
        ArrayList<OrderEntity> all = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT ID_order, ID_product, ID_client, " +
                        db_column_orders_date + ", " + db_column_orders_duration +
                        ", " + db_column_orders_status +
                        " FROM " + db_table_orders +
                        " ORDER BY ID_order DESC", null);
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex("ID_order");
            int index2 = cursor.getColumnIndex("ID_product");
            int index3 = cursor.getColumnIndex("ID_client");
            int index4 = cursor.getColumnIndex(db_column_orders_date);
            int index5 = cursor.getColumnIndex(db_column_orders_duration);
            int index6 = cursor.getColumnIndex(db_column_orders_status);
            ProductEntity productEntity = findProductById(cursor.getInt(index2));
            ClientEntity clientEntity = findClientById(cursor.getInt(index3));
            boolean status = cursor.getInt(index6) == 1;
            all.add(new OrderEntity(cursor.getInt(index1), cursor.getString(index4),
                    cursor.getInt(index5), status, productEntity, clientEntity));
        }
        cursor.close();
        db.close();

        return all;
    }

    public ReportEntity findReportById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT ID_report, " + db_column_reports_text + ", " + db_column_reports_category +", " + db_column_reports_date +
                        " FROM " + db_table_reports +
                        " WHERE ID_report = " + id, null);
        if (cursor.moveToFirst()) {
            int index1 = cursor.getColumnIndex("ID_report");
            int index2 = cursor.getColumnIndex(db_column_reports_text);
            int index3 = cursor.getColumnIndex(db_column_reports_category);
            int index4 = cursor.getColumnIndex(db_column_reports_date);
            Category category = Category.getValue(cursor.getString(index3));
            ReportEntity reportEntity = new ReportEntity(cursor.getInt(index1), cursor.getString(index2), category, cursor.getString(index4));

            cursor.close();
            db.close();
            return reportEntity;
        }
        return null;
    }

    public ClientEntity findClientById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT ID_client, " + db_column_clients_name + ", " + db_column_clients_phone +
                        ", " + db_column_clients_passport + " FROM " + db_table_clients +
                        " WHERE ID_client = " + id, null);
        if (cursor.moveToFirst()) {
            int index1 = cursor.getColumnIndex("ID_client");
            int index2 = cursor.getColumnIndex(db_column_clients_name);
            int index3 = cursor.getColumnIndex(db_column_clients_phone);
            int index4 = cursor.getColumnIndex(db_column_clients_passport);
            ClientEntity clientEntity = new ClientEntity(cursor.getInt(index1), cursor.getString(index2),
                    cursor.getString(index3), cursor.getString(index4));

            cursor.close();
            db.close();
            return clientEntity;
        }
        return null;
    }

    public ProductEntity findProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT ID_product, " + db_column_products_name + ", " + db_column_products_price +
                        ", " + db_column_products_cost + ", " + db_column_products_amount +
                        " FROM " + db_table_products +
                        " WHERE ID_product = " + id, null);
        if (cursor.moveToFirst()) {
            int index1 = cursor.getColumnIndex("ID_product");
            int index2 = cursor.getColumnIndex(db_column_products_name);
            int index3 = cursor.getColumnIndex(db_column_products_price);
            int index4 = cursor.getColumnIndex(db_column_products_cost);
            int index5 = cursor.getColumnIndex(db_column_products_amount);
            int anInt = cursor.getInt(index1);
            String string = cursor.getString(index2);
            float aFloat = cursor.getFloat(index3);
            float aFloat1 = cursor.getFloat(index4);
            int anInt1 = cursor.getInt(index5);
            ProductEntity productEntity = new ProductEntity(anInt, string,
                    aFloat, aFloat1, anInt1);

            cursor.close();
            db.close();
            return productEntity;
        }
        return null;
    }

    public ProductEntity findProductByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT ID_product, " + db_column_products_name + ", " + db_column_products_price +
                        ", " + db_column_products_cost + ", " + db_column_products_amount +
                        " FROM " + db_table_products +
                        " WHERE " + db_column_products_name + " = " + name, null);
        if (cursor.moveToFirst()) {
            int index1 = cursor.getColumnIndex("ID_product");
            int index2 = cursor.getColumnIndex(db_column_products_name);
            int index3 = cursor.getColumnIndex(db_column_products_price);
            int index4 = cursor.getColumnIndex(db_column_products_cost);
            int index5 = cursor.getColumnIndex(db_column_products_amount);
            ProductEntity productEntity = new ProductEntity(cursor.getInt(index1), cursor.getString(index2),
                    cursor.getFloat(index3), cursor.getFloat(index4), cursor.getInt(index5));

            cursor.close();
            db.close();
            return productEntity;
        }
        return null;
    }

    public OrderEntity findOrderById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT ID_order, ID_product, ID_client, " +
                        db_column_orders_date + ", " + db_column_orders_duration +
                        ", " + db_column_orders_status +
                        " FROM " + db_table_orders +
                        " WHERE ID_order = " + id, null);
        if (cursor.moveToFirst()) {
            int index1 = cursor.getColumnIndex("ID_order");
            int index2 = cursor.getColumnIndex("ID_product");
            int index3 = cursor.getColumnIndex("ID_client");
            int index4 = cursor.getColumnIndex(db_column_orders_date);
            int index5 = cursor.getColumnIndex(db_column_orders_duration);
            int index6 = cursor.getColumnIndex(db_column_orders_status);
            ProductEntity productEntity = findProductById(cursor.getInt(index2));
            ClientEntity clientEntity = findClientById(cursor.getInt(index3));
            boolean status = cursor.getInt(index6) == 1;
            OrderEntity orderEntity = new OrderEntity(cursor.getInt(index1), cursor.getString(index4),
                    cursor.getInt(index5), status, productEntity, clientEntity);

            cursor.close();
            db.close();
            return orderEntity;
        }
        return null;
    }

    public void updateProduct(ProductEntity productEntity) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("update " + db_table_products + " " +
//                "set " + db_column_products_name + "= " + productEntity.getName() + ", " +
//                db_column_products_price + "= " + productEntity.getPrice() + ", " +
//                db_column_products_cost + "= " + productEntity.getCost() + ", " +
//                db_column_products_amount + "= " + productEntity.getAmount() +
//                " where ID_product = " + productEntity.getId(), null);
        ContentValues cv = new ContentValues();
        cv.put(db_column_products_amount, productEntity.getAmount());
        db.update(db_table_products, cv, "ID_product = " + productEntity.getId(), null);
        db.close();
    }

    public void updateOrder(OrderEntity orderEntity) {
        SQLiteDatabase db = this.getWritableDatabase();
        int status = orderEntity.getActiveStatus() ? 1 : 0;
//        db.execSQL("update " + db_table_orders + " " +
//                "set ID_product= " + orderEntity.getProductEntity().getId() + ", " +
//                "ID_client= " + orderEntity.getClientEntity().getId() + ", " +
//                db_column_orders_date + "= " + orderEntity.getDate() + ", " +
//                db_column_orders_duration + "= " + orderEntity.getDuration() + ", " +
//                db_column_orders_status + "= " + status +
//                " where ID_order = " + orderEntity.getId(), null);
        ContentValues cv = new ContentValues();
        cv.put(db_column_orders_status, status);
        db.update(db_table_orders, cv, "ID_order = " + orderEntity.getId(), null);
        db.close();
    }


}
