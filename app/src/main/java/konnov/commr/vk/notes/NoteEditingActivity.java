package konnov.commr.vk.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class NoteEditingActivity extends AppCompatActivity {

    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editing);
        initialization();
    }

    private void initialization(){
        final DBHelper dbHelper = new DBHelper(this);
        editText = (EditText) findViewById(R.id.editText);
        Intent intent = getIntent();
        final int noteId = intent.getIntExtra("noteId", -1);
        editText.setText("" + noteId);
        editText.setText(dbHelper.dbNoteItemToString(noteId));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dbHelper.changeNote(noteId, String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
