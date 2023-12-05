package com.example.techarchive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Philosopher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_philosopher);

        TextView textView = findViewById(R.id.club4);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ListView listView = findViewById(R.id.listview3);
        List<String> list = new ArrayList<>();
        list.add("1. Session on Andriod Studio");
        list.add("2. Session on Cloud Computing");
        list.add("3. session on network security");
        list.add("4. Session on Recent Trends in IT Industry");

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    startActivity(new Intent(Philosopher.this, Codecooks1.class));

            }
        });
    }
}