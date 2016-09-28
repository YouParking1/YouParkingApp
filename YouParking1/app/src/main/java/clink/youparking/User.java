package clink.youparking;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by acous on 8/28/2016.
 */
public class User {

    public static String email = null;
    public static String school = null;
    public static String fName = null;
    public static String lName = null;

    public static int percentage = 0;
    public static int spotsHeld = 0;

    public static LatLng myLocation = null;

    public static int points = 0;

    public static boolean failedLogin = false;
    public static ArrayList<LatLng> heldLocation = new ArrayList<>();

    public static long time = 0;

    public static ArrayList<Spot> spots = new ArrayList<>();

}