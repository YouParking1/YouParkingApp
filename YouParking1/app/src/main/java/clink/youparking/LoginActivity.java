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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements AsyncResponse {
    EditText emailEt, passwordEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        String Username = preferences.getString("Username", "");
        String fName = preferences.getString("first_name", "");
        String lName = preferences.getString("last_name", "");
        String school = preferences.getString("University", "");
        if(Username.length() != 0)
        {
            User.email = Username;
            User.fName = fName;
            User.lName = lName;
            User.school = school;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent); //DELETE THIS COMMENT
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
    public void processFinish(String output) throws JSONException {

        JSONObject jsonObject = new JSONObject(output);
        String strLoginID = jsonObject.optString("Email");
        String strSchool = jsonObject.optString("University");
        String strFName = jsonObject.optString("FName");
        String strLName = jsonObject.optString("LName");
        User.points = jsonObject.optInt("Points");
        User.email = strLoginID;
        User.school = strSchool;
        User.fName = strFName;
        User.lName = strLName;

        //System.out.println(User.email + " " + User.school + " " + User.fName + " " + User.lName + " " + User.points);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Username", User.email);
        editor.putString("first_name", User.fName);
        editor.putString("last_name", User.lName);
        editor.putString("University", User.school);
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}