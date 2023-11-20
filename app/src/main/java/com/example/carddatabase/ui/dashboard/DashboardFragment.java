package com.example.carddatabase.ui.dashboard;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.carddatabase.database.DBManager;
import com.example.carddatabase.database.DatabaseHelper;
import com.example.carddatabase.database.ModifyCountryActivity;
import com.example.carddatabase.databinding.FragmentDashboardBinding;
import com.example.carddatabase.R;

public class DashboardFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private FragmentDashboardBinding binding;
    private DBManager dbManager;

    private ListView listView;

    private SimpleCursorAdapter adapter;

    String name, color, type;

    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.NAME, DatabaseHelper.COLOR, DatabaseHelper.TYPE };

    final int[] to = new int[] { R.id.id, R.id.name, R.id.color, R.id.type };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        System.out.println("Created Dashboard Fragment");


        dbManager = new DBManager(getActivity());
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = (ListView) root.findViewById(R.id.listView);
        listView.setEmptyView(root.findViewById(R.id.empty));

        adapter = new SimpleCursorAdapter(getActivity(), R.layout.activity_view_record, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
        TextView idTextView = (TextView) view.findViewById(R.id.id);
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView colorTextView = (TextView) view.findViewById(R.id.color);
        TextView typeTextView = (TextView) view.findViewById(R.id.type);

        String id = idTextView.getText().toString();
        String name = nameTextView.getText().toString();
        String color = colorTextView.getText().toString();
        String type = typeTextView.getText().toString();

        Intent modify_intent = new Intent(getActivity(), ModifyCountryActivity.class);
        modify_intent.putExtra("name", name);
        modify_intent.putExtra("color", color);
        modify_intent.putExtra("type", type);
        modify_intent.putExtra("id", id);

        startActivity(modify_intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.searchButton) {
            TextView nameTextView = (TextView) v.findViewById(R.id.nameSearch);
            TextView colorTextView = (TextView) v.findViewById(R.id.colorSearch);
            TextView typeTextView = (TextView) v.findViewById(R.id.typeSearch);

            name = nameTextView.getText().toString();
            color = colorTextView.getText().toString();
            type = colorTextView.getText().toString();

            Cursor cursor = dbManager.search(name, color, type);

        }
    }
}