package com.android.zakaria.classmateinfo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.android.zakaria.classmateinfo.R;
import com.android.zakaria.classmateinfo.adapters.StudentAdapterListAndGridView;
import com.android.zakaria.classmateinfo.models.StudentInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<StudentInfo> studentInfoList;
    private RecyclerView recyclerView;
    private StudentAdapterListAndGridView studentAdapterListAndGridView;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private int currentViewMode = 0;
    private static final int VIEW_MODE_LIST_VIEW = 0;
    private static final int VIEW_MODE_GRID_VIEW = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        sharedPreferences = getSharedPreferences("category_preference", Context.MODE_PRIVATE);
        currentViewMode = sharedPreferences.getInt("currentCategoryMode", VIEW_MODE_LIST_VIEW);

        getStudentInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switchView:
                if (VIEW_MODE_LIST_VIEW == currentViewMode) {
                    currentViewMode = VIEW_MODE_GRID_VIEW;
                    switchCategoryViewMode(VIEW_MODE_GRID_VIEW);
                } else {
                    currentViewMode = VIEW_MODE_LIST_VIEW;
                    switchCategoryViewMode(VIEW_MODE_LIST_VIEW);
                }
                return true;

            case R.id.webView:
                startActivity(new Intent(this, WebViewActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    private void getStudentInfo() {
        studentInfoList = new ArrayList<>();

        studentInfoList.add(new StudentInfo("Md Zakaria Hossain", "152-15-5729", "zakariahossain143@gmail.com", "01710457406", R.drawable.zakaria));
        studentInfoList.add(new StudentInfo("Mehedi Hashan", "152-15-6103", "mehedihashan123@gmail.com", "01710445676", R.drawable.mehedi));
        studentInfoList.add(new StudentInfo("Ifty Rahman", "152-15-5737", "iftyrahamn123@gmail.com", "01710445744", R.drawable.ifty));
        studentInfoList.add(new StudentInfo("Syed Sabbir Rahman", "152-15-5729", "sabbirrahman678@gmail.com", "01767457406", R.drawable.sabbir));

        studentInfoList.add(new StudentInfo("Md Zakaria Hossain Zakaria Hossain", "152-15-5729", "zakariahossain143@gmail.com", "01710457406", R.drawable.zakaria));
        studentInfoList.add(new StudentInfo("Mehedi Hashan", "152-15-6103", "mehedihashan123@gmail.com", "01710445676", R.drawable.mehedi));
        studentInfoList.add(new StudentInfo("Ifty Rahman", "152-15-5737", "iftyrahamn123@gmail.com", "01710445744", R.drawable.ifty));
        studentInfoList.add(new StudentInfo("Syed Sabbir Rahman", "152-15-5729", "sabbirrahman678@gmail.com", "01767457406", R.drawable.sabbir));

        studentInfoList.add(new StudentInfo("Hossain Zakaria Hossain", "152-15-5729", "zakariahossain143@gmail.com", "01710457406", R.drawable.zakaria));
        studentInfoList.add(new StudentInfo("Mehedi Hashan", "152-15-6103", "mehedihashan123@gmail.com", "01710445676", R.drawable.mehedi));
        studentInfoList.add(new StudentInfo("Ifty Rahman", "152-15-5737", "iftyrahamn123@gmail.com", "01710445744", R.drawable.ifty));
        studentInfoList.add(new StudentInfo("Syed Sabbir Rahman", "152155729", "sabbirrahman678@gmail.com", "+8801767457406", R.drawable.sabbir));

        switchCategoryViewMode(currentViewMode);
    }

    private void switchCategoryViewMode(int selectedCategory) {
        if (selectedCategory == VIEW_MODE_LIST_VIEW) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            studentAdapterListAndGridView = new StudentAdapterListAndGridView(this, studentInfoList, "list_view");
            recyclerView.setAdapter(studentAdapterListAndGridView);

            categoryViewPreferenceSave(VIEW_MODE_LIST_VIEW);
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            studentAdapterListAndGridView = new StudentAdapterListAndGridView(this, studentInfoList, "grid_view");
            recyclerView.setAdapter(studentAdapterListAndGridView);

            categoryViewPreferenceSave(VIEW_MODE_GRID_VIEW);
            }
    }

    private void categoryViewPreferenceSave(int selectedCategory) {
        editor = sharedPreferences.edit();
        editor.putInt("currentCategoryMode", selectedCategory);
        editor.apply();
    }
}
