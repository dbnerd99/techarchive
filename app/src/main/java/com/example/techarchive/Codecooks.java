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

public class Codecooks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codecooks);

        TextView textView = findViewById(R.id.club2);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ListView listView = findViewById(R.id.listview1);
        List<String> list = new ArrayList<>();
        list.add("1. Group Discussion on Social Networking");
        list.add("2. Group Discussion on Digital India");
        list.add("3. Guest Session on Stress Management and Communication Skills");
        list.add("4. Session on Group discussion and Personal Interview");


        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    startActivity(new Intent(Codecooks.this, Codecooks1.class));

            }
        });
    }
}