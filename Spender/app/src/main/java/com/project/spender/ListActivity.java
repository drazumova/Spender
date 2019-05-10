package com.project.spender;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.spender.data.AppDatabase;
import com.project.spender.data.entities.Product;

import java.util.ArrayList;
import java.util.List;


public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private EditText request;
    private List<String> itemsList;
    private AppDatabase dbManager;
    private ImageButton scan;
    private ImageButton list;
    private ImageButton statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_list);
        itemsList = new ArrayList<>();
        dbManager = ChecksRoller.getInstance(this).getAppDatabase();

        scan = findViewById(R.id.scan);
        list = findViewById(R.id.list);
        statistics = findViewById(R.id.statistics);

        list.setBackgroundColor(Color.argb(40, 255, 0, 0));

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ListActivity.this, "Misha molodez",
                        Toast.LENGTH_LONG).show();
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intentShowList = new Intent(ListActivity.this, ListActivity.class);
                startActivity(intentShowList);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(ListActivity.this, Scan.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivityForResult(intent, 42);
            }
        });

        try {
            for (Product i : dbManager.getCheckDao().getAllProducts()) {
                itemsList.add(i.getName());
            }
        } catch (Exception e) {

            e.printStackTrace();
            itemsList = new ArrayList<>();
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, itemsList);
        listView = findViewById(R.id.itemsList);
        listView.setAdapter(adapter);



        request = findViewById(R.id.request);
        request.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                try {
//                    updateList(v.getText().toString());
//                } catch (SQLException e) {
//                    System.out.println(e.getMessage());
//                    itemsList = new ArrayList<>();
//                }
//                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//                v.setText("");
//                System.out.println(itemsList.size() + " " + v.getText());
//                adapter.notifyDataSetChanged();
                Toast.makeText(ListActivity.this, "Waiting for shashas understanding",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

//    private void updateList(String ex) throws SQLException {
//        itemsList.clear();
//        for (Product i : dbHelper.getAllByName(ex)) {
//            itemsList.add(i.getName());
//        }
//    }
}
