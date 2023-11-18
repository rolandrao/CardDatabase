package com.example.carddatabase.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.View.OnClickListener;
import com.example.carddatabase.R;

import com.example.carddatabase.database.AddCardActivity;
import com.example.carddatabase.database.CardListActivity;
import com.example.carddatabase.database.DBManager;
import com.example.carddatabase.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements OnClickListener {

    private FragmentHomeBinding binding;
    private Button addBtn;
    private EditText nameEditText;
    private EditText colorEditText;
    private EditText typeEditText;

    private DBManager dbManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        System.out.println("Created Home Fragment");

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        nameEditText = (EditText) root.findViewById(R.id.editName);
        colorEditText = (EditText) root.findViewById(R.id.editColor);
        typeEditText = (EditText) root.findViewById(R.id.editType);

        addBtn = (Button) root.findViewById(R.id.add_record);
        addBtn.setOnClickListener(this);

        dbManager = new DBManager(getActivity());
        dbManager.open();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_record){
            final String name = nameEditText.getText().toString();
            final String color = colorEditText.getText().toString();
            final String type = typeEditText.getText().toString();

            System.out.println(name);
            System.out.println(color);
            System.out.println(type);
            System.out.println("Before insert");
            dbManager.insert(name, color, type);
            System.out.println("After insert");

            dbManager.fetch();

        }
    }
}