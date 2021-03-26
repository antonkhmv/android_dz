package ru.hse.task15;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.hse.task15.R;

import static android.content.Context.MODE_PRIVATE;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private List<Item> mData;
    final Context context;

    public List<Item> getData() {
        return mData;
    }

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, List<Item> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view, context, this);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mData.get(position);
        holder.myTextView.setText(item.name);
        holder.setItem(item);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        Item item;

        void setItem(Item item) {
            this.item = item;
        }

        Context context;

        MyRecyclerViewAdapter adapter;

        List<Item> items;

        ViewHolder(View itemView, Context context, MyRecyclerViewAdapter adapter) {
            super(itemView);
            this.context = context;
            this.adapter = adapter;
            items = adapter.getData();

            myTextView = itemView.findViewById(R.id.data_text);
            final Button editButton = itemView.findViewById(R.id.edit_button);
            final Button deleteButton = itemView.findViewById(R.id.delete_button);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SQLiteDatabase db =
                            context.openOrCreateDatabase("DBName",MODE_PRIVATE,null);
                    db.execSQL("DELETE FROM MyTable5 WHERE _id=" + item.id + "");
                    db.close();

                    int position = items.indexOf(item);
                    items.remove(position);
                    adapter.notifyItemRemoved(position);

                    Toast.makeText(context, "row deleted from the db id=" + item.id,
                            Toast.LENGTH_LONG).show();
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                            .setMessage("Enter new value:")
                            .setTitle("Changing the item");

                        LayoutInflater inflater =
                                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        View dialogview = inflater.inflate(R.layout.dialog,null);

                        dialog.setView(dialogview);
                        EditText dialogEdit = dialogview.findViewById(R.id.editTextDialog);
                        dialogEdit.setText(item.name);
                        dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int i) {

                                String newName = dialogEdit.getText().toString()
                                        // Для того чтобы строковое поле не могло помешать работе sql
                                        .replaceAll("[^a-zA-Z0-9 ]","");

                                SQLiteDatabase db =
                                        context.openOrCreateDatabase("DBName",MODE_PRIVATE,null);
                                db.execSQL("UPDATE MyTable5 SET Name='" + newName + "' " +
                                        "WHERE _id=" + item.id +"");
                                db.close();

                                int position = items.indexOf(item);
                                items.set(position, new Item(item.id, newName));
                                adapter.notifyItemChanged(position);

                                Toast.makeText(context, "row edited from the db row id=" + item.id,
                                        Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });

                        dialog.setIcon(R.mipmap.ic_launcher_round);
                        AlertDialog alertDialog = dialog.create();
                        alertDialog.show();
                    }
                }
            );


        }
    }

}