package example.haim.savingdata;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private EditText etNote;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private SharedPreferences prefs;

    int noteCounter = 1;
    int currentNote = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        etNote = (EditText) findViewById(R.id.etNote);
        fab.setOnClickListener(this);

        prefs = getSharedPreferences("Notes", MODE_PRIVATE);

        etNote.addTextChangedListener(this);
        setSupportActionBar(toolbar);

        String userName = prefs.getString("UserName", null);
        if(userName == null){
            startActivity(new Intent(this, LoginActivity.class));
        }else{
            Toast.makeText(this, "Hi, " + userName, Toast.LENGTH_SHORT).show();
        }

        loadNoteCounter();
        loadNote();
    }

    private void loadNoteCounter() {
        currentNote = prefs.getInt("CurrentNote", 1);
        noteCounter = prefs.getInt("NoteCounter", 1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_previous:
                previousNote();
                return true;
            case R.id.action_next:
                nextNote();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        saveNote();
    }

    private void loadNote() {
        String note = prefs.getString("note" + currentNote, ""/*if no data yet, give me an Empty String*/);
        etNote.setText(note);
    }

    private void saveNote() {
        //Shared Prefereces - xml - notes.xml
        // 1) get reference to sharedPreferences: -> notes.xml


        //2) use the Prefferences editor to write data in key value pairs
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("note" + currentNote, etNote.getText().toString());

        //3) commit the changes / Or apply them
        editor.apply(); //commit = save the changes immediately, applay = eventualy.

        /* this is how the Notes.xml will be generated:
        *   notes.xml
        *       <item name="note1">
        *           Hi, mom. I'm writing a note
        *       </item>
        *       <item name="UserName">
        *           usernamehaim
        *       </item>
        *
        *
        */
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        saveNote();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }



    //New Note Clicked
    @Override
    public void onClick(View v) {
        noteCounter++;
        currentNote = noteCounter;
        etNote.setText("");
        saveNoteCounter();
    }

    private void nextNote() {
        currentNote++;
        if (currentNote > noteCounter){
            currentNote = noteCounter;
            Toast.makeText(this, "Last Note", Toast.LENGTH_SHORT).show();
        }
        loadNote();
        saveNoteCounter();
    }

    private void previousNote() {
        currentNote--;
        if (currentNote < 1) {
            currentNote = 1;
            Toast.makeText(this, "The First Note", Toast.LENGTH_SHORT).show();
        }
        loadNote();
        saveNoteCounter();
    }

    private void saveNoteCounter() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("NoteCounter", noteCounter);
        editor.putInt("CurrentNote", currentNote);
        editor.commit();

    }
}
