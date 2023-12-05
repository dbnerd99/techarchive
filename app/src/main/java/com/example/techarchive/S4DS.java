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

public class S4DS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s4_ds);

        TextView textView = findViewById(R.id.club3);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ListView listView = findViewById(R.id.listview2);
        List<String> list = new ArrayList<>();
        list.add("1. Session on Life Skill Development");
        list.add("2. Guest session How to Read Reference Paper");
        list.add("3. Guest session on Life Skill Management");
        list.add("4. Program on Swatchha Ani Swastha Bharat Abhiyan");
        list.add("5. International Yoga Day Celebration");

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    startActivity(new Intent(S4DS.this, Codecooks1.class));

            }
        });
    }
}