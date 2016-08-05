package data;

/**
 * Created by yuhui on 8/5/2016.
 */
public class TestRequest {

    private int tid;
    private int release;
    private int dur;
    private int deadline;

    public TestRequest(int tid, int release, int dur, int deadline) {
        this.tid = tid;
        this.release = release;
        this.dur = dur;
        this.deadline = deadline;
    }

    public int getTid() {
        return tid;
    }

    public int getRelease() {
        return release;
    }

    public int getDur() {
        return dur;
    }

    public int getDeadline() {
        return deadline;
    }
}
