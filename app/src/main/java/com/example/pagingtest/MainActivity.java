package com.example.pagingtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.example.pagingtest.adapter.GithubPagedAdapter;
import com.example.pagingtest.vm.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;
    private GithubPagedAdapter mGithubPagedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        EditText editText = findViewById(R.id.search_repo);
        Button button = findViewById(R.id.search);
        button.setOnClickListener(v -> {
            mainViewModel.setItemPagedList(editText.getText().toString(), "stars");
            setRecyclerView();
        });

    }

    private void setRecyclerView() {
        mGithubPagedAdapter = new GithubPagedAdapter(this);
        mainViewModel.getItemPagedList().observe(this, items -> {
            mGithubPagedAdapter.submitList(items);
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mGithubPagedAdapter);
    }
}