package com.diploma.easytake.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.diploma.easytake.R;
import com.diploma.easytake.entity.ReportEntity;

import java.util.List;

public class ReportAdapter extends ArrayAdapter<ReportEntity> {

    private LayoutInflater inflater;
    private int layout;
    private List<ReportEntity> reportEntities;

    public ReportAdapter(Context context, int resource, List<ReportEntity> reportEntities){
        super(context, resource, reportEntities);
        this.reportEntities = reportEntities;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView report_id_col = (TextView) view.findViewById(R.id.report_id_col);
        TextView report_date_col = (TextView) view.findViewById(R.id.report_date_col);
        TextView report_category_col = (TextView) view.findViewById(R.id.report_category_col);

        ReportEntity reportEntity = reportEntities.get(position);

        report_id_col.setText(reportEntity.getId().toString());
        report_date_col.setText(reportEntity.getDate());
        report_category_col.setText(reportEntity.getCategory().getName());

        return view;
    }
}
