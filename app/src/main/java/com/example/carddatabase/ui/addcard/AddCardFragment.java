package com.example.carddatabase.ui.addcard;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.carddatabase.R;

import com.example.carddatabase.database.DBManager;
import com.example.carddatabase.database.DatabaseHelper;
import com.example.carddatabase.databinding.FragmentAddCardBinding;

public class AddCardFragment extends Fragment implements OnClickListener {

    private FragmentAddCardBinding binding;
    private Button addBtn;
    private EditText nameEditText;
    private EditText colorEditText;
    private EditText typeEditText;
    private ListView databaseView;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;

    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.NAME, DatabaseHelper.COLOR, DatabaseHelper.TYPE };

    final int[] to = new int[] { R.id.id, R.id.name, R.id.color, R.id.type };
    private DBManager dbManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", IMPORTANCE_DEFAULT);
            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        AddCardViewModel homeViewModel =
                new ViewModelProvider(this).get(AddCardViewModel.class);

        System.out.println("Created Home Fragment");

        binding = FragmentAddCardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        nameEditText = (EditText) root.findViewById(R.id.editName);
        colorEditText = (EditText) root.findViewById(R.id.editColor);
        typeEditText = (EditText) root.findViewById(R.id.editType);

        addBtn = (Button) root.findViewById(R.id.add_record);
        addBtn.setOnClickListener(this);

        dbManager = new DBManager(getActivity());
        dbManager.open();
        cursor = dbManager.fetch();

        databaseView = (ListView) root.findViewById(R.id.databaseView);
        databaseView.setSmoothScrollbarEnabled(true);
        databaseView.setEmptyView(root.findViewById(R.id.empty));

        adapter = new SimpleCursorAdapter(getActivity(), R.layout.activity_view_record, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        databaseView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_record) {
            final String name = nameEditText.getText().toString();
            final String color = colorEditText.getText().toString();
            final String type = typeEditText.getText().toString();

            System.out.println(name);
            System.out.println(color);
            System.out.println(type);
            System.out.println("Before insert");
            dbManager.insert(name, color, type);
            databaseView.smoothScrollToPosition(databaseView.getCount()-1);
            cursor = dbManager.fetch();
            adapter.changeCursor(cursor);
            adapter.notifyDataSetChanged();
            System.out.println("Updated DatabaseView");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "My Notification");
            builder.setContentTitle("New Card Added");
            builder.setContentText("Name: " + name + ", Color: " + color + ", Type: " + type);
            builder.setSmallIcon(R.drawable.ic_launcher_background);
            builder.setAutoCancel(true);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getContext());
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),  new String[] {Manifest.permission.POST_NOTIFICATIONS}, 1);
                return;
            }
            managerCompat.notify(1, builder.build());
            nameEditText.setText("");
            colorEditText.setText("");
            typeEditText.setText("");
            System.out.println("After insert");

            dbManager.fetch();

        }
    }
}