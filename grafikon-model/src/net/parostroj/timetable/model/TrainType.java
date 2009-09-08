package net.parostroj.timetable.model;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import org.mvel2.templates.TemplateRuntime;

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
    /** Brake type. */
    private SpeedingBrakingType sbType;
    /** Needs platform in the station. */
    private boolean platform;
    /** Template for train name. */
    private String trainNameTemplate;
    /** Template for complete train name. */
    private String trainCompleteNameTemplate;
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
     * @return brake type
     */
    public SpeedingBrakingType getSbType() {
        return sbType;
    }

    /**
     * @param sbType sets brake type
     */
    public void setSbType(SpeedingBrakingType sbType) {
        this.sbType = sbType;
    }

    /**
     * @return train's name template
     */
    public String getTrainNameTemplate() {
        return trainNameTemplate;
    }

    /**
     * @param trainNameTemplate sets train's name template
     */
    public void setTrainNameTemplate(String trainNameTemplate) {
        this.trainNameTemplate = trainNameTemplate;
    }

    /**
     * @return train's complete name template
     */
    public String getTrainCompleteNameTemplate() {
        return trainCompleteNameTemplate;
    }

    /**
     * @param trainCompleteNameTemplate sets template with complete train's name
     */
    public void setTrainCompleteNameTemplate(String trainCompleteNameTemplate) {
        this.trainCompleteNameTemplate = trainCompleteNameTemplate;
    }

    /**
     * formats the train's name according to template.
     * 
     * @param train train
     * @return formatted train's name
     */
    public String formatTrainName(Train train) {
        String template = (trainNameTemplate == null) ?
            trainsData.getTrainNameTemplate() :
            trainNameTemplate;
        return (String)TemplateRuntime.eval(template, this.createVariablesForTemplate(train));
    }

    /**
     * formats the train's complete name according to template.
     * 
     * @param train train
     * @return formatted complete train's name
     */
    public String formatTrainCompleteName(Train train) {
        String template = (trainCompleteNameTemplate == null) ?
            trainsData.getTrainCompleteNameTemplate() :
            trainCompleteNameTemplate;
        return (String)TemplateRuntime.eval(template, this.createVariablesForTemplate(train));
    }
    
    private Map<String,Object> createVariablesForTemplate(Train train) {
        Map<String,Object> variables = new HashMap<String, Object>();
        variables.put("train", train);
        variables.put("type", this);
        return variables;
    }
    
    @Override
    public String toString() {
        return abbr + " - " + desc;
    }
}
