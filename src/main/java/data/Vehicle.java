package data;

/**
 * Created by yuhui on 8/5/2016.
 */
public class Vehicle {

    private int vid;
    private int release;

    public Vehicle(int vid, int release) {
        this.vid = vid;
        this.release = release;
    }

    public int getVid() {
        return vid;
    }

    public int getRelease() {
        return release;
    }
}
