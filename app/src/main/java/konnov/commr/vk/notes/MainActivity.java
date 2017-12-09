package konnov.commr.vk.notes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView notesListView;
    ArrayList notesArrayList;
    ArrayAdapter<String> notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
    }


    private void initialization(){
        notesArrayList = new ArrayList<String>();
        final DBHelper dbHelper = new DBHelper(this);
//        dbHelper.insertData("Android is cool");
//        dbHelper.insertData("So is IOS");
//        dbHelper.insertData("And java");
//        dbHelper.deleteDB();
        notesArrayList = dbHelper.dbToList();
        notesListView = findViewById(R.id.notesListView);
        notesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notesArrayList);
        notesListView.setAdapter(notesAdapter);


        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, NoteEditingActivity.class);
                intent.putExtra("noteId", i);
                startActivity(intent);
            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int itemToDelete = i;

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dbHelper.deleteItemFromDB(itemToDelete);
                                        initialization();
                                    }
                                }
                        )
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }

        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        initialization();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_menu_button){
            notesArrayList.add("");
            DBHelper dbHelper = new DBHelper(this);
            dbHelper.insertData("");
            int i = notesArrayList.size()-1;
            Intent intent = new Intent(MainActivity.this, NoteEditingActivity.class);
            intent.putExtra("noteId", i);
            startActivity(intent);
        }

        return false;
    }
}
