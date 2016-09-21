package clink.firstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ToastPractice extends AppCompatActivity {

    private EditText password;
    private Button button;
    int numClicks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_practice);
        onButtonListener();
    }

    public void onButtonListener()
    {
        password = (EditText)findViewById(R.id.passwordField);
        button = (Button)findViewById(R.id.showBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numClicks++;
                if(numClicks > 3)
                {
                    Toast.makeText(ToastPractice.this, "Stop hitting the button.", Toast.LENGTH_LONG).show();
                    button.setClickable(false);
                }
                else
                {
                    Toast.makeText(ToastPractice.this, password.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
