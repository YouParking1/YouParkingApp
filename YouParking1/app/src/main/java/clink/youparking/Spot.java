package clink.youparking;

/**
 * Created by Aaron on 9/26/2016.
 */
public class Spot {

    private double latitude;
    private double longitude;

    private int points;
    private int holder_car;
    private int holder_percentage;
    private int holder_spots_held;
    private int time;

    private String holder_email;
    private String comments;

    public Spot(double slat, double slong, int point, int hc, String email, String comment, int percent, int spots,
                int time) {

        this.latitude = slat;
        this.longitude = slong;
        this.points = point;
        this.holder_car = hc;
        this.holder_email = email;
        this.comments = comment;
        this.holder_percentage = percent;
        this.holder_spots_held = spots;
        this.time = time;

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getHolder_car() {
        return holder_car;
    }

    public int getPoints() {
        return points;
    }

    public String getComments() {
        return comments;
    }

    public String getHolder_email() {
        return holder_email;
    }

    public int getHolder_percentage() {
        return holder_percentage;
    }

    public int getHolder_spots_held() {
        return holder_spots_held;
    }

    public int getTime() {
        return time;
    }
}
