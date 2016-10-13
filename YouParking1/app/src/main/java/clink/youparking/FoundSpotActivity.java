package clink.youparking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.json.JSONException;

public class FoundSpotActivity extends AppCompatActivity implements HoldingMapFragment.OnFragmentInteractionListener,
        GMapFragment.OnFragmentInteractionListener, AsyncResponse{
    int spotID = -1;
    String role = "";
    String transactionID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            spotID = extras.getInt("SpotID");
            role = extras.getString("Role");
            transactionID = extras.getString("TransID");
        }

        super.onCreate(savedInstanceState);

        if (role.equals("Buyer"))
            setContentView(R.layout.activity_found_spot);
        else if (role.equals("Holder"))
            setContentView(R.layout.activity_holding_spot);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Comment can go here!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


//        if (role.equals("Holder")) {
//            Fragment fragment = null;
//            Class fragmentClass = null;
//            fragmentClass = HoldingMapFragment.class;
//            try {
//                fragment = (Fragment) fragmentClass.newInstance();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit();
//        }

    }

    @Override
    public void onBackPressed() {
        //DISABLED BACK BUTTON PRESSED
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void endTransaction(View view) {
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.delegate = this;
        backgroundWorker.execute("BuyNowComplete", transactionID);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    @Override
    public void processFinish(String output) throws JSONException {

    }
}
