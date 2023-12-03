package com.example.carddatabase.ui.search;

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
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.carddatabase.ModifyCardActivity;
import com.example.carddatabase.database.DBManager;
import com.example.carddatabase.database.DatabaseHelper;
import com.example.carddatabase.databinding.FragmentSearchBinding;
import com.example.carddatabase.R;

public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private FragmentSearchBinding binding;
    private DBManager dbManager;

    private ListView listView;

    private Button searchButton;

    private SimpleCursorAdapter adapter;
    private Cursor cursor;

    private EditText nameEditView, colorEditView, typeEditView;

    String name, color, type;

    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.NAME, DatabaseHelper.COLOR, DatabaseHelper.TYPE };

    final int[] to = new int[] { R.id.id, R.id.name, R.id.color, R.id.type };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        System.out.println("Created Dashboard Fragment");


        dbManager = new DBManager(getActivity());
        dbManager.open();
        cursor = dbManager.fetch();

        searchButton = (Button) root.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);

        nameEditView = (EditText) root.findViewById(R.id.nameSearch);
        colorEditView = (EditText) root.findViewById(R.id.colorSearch);
        typeEditView = (EditText) root.findViewById(R.id.typeSearch);

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

        Intent modify_intent = new Intent(getActivity(), ModifyCardActivity.class);
        modify_intent.putExtra("name", name);
        modify_intent.putExtra("color", color);
        modify_intent.putExtra("type", type);
        modify_intent.putExtra("id", id);

        startActivity(modify_intent);
    }

    @Override
    public void onClick(View v) {
        System.out.println("Clicked");
        if (v.getId() == R.id.searchButton) {
            System.out.println("Clicked searchButton");

            System.out.println("FLAG0");

            name = nameEditView.getText().toString();
            System.out.println("FLAG1");
            color = colorEditView.getText().toString();
            System.out.println("FLAG2");
            type = typeEditView.getText().toString();
            System.out.println("FLAG3");


            cursor = dbManager.search(name, color, type);
            System.out.println("Created Cursor");
//            adapter = new SimpleCursorAdapter(getActivity(), R.layout.activity_view_record, cursor, from, to, 0);
            adapter.changeCursor(cursor);
            adapter.notifyDataSetChanged();
            System.out.println("Updated ListView");

        }
    }
}