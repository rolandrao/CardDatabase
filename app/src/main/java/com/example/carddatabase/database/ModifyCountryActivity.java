package com.example.carddatabase.database;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.example.carddatabase.R;

public class ModifyCountryActivity extends Activity implements OnClickListener {

    private EditText nameText, colorText, typeText;
    private Button updateBtn, deleteBtn;

    private long _id;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modify Record");

        setContentView(R.layout.activity_modify_record);

        dbManager = new DBManager(this);
        dbManager.open();

        nameText = (EditText) findViewById(R.id.editName);
        colorText = (EditText) findViewById(R.id.editColor);
        typeText = (EditText) findViewById(R.id.editType);

        updateBtn = (Button) findViewById(R.id.btn_update);
        deleteBtn = (Button) findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String color = intent.getStringExtra("color");
        String type = intent.getStringExtra("type");

        _id = Long.parseLong(id);

        nameText.setText(name);
        colorText.setText(color);
        typeText.setText(type);

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_update) {
            String name = nameText.getText().toString();
            String color = colorText.getText().toString();
            String type = typeText.getText().toString();

            dbManager.update(_id, name, color, type);
            this.returnHome();

        }
        else if (v.getId() == R.id.btn_delete) {
            dbManager.delete(_id);
            this.returnHome();
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), CardListActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}
