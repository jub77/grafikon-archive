package net.parostroj.timetable.model.ls.impl3;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import net.parostroj.timetable.model.TrainsData;

/**
 * Storage for train types.
 * 
 * @author jub
 */
@XmlRootElement(name = "trains_data")
@XmlType(propOrder = {"trainNameTemplate", "trainCompleteNameTemplate", "trainSortPattern"})
public class LSTrainsData {

    private String trainNameTemplate;
    private String trainCompleteNameTemplate;
    private LSSortPattern trainSortPattern;

    public LSTrainsData() {
    }

    public LSTrainsData(TrainsData data) {
        this();
        trainNameTemplate = data.getTrainNameTemplate();
        trainCompleteNameTemplate = data.getTrainCompleteNameTemplate();
        trainSortPattern = new LSSortPattern(data.getTrainSortPattern());
    }

    @XmlElement(name = "name_template")
    public String getTrainNameTemplate() {
        return trainNameTemplate;
    }

    public void setTrainNameTemplate(String trainNameTemplate) {
        this.trainNameTemplate = trainNameTemplate;
    }

    @XmlElement(name = "complete_name_template")
    public String getTrainCompleteNameTemplate() {
        return trainCompleteNameTemplate;
    }

    public void setTrainCompleteNameTemplate(String trainCompleteNameTemplate) {
        this.trainCompleteNameTemplate = trainCompleteNameTemplate;
    }

    @XmlElement(name = "sort_pattern")
    public LSSortPattern getTrainSortPattern() {
        return trainSortPattern;
    }

    public void setTrainSortPattern(LSSortPattern trainSortPattern) {
        this.trainSortPattern = trainSortPattern;
    }
    
    public TrainsData createTrainsData() {
        return new TrainsData(trainNameTemplate, trainCompleteNameTemplate, trainSortPattern.createSortPattern());
    }
}
