package net.parostroj.timetable.model;

public enum TimeIntervalType {
    
    LINE_START_THROUGH, LINE_THROUGH_STOP, NODE_THROUGH, NODE_STOP, NODE_START, NODE_END, LINE_START_STOP, LINE_THROUGH;
    
    public boolean isNextStop() {
        return (this == LINE_THROUGH_STOP || this == LINE_START_STOP);
    }
    
    public TimeIntervalType changeToNextStop() {
        if (this == LINE_THROUGH)
            return LINE_THROUGH_STOP;
        if (this == LINE_START_THROUGH)
            return LINE_START_STOP;
        return this;
    }
    
    public TimeIntervalType changeToNextThrough() {
        if (this == LINE_START_STOP)
            return LINE_START_THROUGH;
        if (this == LINE_THROUGH_STOP)
            return LINE_THROUGH;
        return this;
    }

    public boolean isPreviousStop() {
        return (this == LINE_START_STOP || this == LINE_START_THROUGH);
    }
    
    public TimeIntervalType changeToPreviousStop() {
        if (this == LINE_THROUGH)
            return LINE_START_THROUGH;
        if (this == LINE_THROUGH_STOP)
            return LINE_START_STOP;
        return this;
    }
    
    public TimeIntervalType changeToPreviousThrough() {
        if (this == LINE_START_STOP)
            return LINE_THROUGH_STOP;
        if (this == LINE_START_THROUGH)
            return LINE_THROUGH;
        return this;
    }

    public boolean isStop() {
        return this==NODE_END || this==NODE_START || this==NODE_STOP;
    }
}
