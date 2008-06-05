package net.parostroj.timetable.model;

public enum Scale {

    H0(87), TT(120), N(160);

    private int ratio;

    private Scale(int ratio) {
        this.ratio = ratio;
    }

    public int getRatio() {
        return ratio;
    }
}
