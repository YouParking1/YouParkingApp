package clink.youparking;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by acous on 8/28/2016.
 */
public class User {


    public static String email = "a@troy.edu";
    public static String school = "Troy University";
    public static String fName = null;
    public static String lName = null;
    public static LatLng myLocation = null;
    public static int points = 0;
    public static boolean failedLogin = false;
    public static ArrayList<LatLng> heldLocation = new ArrayList<>();
}