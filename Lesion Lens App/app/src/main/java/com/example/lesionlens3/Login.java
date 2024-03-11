package com.example.lesionlens3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private Button button;
////
////    @Override
////    protected void onCreate(@Nullable Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.get_data2);
////
////        button=findViewById(R.id.button);
////        button.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent =new Intent(Login.this, MainActivity.class);
////                startActivity(intent);
////            }
////        });
////    }
String[] item={"Scalp","Ear","Face","Back","Trunck","Chest","Upper Extremity","Abdomen","Other"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_data);
        autoCompleteTextView=findViewById(R.id.autoCompletetext);
        adapterItems=new ArrayAdapter<String>(this,R.layout.list_item,item);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item=parent.getItemAtPosition(position).toString();
                Toast.makeText(Login.this, "Item"+item, Toast.LENGTH_SHORT).show();
            }
        });

        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(Login.this,"Information submitted successfully",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
