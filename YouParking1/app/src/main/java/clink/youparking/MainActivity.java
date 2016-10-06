package clink.youparking;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener, VehiclesFragment.OnFragmentInteractionListener,
        MyBidsFragment.OnFragmentInteractionListener, FindNowFragment.OnFragmentInteractionListener,
        HoldSpotFragment.OnFragmentInteractionListener, FindLaterFragment.OnFragmentInteractionListener, HoldLaterFragment.OnFragmentInteractionListener,
        SignOutFragment.OnFragmentInteractionListener, GMapFragment.OnFragmentInteractionListener, HoldSpotMapFragment.OnFragmentInteractionListener,
        MapInteraction, HoldLaterMapFragment.OnFragmentInteractionListener, DynamicSpot.OnFragmentInteractionListener, Achievements.OnFragmentInteractionListener,
        AsyncResponse {

    TextView numTickets;
    String outputFromProcess = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = HomeFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //This bit of code is for setting ticket icon in nav header.
        View hView = navigationView.getHeaderView(0);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        TextView text = (TextView) hView.findViewById(R.id.inMenu);
        text.setTypeface(font);

        numTickets = (TextView)findViewById(R.id.numTickets);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed(); Disabled so the user cannot return to the login activity.
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_home) {
            fragmentClass = HomeFragment.class;
        } else if (id == R.id.nav_vehicles) {
            fragmentClass = VehiclesFragment.class;
        } else if (id == R.id.nav_my_bids) {
            fragmentClass = MyBidsFragment.class;
        } else if (id == R.id.nav_find_now) {
            fragmentClass = FindNowFragment.class;
        } else if (id == R.id.nav_hold_spot) {
            fragmentClass = HoldSpotMapFragment.class;
        } else if (id == R.id.nav_find_later) {
            fragmentClass = FindLaterFragment.class;
        } else if (id == R.id.nav_hold_later) {
            fragmentClass = HoldLaterMapFragment.class;
        } else if (id == R.id.nav_sign_out) {
            fragmentClass = SignOutFragment.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * finishHold & recheckSpot are functions for HoldSpotMapFragment
     * finishHold will take your coordinates and send them HoldSpotFragment.
     *
     * recheckSpot will reload the HoldSpotMapFragment in order obtain your current
     * coordinates after error.
     * @param view
     */
    @Override
    public void finishHold(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new HoldSpotFragment()).commit();
    }

    @Override
    public void recheckSpot(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new HoldSpotMapFragment()).commit();
    }

    public void finishHoldLater(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new HoldLaterFragment()).commit();
    }

    public void recheckSpotLater(View view)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new HoldLaterMapFragment()).commit();
    }

    public void goToAchievements(View view)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new Achievements()).commit();
    }

    /**
     * Takes content in HoldSpotFragment and sends it to database.
     * @param view
     */
    public void onHold(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.holdPointsSpinner);
        String choice = spinner.getSelectedItem().toString();

        EditText editText = (EditText) findViewById(R.id.holdSpotComments);
        String holdComments = editText.getText().toString();

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.delegate = this;

        backgroundWorker.execute("hold", choice, "1", Double.toString(User.myLocation.latitude),
                Double.toString(User.myLocation.longitude), holdComments);
    }

    public void onHoldLater(View view)
    {
        EditText editText = (EditText) findViewById(R.id.holdSpotLaterComments);
        String holdLaterComments = editText.getText().toString();

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.delegate = this;

        //TODO: Travis Clinkscales - Change the second argument to something else
        backgroundWorker.execute("hold_later", "bid", Long.toString(User.time), "1", Double.toString(User.myLocation.latitude),
                Double.toString(User.myLocation.longitude), holdLaterComments);
    }

    public void onSetTimeButtonClicked(View view){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "TimePicker");
    }

    @Override
    public void processFinish(String output) throws JSONException {
        outputFromProcess = output;
    }

    /**
     * FUNCTIONS FOR FIND NOW
     * @param view
     */
    public void buySpot(View view) {
        if (User.spots.get(view.getId()).getPoints() <= User.points) {
            System.out.println("Email: " + User.email + " " + "Holder: " + User.spots.get(view.getId()).getHolder_email() +
                " " + "Spots: " + User.spots.get(view.getId()).getPoints());

//            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
//            backgroundWorker.delegate = this;
//            backgroundWorker.execute("exchange", User.spots.get(view.getId()).getHolder_email(),
//                    Integer.toString(User.spots.get(view.getId()).getPoints()));
            //TODO: UNCOMMENT ABOVE CODE

            Intent intent = new  Intent(this, AaronTestActivity.class);
            intent.putExtra("ROOM", User.spots.get(view.getId()).getHolder_email());
            System.out.println(User.spots.get(view.getId()).getHolder_email());
            startActivity(intent);
        }
        else {
            Toast toast = Toast.makeText(this, "Not Enough Points", Toast.LENGTH_LONG);
            toast.show();
        }
//        double sLat, sLong;
//        sLat = User.spots.get(view.getId()).getLatitude();
//        sLong = User.spots.get(view.getId()).getLongitude();
//
//        Intent intent = new Intent(this, FoundSpotActivity.class);
//        intent.putExtra("LAT", sLat);
//        intent.putExtra("LONG", sLong);
//        startActivity(intent);

    }


    /**
     * Adjusts map to clicked HeldSpot in the FindNowMapFragment
     * @param view
     */
    public void moveMap(View view) {
        int i = 0;
        boolean notFound = true;
        while ((i < getSupportFragmentManager().getFragments().size()) && notFound) {
            String fragmentClass = getSupportFragmentManager().getFragments().get(i).getClass().toString();
            if (fragmentClass.contains("FindNowFragment")) {
                notFound = false;
            }
            else
                i++;
        }
        int j = 0;
        while ((j < getSupportFragmentManager().getFragments().get(i).getChildFragmentManager().getFragments().size()) && !notFound) {
            if (getSupportFragmentManager().getFragments().get(i).getChildFragmentManager().getFragments().get(j).getClass().toString()
                    .contains("GMapFragment")) {
                notFound = true;
            }
            else {
                j++;
            }
        }

        GMapFragment gmap = (GMapFragment) getSupportFragmentManager().getFragments().get(i).getChildFragmentManager()
                .getFragments().get(j);
        gmap.setToSpotClicked(view.getId());
    }


}