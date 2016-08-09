package data;

/**
 * Created by yuhui on 8/5/2016.
 */
public class TestRequest {

    private int tid;
    private int release;
    private int prep;
    private int tat;
    private int analysis;
    private int deadline;

    public TestRequest(int tid, int release,
                       int prep, int tat, int analysis, int deadline) {
        this.tid = tid;
        this.release = release;
        this.prep = prep;
        this.tat = tat;
        this.analysis = analysis;
        this.deadline = deadline;
    }

    public int getTid() {
        return tid;
    }

    public int getRelease() {
        return release;
    }

    public int getDur() {
        return this.prep + this.tat + this.analysis;
    }

    public int getPrep() {
        return prep;
    }

    public int getTat() {
        return tat;
    }

    public int getAnalysis() {
        return analysis;
    }

    public int getDeadline() {
        return deadline;
    }
}
