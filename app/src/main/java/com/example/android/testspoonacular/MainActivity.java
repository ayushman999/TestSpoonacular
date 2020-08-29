package com.example.android.testspoonacular;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
Button predict_btn;
Button add_btn;
ListView ing_list;
EditText add_text;
static ArrayList<String> ingridient_list=new ArrayList<String>();
    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        predict_btn=findViewById(R.id.predict_button);
        add_btn=findViewById(R.id.add_button);
        ing_list=findViewById(R.id.list_item);
        add_text=findViewById(R.id.edit_text);
        final ArrayList<String> afterCall=new ArrayList<>();
        final ArrayAdapter<String> ing_adapter=new ArrayAdapter<String>(this,R.layout.custom_ing_item,ingridient_list);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ing=add_text.getText().toString();
                if(ing.length()!=0)
                {
                    ingridient_list.add(ing);
                    ing_list.setAdapter(ing_adapter);

                }
                else
                {
                    Toast toast=Toast.makeText(MainActivity.this,"Empty addition!",Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        predict_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ingridient_list.size()!=0)
                {
                    Intent transfer=new Intent(MainActivity.this, Prediction.class);
                    startActivity(transfer);
                }
                else
                {
                    Toast toast=Toast.makeText(MainActivity.this,"Add ingredients first!",Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
    }
    public static ArrayList<String> getList() {
        return ingridient_list;
    }
}