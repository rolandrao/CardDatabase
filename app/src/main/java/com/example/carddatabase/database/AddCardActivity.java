package com.example.carddatabase.database;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.example.carddatabase.R;

public class AddCardActivity extends Activity implements OnClickListener {

    private Button addTodoBtn;
    private EditText nameEditText;
    private EditText colorEditText;
    private EditText typeEditText;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Record");

        setContentView(R.layout.fragment_home);

        nameEditText = (EditText) findViewById(R.id.editName);
        colorEditText = (EditText) findViewById(R.id.editColor);
        typeEditText = (EditText) findViewById(R.id.editType);

        addTodoBtn = (Button) findViewById(R.id.add_record);

        dbManager = new DBManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        System.out.println(v);
        System.out.println(v.getId());
//        switch (v.getId()) {
//            case R.id.add_record:
            if (v.getId() == R.id.add_record) {


                final String name = nameEditText.getText().toString();
                final String color = colorEditText.getText().toString();
                final String type = typeEditText.getText().toString();

                dbManager.insert(name, color, type);

                Intent main = new Intent(AddCardActivity.this, CardListActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main);
            }
        }
    }
