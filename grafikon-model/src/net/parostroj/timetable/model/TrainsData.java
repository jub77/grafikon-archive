package net.parostroj.timetable.model;

/**
 * Data about trains' names creation a sorting.
 * 
 * @author jub
 */
public class TrainsData {

    private SortPattern trainSortPattern;
    private String trainNameTemplate;
    private String trainCompleteNameTemplate;

    public TrainsData(String trainNameTemplate, String trainCompleteNameTemplate, SortPattern trainSortPattern) {
        this.trainNameTemplate = trainNameTemplate;
        this.trainSortPattern = trainSortPattern;
        this.trainCompleteNameTemplate = trainCompleteNameTemplate;
    }

    public String getTrainNameTemplate() {
        return trainNameTemplate;
    }

    public void setTrainNameTemplate(String trainNameTemplate) {
        this.trainNameTemplate = trainNameTemplate;
    }

    public String getTrainCompleteNameTemplate() {
        return trainCompleteNameTemplate;
    }

    public void setTrainCompleteNameTemplate(String trainCompleteNameTemplate) {
        this.trainCompleteNameTemplate = trainCompleteNameTemplate;
    }

    public SortPattern getTrainSortPattern() {
        return trainSortPattern;
    }

    public void setTrainSortPattern(SortPattern trainSortPattern) {
        this.trainSortPattern = trainSortPattern;
    }
}
