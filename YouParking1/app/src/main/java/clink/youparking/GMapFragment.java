package clink.youparking;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private double myLat = 0, myLong = 0;
    private double holdLat = 0, holdLong = 0;

    public static final String TAG = GMapFragment.class.getSimpleName();
    private LocationRequest mLocationRequest;

    private String mapType;
    private String room = null; // FOR USE WITH REAL TIME NAVIGATION TRACKING

    private int spotID = -1;

    JSONObject jsonSend;
    private PolylineOptions mPolylineOptions;

    private Socket mSocket;
    {
            try {
                mSocket = IO.socket("http://108.167.99.14:88");
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GMapFragment newInstance(String param1, String param2) {
        GMapFragment fragment = new GMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mapType = getArguments().getString("TYPE");

        if (mapType.equals("HOLD")) { //IF USER IS HOLDING A SPOT
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)
                    .setFastestInterval(1 * 1000);
        }
        else if (mapType.equals("BOUGHT")) { // IF USER IS JUST BOUGHT A SPOT
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)
                    .setFastestInterval(1 * 1000);


            Bundle extras = getActivity().getIntent().getExtras();
            spotID = extras.getInt("SpotID");

            mSocket.connect();
            mSocket.on("message", onNewMessage);
            mSocket.emit("login", User.email);
            mSocket.emit("joinRoom", User.spots.get(spotID).getHolder_email());

        }
        else {
            // TODO: ADD CODE FOR FINDING SCHOOLS LOCATION AND SETTING CENTRAL VIEW TO THOSE COORDINATES
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_gmap, container, false);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map222);
        fragment.getMapAsync(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        myLat = mLastLocation.getLatitude();
        myLong = mLastLocation.getLongitude();
        if (mapType.equals("BOUGHT")) {
            //IF BOUGHT, DON'T DISABLE LOCATION UPDATES
        }
        else {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        setCurrentLoc();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {



        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng troy = new LatLng(31.7988, -85.9574);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(troy));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

        if (mapType.equals("FIND")) {
            int size = getArguments().getDoubleArray("LATS").length;
            double lats [] = getArguments().getDoubleArray("LATS");
            double longs [] = getArguments().getDoubleArray("LONGS");
            int points [] = getArguments().getIntArray("POINTS");

            for (int i = 0; i < size; i++) {
                LatLng latLng = new LatLng(lats[i], longs[i]);
                mMap.addMarker(new MarkerOptions().position(latLng).title(Integer.toString(points[i])));
            }
        }
        else if (mapType.equals("BOUGHT")) {
            holdLat = User.spots.get(spotID).getLatitude();
            holdLong = User.spots.get(spotID).getLongitude();

            LatLng loc = new LatLng(holdLat, holdLong);
            mMap.addMarker(new MarkerOptions().position(loc).title("SPOT DESTINATION"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
            initializeMap();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setCurrentLoc() {
        if (mapType.equals("HOLD")) {
            LatLng loc = new LatLng(myLat, myLong);
            User.myLocation = loc;
            mMap.addMarker(new MarkerOptions().position(loc));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        }
    }

    public void setToSpotClicked(int index) {
        double mLat = User.spots.get(index).getLatitude();
        double mLong = User.spots.get(index).getLongitude();
        LatLng loc = new LatLng(mLat, mLong);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
    }

    @Override
    public void onStart() {
        if (mapType.equals("HOLD") || mapType.equals("BOUGHT"))
            mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        mSocket.disconnect();
        mSocket.off("new message", onNewMessage);
    }


    /**
     *
     * onLocationChanged will take users location change, format to JSONObject and send data to
     * Socket.IO
     *
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        jsonSend = new JSONObject();

        if (mapType.equals("BOUGHT")) {
            double newLat = location.getLatitude();
            double newLong = location.getLongitude();

            try {
                jsonSend.put("LAT", newLat);
                jsonSend.put("LONG", newLong);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            mSocket.emit("message", jsonSend); //EMIT NEW LOCATION AS JSON
            //realTimeMap(newLat, newLong);
        }
    }


    /**
     * Used to update locations of holder and buyer in real time.
     *
     *
     */
    private void realTimeMap(double newLat, double newLong) {

            LatLng loc = new LatLng(newLat, newLong);
            updatePolyline(loc);
            mMap.addMarker(new MarkerOptions().position(loc).title("NEW LOCATION"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));

    }

    public void updatePolyline(LatLng mlatlng) {
        mMap.clear();
        mMap.addPolyline(mPolylineOptions.add(mlatlng));
    }

    private void initializeMap() {
        mPolylineOptions = new PolylineOptions();
        mPolylineOptions.color(Color.BLUE).width(10);
    }

    /**
     * If location arrives from Socket.IO
     */
    private Emitter.Listener onNewMessage = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            final Object arg = args[0];
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    JSONObject data = (JSONObject) arg;

                    System.out.println(data.toString());
                    double newLat = 0;
                    double newLong = 0;

                    try {
                        newLat = data.getDouble("LAT");
                        newLong = data.getDouble("LONG");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    if (mMap != null) {
                        realTimeMap(newLat, newLong);
                    }

                }
            });
        }
    };
}
