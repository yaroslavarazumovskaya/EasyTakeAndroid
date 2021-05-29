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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.diploma.easytake.adapters.ClientAdapter;
import com.diploma.easytake.adapters.ProductAdapter;
import com.diploma.easytake.db.DBHelper;
import com.diploma.easytake.entity.ClientEntity;
import com.diploma.easytake.entity.ReportEntity;
import com.diploma.easytake.enums.Category;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private Button addNewClientBtn;
    private Button addClientReportBtn;
    private ListView clientList;
    private ClientAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        dbHelper = new DBHelper(this);
        addNewClientBtn = findViewById(R.id.add_new_client_btn);
        addClientReportBtn = findViewById(R.id.add_client_report_btn);
        clientList = findViewById(R.id.client_list);
        loadList();
    }

    private void loadList() {
        List<ClientEntity> allClients = dbHelper.findAllClients();
        if (adapter == null) {
            adapter = new ClientAdapter(this, R.layout.client_row, allClients);
            clientList.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(allClients);
            adapter.notifyDataSetChanged();
        }
    }

    public void onClickAddReport(View view) {
        final ReportEntity reportEntity = new ReportEntity();
        ArrayList<ClientEntity> allClients = dbHelper.findAllClients();
        final StringBuilder message = new StringBuilder("Усього клієнтів:" + allClients.size() + "\n\n");
        for (int i = 0; i < allClients.size(); i++) {
            message.append(i + 1).append(": ");
            message.append(allClients.get(i).getName());
            message.append("; Телефон: ").append(allClients.get(i).getPhone());
            message.append("; Паспортні дані: ").append(allClients.get(i).getPassport());
            message.append("\n\n");
        }
        reportEntity.setText(message.toString());
        reportEntity.setCategory(Category.CLIENTS);
        reportEntity.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        final androidx.appcompat.app.AlertDialog dialog = new AlertDialog.Builder(ClientActivity.this)
                .setTitle("Звіт про клієнтів")
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
                        generateNoteOnSD(ClientActivity.this, "clients_"+sdf.format(new Date()), message.toString());
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

    public void onClickAddClient(final View view) {
        LayoutInflater inflater = getLayoutInflater();
        final View dialoglayout = inflater.inflate(R.layout.client_add_dialog, null);
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
                final EditText name = (EditText) dialoglayout.findViewById(R.id.newClientName);
                final EditText phone = (EditText) dialoglayout.findViewById(R.id.newClientPhone);
                final EditText passport = (EditText) dialoglayout.findViewById(R.id.newClientPassport);
                if (!String.valueOf(name.getText()).isEmpty())
                    if (!String.valueOf(phone.getText()).isEmpty())
                        if (!String.valueOf(passport.getText()).isEmpty()) {
                            try {
                                Pattern pattern = Pattern.compile("^\\+380\\d\\d\\d\\d\\d\\d\\d\\d\\d$" ,Pattern.CASE_INSENSITIVE);
                                Matcher matcher = pattern.matcher(String.valueOf(phone.getText()));
                                if(!matcher.matches())
                                    throw new Exception();
                                ClientEntity clientEntity = new ClientEntity();
                                clientEntity.setName(name.getText().toString());
                                clientEntity.setPhone(phone.getText().toString());
                                clientEntity.setPassport(passport.getText().toString());
                                dbHelper.insertClient(clientEntity);
                                loadList();
                                dialog.dismiss();
                            } catch (Exception e) {
                                Toast.makeText(ClientActivity.this, "Невірно заповнені дані!", Toast.LENGTH_SHORT).show();

                            }
                        } else
                            Toast.makeText(ClientActivity.this, "Введіть паспортні дані!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(ClientActivity.this, "Введіть телефонний номер у форматі \"+380...\"!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ClientActivity.this, "Введіть ім\'я!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onClickMainMenu(View view) {
        Intent intent = new Intent(ClientActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
