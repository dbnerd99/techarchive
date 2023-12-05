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

public class Cypher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cypher);

        TextView textView = findViewById(R.id.club1);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ListView listView = findViewById(R.id.listview);
        List<String> list = new ArrayList<>();
        list.add("1. Debate on education system in India Vs Abroad");
        list.add("2. Softskill Workshop by Barclays");
        list.add("3. Session on Soft Skills");
        list.add("4. Session on Leadership and Team building");
        list.add("5. Communication, Employability Skill Development And Aptitude Training");

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    startActivity(new Intent(Cypher.this, Codecooks1.class));

            }
        });
    }
}