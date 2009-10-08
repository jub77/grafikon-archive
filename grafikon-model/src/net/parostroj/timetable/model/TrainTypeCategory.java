package net.parostroj.timetable.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Category of train types - freight, passenger ...
 *
 * @author jub
 */
public class TrainTypeCategory {
    private static final List<TrainTypeCategory> predefined;

    static {
        predefined = Collections.unmodifiableList(Arrays.asList(new TrainTypeCategory("passenger"), new TrainTypeCategory("freight")));
    }

    private final String key;

    private TrainTypeCategory(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static List<TrainTypeCategory> getPredefined() {
        return predefined;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TrainTypeCategory other = (TrainTypeCategory) obj;
        if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.key != null ? this.key.hashCode() : 0);
        return hash;
    }

    public static TrainTypeCategory fromString(String str) {
        for (TrainTypeCategory item : getPredefined()) {
            if (item.getKey().equals(str))
                return item;
        }
        return new TrainTypeCategory(str);
    }

    @Override
    public String toString() {
        return "category<key:" + key + ">";
    }
}
