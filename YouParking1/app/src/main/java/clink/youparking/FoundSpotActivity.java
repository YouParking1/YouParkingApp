package clink.youparking;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class FoundSpotActivity extends AppCompatActivity implements GMapFragment.OnFragmentInteractionListener {
    int spotID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_spot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            spotID = extras.getInt("SPOT");
//        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Comment can go here!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        //DISABLED BACK BUTTON PRESSED
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
