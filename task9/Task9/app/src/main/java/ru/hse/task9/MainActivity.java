package ru.hse.task9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.text1);
        registerForContextMenu(textView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.contextmenu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.color_red){
            TextView textView = findViewById(R.id.text1);
            textView.setTextColor(Color.parseColor("red"));
        }
        else if (id == R.id.color_black){
            TextView textView = findViewById(R.id.text1);
            textView.setTextColor(Color.parseColor("black"));
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.hide_text){
            if (item.isChecked()){

                TextView textView = findViewById(R.id.text1);
                textView.setVisibility(TextView.VISIBLE);
                item.setChecked(false);
            }
            else {
                TextView textView = findViewById(R.id.text1);
                textView.setVisibility(TextView.INVISIBLE);
                item.setChecked(true);
            }
        }
        else if (id == R.id.about) {
            Toast.makeText(MainActivity.this, R.string.fio,
                    Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}

