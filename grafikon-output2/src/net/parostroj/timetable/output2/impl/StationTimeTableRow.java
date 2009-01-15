package net.parostroj.timetable.output2.impl;

import java.util.LinkedList;
import java.util.List;

/**
 * Row for station timetable.
 *
 * @author jub
 */
class StationTimetableRow {

    private String trainName;
    private String from;
    private String fromTime;
    private String to;
    private String toTime;
    private String track;
    private boolean technologicalTime;
    private boolean occupied;
    private String comment;
    private List<EngineTo> engineTo;
    private List<EngineFrom> engineFrom;
    private List<TrainUnitTo> trainUnitTo;
    private List<TrainUnitFrom> trainUnitFrom;
    private LengthInfo length;

    public StationTimetableRow() {
    }

    public StationTimetableRow(String trainName, String from, String fromTime, String to, String toTime, String track) {
        this.trainName = trainName;
        this.from = from;
        this.fromTime = fromTime;
        this.to = to;
        this.toTime = toTime;
        this.track = track;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public boolean isTechnologicalTime() {
        return technologicalTime;
    }

    public void setTechnologicalTime(boolean technologicalTime) {
        this.technologicalTime = technologicalTime;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<EngineFrom> getEngineFrom() {
        if (engineFrom == null) {
            engineFrom = new LinkedList<EngineFrom>();
        }
        return engineFrom;
    }

    public void setEngineFrom(List<EngineFrom> engineFrom) {
        this.engineFrom = engineFrom;
    }

    public List<EngineTo> getEngineTo() {
        if (engineTo == null) {
            engineTo = new LinkedList<EngineTo>();
        }
        return engineTo;
    }

    public void setEngineTo(List<EngineTo> engineTo) {
        this.engineTo = engineTo;
    }

    public List<TrainUnitFrom> getTrainUnitFrom() {
        if (trainUnitFrom == null) {
            trainUnitFrom = new LinkedList<TrainUnitFrom>();
        }
        return trainUnitFrom;
    }

    public void setTrainUnitFrom(List<TrainUnitFrom> trainUnitFrom) {
        this.trainUnitFrom = trainUnitFrom;
    }

    public List<TrainUnitTo> getTrainUnitTo() {
        if (trainUnitTo == null) {
            trainUnitTo = new LinkedList<TrainUnitTo>();
        }
        return trainUnitTo;
    }

    public void setTrainUnitTo(List<TrainUnitTo> trainUnitTo) {
        this.trainUnitTo = trainUnitTo;
    }

    public LengthInfo getLength() {
        return length;
    }

    public void setLength(LengthInfo length) {
        this.length = length;
    }
}
