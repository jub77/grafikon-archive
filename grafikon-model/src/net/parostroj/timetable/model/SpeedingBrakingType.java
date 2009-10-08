package net.parostroj.timetable.model;

public enum SpeedingBrakingType {
    FREIGHT, PASSENGER;
    
    public static SpeedingBrakingType fromString(String id) {
        for (SpeedingBrakingType type : values()) {
            if (type.toString().equals(id))
                return type;
        }
        return null;
    }
}
