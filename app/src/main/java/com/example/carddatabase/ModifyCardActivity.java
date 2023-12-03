package com.example.carddatabase;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.carddatabase.database.DBManager;

public class ModifyCardActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameText, colorText, typeText;
    private Button updateBtn, deleteBtn;

    private long _id;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "My Notification");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,  new String[] {Manifest.permission.POST_NOTIFICATIONS}, 1);
            return;
        }
        if (v.getId() == R.id.btn_update) {
            String name = nameText.getText().toString();
            String color = colorText.getText().toString();
            String type = typeText.getText().toString();

            dbManager.update(_id, name, color, type);

            builder.setContentTitle("Card Updated");
            builder.setContentText("Name: " + name + ", Color: " + color + ", Type: " + type);
            builder.setSmallIcon(R.drawable.ic_launcher_background);
            builder.setAutoCancel(true);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(1, builder.build());


            this.returnHome();

        }
        else if (v.getId() == R.id.btn_delete) {
            dbManager.delete(_id);

            builder.setContentTitle("Card Deleted");
            builder.setContentText("A Card was recently deleted from the database");
            builder.setSmallIcon(R.drawable.ic_launcher_background);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(1, builder.build());

            this.returnHome();
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}
