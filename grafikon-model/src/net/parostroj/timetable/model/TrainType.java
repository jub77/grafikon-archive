package net.parostroj.timetable.model;

import java.awt.Color;

/**
 * Train type.
 *
 * @author jub
 */
public class TrainType implements ObjectWithId {
    /** Id. */
    private final String id;
    /** Abbreviation of the type. */
    private String abbr;
    /** Description of the type. */
    private String desc;
    /** Color for GT. */
    private Color color;
    /** Category. */
    private TrainTypeCategory category;
    /** Needs platform in the station. */
    private boolean platform;
    /** Template for train name. */
    private TextTemplate trainNameTemplate;
    /** Template for complete train name. */
    private TextTemplate trainCompleteNameTemplate;
    /** Reference to trains data. */
    private TrainsData trainsData;
    
    /**
     * creates instance.
     * 
     * @param id id
     */
    public TrainType(String id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    @Override
    public String getId() {
        return id;
    }

    public TrainsData getTrainsData() {
        return trainsData;
    }

    public void setTrainsData(TrainsData trainsData) {
        this.trainsData = trainsData;
    }

    /**
     * @return the abbreviation
     */
    public String getAbbr() {
        return abbr;
    }

    /**
     * @param abbr the abbreviation to set
     */
    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    /**
     * @return color for GT
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to be set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return description of the type
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc description to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return if the type needs platform in the station
     */
    public boolean isPlatform() {
        return platform;
    }

    /**
     * @param platform sets if the type needs the platform in the station
     */
    public void setPlatform(boolean platform) {
        this.platform = platform;
    }

    /**
     * @return category of train type
     */
    public TrainTypeCategory getCategory() {
        return category;
    }

    /**
     * @param category sets category of train type
     */
    public void setCategory(TrainTypeCategory category) {
        this.category = category;
    }

    /**
     * @return train's name template
     */
    public TextTemplate getTrainNameTemplate() {
        return trainNameTemplate;
    }

    /**
     * @param trainNameTemplate sets train's name template
     */
    public void setTrainNameTemplate(TextTemplate trainNameTemplate) {
        this.trainNameTemplate = trainNameTemplate;
    }

    /**
     * @return train's complete name template
     */
    public TextTemplate getTrainCompleteNameTemplate() {
        return trainCompleteNameTemplate;
    }

    /**
     * @param trainCompleteNameTemplate sets template with complete train's name
     */
    public void setTrainCompleteNameTemplate(TextTemplate trainCompleteNameTemplate) {
        this.trainCompleteNameTemplate = trainCompleteNameTemplate;
    }

    /**
     * formats the train's name according to template.
     * 
     * @param train train
     * @return formatted train's name
     */
    public String formatTrainName(Train train) {
        TextTemplate template = (trainNameTemplate == null) ?
            trainsData.getTrainNameTemplate() :
            trainNameTemplate;
        return template.evaluate(train, train.createTemplateBinding());
    }

    /**
     * formats the train's complete name according to template.
     * 
     * @param train train
     * @return formatted complete train's name
     */
    public String formatTrainCompleteName(Train train) {
        TextTemplate template = (trainCompleteNameTemplate == null) ?
            trainsData.getTrainCompleteNameTemplate() :
            trainCompleteNameTemplate;
        return template.evaluate(train, train.createTemplateBinding());
    }
    
    @Override
    public String toString() {
        return abbr + " - " + desc;
    }
}
