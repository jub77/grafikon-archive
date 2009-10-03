package net.parostroj.timetable.model.ls.impl4;

import net.parostroj.timetable.model.TrainType;
import java.awt.Color;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.model.TrainTypeCategory;
import net.parostroj.timetable.utils.Conversions;

/**
 * Train type storage class.
 *
 * @author jub
 */
@XmlRootElement(name = "train_type")
@XmlType(propOrder = {"id", "abbr", "desc", "color", "category", "platform", "trainNameTemplate", "trainCompleteNameTemplate"})
public class LSTrainType {

    private String id;
    private String abbr;
    private String desc;
    private String color;
    private String category;
    private boolean platform;
    private String trainNameTemplate;
    private String trainCompleteNameTemplate;

    public LSTrainType() {
    }

    public LSTrainType(TrainType type) {
        this.id = type.getId();
        this.abbr = type.getAbbr();
        this.desc = type.getDesc();
        this.platform = type.isPlatform();
        Color c = type.getColor();
        this.color = Conversions.convertColorToText(c);
        this.category = type.getCategory().getKey();
        this.trainNameTemplate = type.getTrainNameTemplate();
        this.trainCompleteNameTemplate = type.getTrainCompleteNameTemplate();
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPlatform() {
        return platform;
    }

    public void setPlatform(boolean platform) {
        this.platform = platform;
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
    
    public TrainType createTrainType(TrainDiagram diagram) {
        TrainType type = new TrainType(id);
        type.setAbbr(abbr);
        type.setColor(Conversions.convertTextToColor(color));
        type.setDesc(desc);
        type.setPlatform(platform);
        type.setCategory(diagram.getPenaltyTable().getTrainTypeCategory(category));
        type.setTrainCompleteNameTemplate(trainCompleteNameTemplate);
        type.setTrainNameTemplate(trainNameTemplate);
        type.setTrainsData(diagram.getTrainsData());
        return type;
    }
}
