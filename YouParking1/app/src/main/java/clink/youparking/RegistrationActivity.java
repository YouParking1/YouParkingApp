package clink.youparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void goToVehicleRegistration(View view)
    {
        Intent intent = new Intent(this, VehicleRegistrationActivity.class);
        startActivity(intent);
    }
}
