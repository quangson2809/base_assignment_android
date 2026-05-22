package com.example.base;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private AdapterHelper adapterHelper;
    private DatabaseHelper databaseHelper;
    private SearchView searchView;
    private FloatingActionButton fabAddItem;
    private RecyclerView recyclerView;
    private WifiReceiver wifiReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setRecycle();
        loadListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wifiReceiver != null) {
            unregisterReceiver(wifiReceiver);
        }
    }

    public void init(){
        searchView = findViewById(R.id.searchView);
        fabAddItem = findViewById(R.id.fabAddItem);
        recyclerView = findViewById(R.id.recycleView);
        databaseHelper = new DatabaseHelper(this);
        adapterHelper = new AdapterHelper(
                databaseHelper.getAllItems(),
                this,
                new AdapterHelper.OnItemLongClickListener(){
                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(MainActivity.this,
                                "Long click item: " + position,
                                Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onMenuClick(int actionId, int position) {
                        processContextMenu(actionId,position);
                    }
                },
                new AdapterHelper.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Toast.makeText(MainActivity.this,
                                "Click item: " + position,
                                Toast.LENGTH_SHORT).show();
                    }

                }
        );
    }

    //load data adapter
    public void loadData(){
        adapterHelper.setData(databaseHelper.getAllItems());
        setRecycle();
    }

    //set recycle view
    public void setRecycle(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterHelper);
    }

    //change activity
    public void changeAct(Item item){
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.putExtra("Item",item);
        startActivity(intent);
    }

    public void loadListener(){
        //search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                adapterHelper.filter(newText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterHelper.filter(query);
                return true;
            }
        });

        //floating action button
        fabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.addItem(new Item("name",0));
                loadData();
                recyclerView.scrollToPosition(adapterHelper.getSize()-1);
                Toast.makeText(MainActivity.this, "Click add item", Toast.LENGTH_SHORT).show();
            }
        });

        // Broadcast Receiver
        wifiReceiver = new WifiReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(wifiReceiver, filter);
    }

    public void processContextMenu(int actionId, int position){
        switch(actionId){
            case 1:
                //xóa
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có muốn xóa item này không?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            // gọi callback xóa item tại position
                            //databaseHelper.deleteItem(adapterHelper.getItem(position).getId());
                            databaseHelper.deleteItems(adapterHelper.getSelectedIds());
                            loadData();
                        })
                        .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                        .show();

                Toast.makeText(MainActivity.this,
                        "Removed item: " + position,
                        Toast.LENGTH_SHORT).show();
                break;
            case 2:
                //sửa
                changeAct(adapterHelper.getItem(position));

                Toast.makeText(MainActivity.this,
                        "Click edit item: " + position,
                        Toast.LENGTH_SHORT).show();
                break;
            case 3:
                //sắp xếp
                adapterHelper.sort();

                Toast.makeText(MainActivity.this,
                        "Click sort item: " + position,
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}