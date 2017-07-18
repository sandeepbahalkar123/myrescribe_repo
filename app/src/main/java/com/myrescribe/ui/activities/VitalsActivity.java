package com.myrescribe.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.myrescribe.R;

public class VitalsActivity extends AppCompatActivity {

    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vitals_main_activity);

        tableLayout = (TableLayout) findViewById(R.id.table);
        tableLayout.addView(addTableRow(2));
        tableLayout.addView(addTableRow(1));
        tableLayout.addView(addTableRow(2));
        tableLayout.addView(addTableRow(4));

    }

    private View addTableRow(int columnCount) {
        TableRow tableRow = new TableRow(this);

        for (int i = 0;i<columnCount;i++) {
            View item = LayoutInflater.from(this)
                    .inflate(R.layout.item, tableRow, false);
            tableRow.addView(item);
        }
        return tableRow;
    }
}
