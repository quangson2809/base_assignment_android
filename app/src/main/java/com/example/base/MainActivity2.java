package com.example.base;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    private Button btnSave,btnMove,btnAdd;
    private EditText etName;
    private RadioGroup radioGroup;
    private RadioButton rbLike,rbDLike;
    private DatabaseHelper databaseHelper;
    private Item item;

    public void init(){
        btnSave = findViewById(R.id.btnSave);
        btnMove = findViewById(R.id.btnMove);
        btnAdd = findViewById(R.id.btnAdd);
        etName = findViewById(R.id.etName);
        radioGroup = findViewById(R.id.radioGroup);
        rbLike = findViewById(R.id.rbLike);
        rbDLike = findViewById(R.id.rbDontLike);
        databaseHelper = new DatabaseHelper(this);
        item = (Item) getIntent().getSerializableExtra("Item");
    }
    public void setListener(){
        btnSave.setOnClickListener(v -> {
            item.setName(etName.getText().toString());
            item.setIsLike(rbLike.isChecked() ? 1 : 0);
            databaseHelper.updateItem(item);
            finish();
        });

        btnMove.setOnClickListener(v -> finish());

    }
    public void setData(){
        etName.setText(item.getName() == null ? "" : item.getName());
        if(item.isLiked())
            rbLike.setChecked(true);
        else
            rbDLike.setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();
        setData();
        setListener();
    }
}