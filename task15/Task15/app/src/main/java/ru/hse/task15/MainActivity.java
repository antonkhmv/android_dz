package ru.hse.task15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText edit1;

    final ArrayList<Item> items = new ArrayList<>();

    MyRecyclerViewAdapter adapter;

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences save = getSharedPreferences("SAVE",0);
        SharedPreferences.Editor editor = save.edit();
        editor.putString("text", edit1.getText().toString());
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit1 = findViewById(R.id.editText);
        SharedPreferences save = getSharedPreferences("SAVE",0);
        edit1.setText(save.getString("text",""));

        Button addButton = findViewById(R.id.add_button);

        SQLiteDatabase db =
                openOrCreateDatabase("DBName", MODE_PRIVATE,null);

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS MyTable5 (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " Name VARCHAR" +
                ");"
        );

        Cursor cursor = db.rawQuery("SELECT * FROM MyTable5",null);

        // Записываем все из DBName в items
        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(cursor.getColumnIndex("Name"));
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                items.add(new Item(id, data));
            }
            while(cursor.moveToNext());
        }
        cursor.close();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edit1.getText()
                        .toString()
                        // Для того чтобы строковое поле не могло помешать работе sql
                        .replaceAll("[^a-zA-Z0-9 ]","");

                edit1.setText("");
                int newId = 1 + (items.isEmpty() ? 0 : items.get(items.size()-1).id);

                db.execSQL("INSERT INTO MyTable5 VALUES('" + newId + "','" + name + "');");
                items.add(new Item(newId, name));
                adapter.notifyItemInserted(items.size()-1);
            }
        });

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.database);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.about) {
            AlertDialog.Builder dialog = new
                    AlertDialog.Builder(MainActivity.this);
            dialog.setMessage(getString(R.string.fio));
            dialog.setTitle(getString(R.string.about_text));
            dialog.setNeutralButton("OK", new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.setIcon(R.mipmap.ic_launcher_round);
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
            return true;
        }

        if (item.getItemId()==R.id.settings) {
            Intent intent = new Intent(MainActivity.this,
                    SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}