package example.haim.savingdata;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText etUserName;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserName = (EditText) findViewById(R.id.etUserName);
        prefs = getSharedPreferences("Notes", MODE_PRIVATE);


    }

    public void submitUserName(View v) {
        String userName = etUserName.getText().toString();
        if(etUserName.length() < 3){
            etUserName.setError("Must be at least 3 characters");
            return;
        }
        //save the user name
        //get referense to the SharedPreferences Editor
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("UserName", userName);
        editor.commit();

        //go back to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
