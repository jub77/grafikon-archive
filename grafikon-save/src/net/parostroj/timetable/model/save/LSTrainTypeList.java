package net.parostroj.timetable.model.save;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import net.parostroj.timetable.model.TrainType;
import net.parostroj.timetable.model.TrainsData;

/**
 * List of train types.
 * 
 * @author jub
 */
@XmlRootElement
public class LSTrainTypeList {
    private Map<TrainType, LSTrainType> mapping;
    
    private Map<String, TrainType> mappingByKey;
    
    private List<TrainType> trainTypeList;
    
    private TrainsData data;
    
    private String trainNameTemplate;
    
    private String trainCompleteNameTemplate;
    
    private LSSortPattern trainSortPattern;
    
    private LSTrainType[] trainType;
    
    public LSTrainTypeList() {
        mapping = new HashMap<TrainType, LSTrainType>();
        mappingByKey = new HashMap<String, TrainType>();
        trainTypeList = new LinkedList<TrainType>();
    }
    
    public LSTrainTypeList(List<TrainType> list, TrainsData data) {
        this();
        trainType = new LSTrainType[list.size()];
        int i = 0;
        for (TrainType type : list) {
            LSTrainType lsTrainType = new LSTrainType(type, Integer.toString(i));
            mapping.put(type, lsTrainType);
            trainType[i++] = lsTrainType;
        }
        trainNameTemplate = data.getTrainNameTemplate();
        trainCompleteNameTemplate = data.getTrainCompleteNameTemplate();
        trainSortPattern = new LSSortPattern(data.getTrainSortPattern());
    }
    
    public List<TrainType> getTrainTypeList() {
        return trainTypeList;
    }
    
    public TrainsData getTrainsData() {
        if (data == null)
            createData();
        return data;
    }
    
    private void createData() {
        data = new TrainsData(trainNameTemplate, trainCompleteNameTemplate, trainSortPattern != null ? trainSortPattern.getSortPattern() : null);
    }
    
    public TrainType getTrainType(String key) {
        return mappingByKey.get(key);
    }
    
    public LSTrainType getLSTrainType(TrainType trainType) {
        return mapping.get(trainType);
    }

    public LSTrainType[] getTrainType() {
        return trainType;
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

    public LSSortPattern getTrainSortPattern() {
        return trainSortPattern;
    }

    public void setTrainSortPattern(LSSortPattern trainSortPattern) {
        this.trainSortPattern = trainSortPattern;
    }

    public void setTrainType(LSTrainType[] trainType) {
        this.trainType = trainType;
        // create mappings
        for(LSTrainType lsTrainType : trainType) {
            TrainType tt = lsTrainType.convertToTrainType();
            mappingByKey.put(lsTrainType.getKey(), tt);
            trainTypeList.add(tt);
        }
    }
}
