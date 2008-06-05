/*
 * TrainTimetablesPage.java
 * 
 * Created on 8.9.2007, 16:29:45
 */
package net.parostroj.timetable.output;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

/**
 * One page for train timetable list.
 * 
 * @author jub
 */
public class TrainTimetablesPage implements Page {
    
    private List<TrainTimetable> trains;
    
    private int number;
    
    private int pageLength;
    
    private int actualLength;
    
    private TrainTimetablesListTemplates templates;
    
    public TrainTimetablesPage(int pageLength, TrainTimetablesListTemplates templates) {
        trains = new LinkedList<TrainTimetable>();
        this.templates = templates;
        this.pageLength = pageLength;
        this.actualLength = 0;
    }
    
    public boolean addTrainTimetable(TrainTimetable timetable) {
        if ((timetable.getLength() + actualLength + 10) <= pageLength) {
            trains.add(timetable);
            actualLength = actualLength 
                    + timetable.getLength();
            return true;
        } else {
            return false;
        }
        
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    public void writeTo(Writer writer) throws IOException {
        for (TrainTimetable item : trains) {
            item.writeTo(writer, this);
        }
    }
}
