package clink.youparking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class VerifyEmail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
    }

    public void registerVehicle(View view)
    {
        String email_code = "";
        String database_code = "";

        if(email_code == database_code) {
            Intent intent = new Intent(this, VehicleRegistrationActivity.class);
            startActivity(intent);
        }
        else
        {
            Context context = getApplicationContext();
            CharSequence text = "Wrong verification code!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}
