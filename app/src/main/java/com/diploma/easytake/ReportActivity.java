package com.diploma.easytake;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.diploma.easytake.adapters.ReportAdapter;
import com.diploma.easytake.db.DBHelper;
import com.diploma.easytake.entity.ReportEntity;

import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private ListView reportList;
    private ReportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        dbHelper = new DBHelper(this);
        reportList = findViewById(R.id.report_list);
        loadList();
    }


    private void loadList() {
        List<ReportEntity> allReports = dbHelper.findAllReports();
        if (adapter == null) {
            adapter = new ReportAdapter(this, R.layout.report_row, allReports);
            reportList.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(allReports);
            adapter.notifyDataSetChanged();
        }
    }

    public void onClickOrderDetail(final View view) {
        View parent = (View) view.getParent();
        TextView textId = (TextView) parent.findViewById(R.id.report_id_col);
        int id = Integer.parseInt(textId.getText().toString());
        ReportEntity orderById = dbHelper.findReportById(id);
        final AlertDialog dialog = new AlertDialog.Builder(ReportActivity.this)
                .setTitle("Звіт за категорією "+orderById.getCategory().getName()+" за "+orderById.getDate())
                .setMessage(orderById.getText())
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dialog.show();
    }

    public void onClickMainMenu(View view) {
        Intent intent = new Intent(ReportActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
