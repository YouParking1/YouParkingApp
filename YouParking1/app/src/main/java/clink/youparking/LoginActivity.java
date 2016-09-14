package clink.youparking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements AsyncResponse {
    EditText emailEt, passwordEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        String Username = preferences.getString("Username", "");
        if(Username.length() != 0)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEt = (EditText) findViewById(R.id.email);
        passwordEt = (EditText) findViewById(R.id.pass);
        TextView tvSignUp =(TextView) findViewById(R.id.tvSignUp);
    }

    public void goToRegistration(View view)
    {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void OnLogin(View view) {
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        String type = "login";

        if (email.toUpperCase().contains(".EDU") && email.contains("@")) {
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.delegate = this;
            backgroundWorker.execute(type, email, password);
        }
    }

    @Override
    public void processFinish(String output) {
        if (output.contains("success")) {
            User.email = emailEt.getText().toString();
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("Username", User.email);
            editor.commit();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}