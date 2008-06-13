package net.parostroj.timetable.model;

import java.util.*;

/**
 * Collection of all parts of graphical timetable.
 *
 * @author jub
 */
public class TrainDiagram implements AttributesHolder, ObjectWithId {

    /** Id. */
    private final String id;
    /** Net. */
    private Net net;
    /** Predefined routes. */
    private List<Route> routes;
    /** Trains. */
    private List<Train> trains;
    /** Cycles. */
    private Map<TrainsCycleType, List<TrainsCycle>> cycles;
    /** List of images for trains timetable. */
    private List<TimetableImage> images;
    /** Train types available. */
    private List<TrainType> trainTypes;
    /** Attributes. */
    private Attributes attributes;
    /** Trains' data. */
    private TrainsData trainsData;
    /** List of engine classes. */
    private List<EngineClass> engineClasses;
    private GTListenerTrainDiagramImpl listener;

    /**
     * Default constructor.
     */
    public TrainDiagram(String id, TrainsData data) {
        this.id = id;
        routes = new ArrayList<Route>();
        trains = new ArrayList<Train>();
        cycles = new EnumMap<TrainsCycleType, List<TrainsCycle>>(TrainsCycleType.class);
        images = new LinkedList<TimetableImage>();
        engineClasses = new LinkedList<EngineClass>();
        this.trainTypes = new LinkedList<TrainType>();
        this.attributes = new Attributes();
        this.trainsData = data;
        this.listener = new GTListenerTrainDiagramImpl(this);
    }

    /**
     * @return net
     */
    public Net getNet() {
        return net;
    }

    /**
     * @param net net to be set
     */
    public void setNet(Net net) {
        this.net = net;
    }

    /**
     * @return predefined routes
     */
    public List<Route> getRoutes() {
        return Collections.unmodifiableList(this.routes);
    }

    public void addRoute(Route route) {
        this.routes.add(route);
    }

    public void removeRoute(Route route) {
        this.routes.remove(route);
    }

    public Route getRouteById(String id) {
        for (Route route : routes) {
            if (route.getId().equals(id)) {
                return route;
            }
        }
        return null;
    }

    /**
     * @return the trains
     */
    public List<Train> getTrains() {
        return Collections.unmodifiableList(this.trains);
    }

    public void addTrain(Train train) {
        train.addListener(listener);
        train.attach();
        this.trains.add(train);
    }

    public void removeTrain(Train train) {
        train.detach();
        this.trains.remove(train);
        train.removeListener(listener);
    }

    public Train getTrainById(String id) {
        for (Train train : trains) {
            if (train.getId().equals(id)) {
                return train;
            }
        }
        return null;
    }

    public Map<TrainsCycleType, List<TrainsCycle>> getCyclesMap() {
        EnumMap<TrainsCycleType, List<TrainsCycle>> modMap = new EnumMap<TrainsCycleType, List<TrainsCycle>>(TrainsCycleType.class);
        for (Map.Entry<TrainsCycleType, List<TrainsCycle>> entry : cycles.entrySet()) {
            modMap.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
        }
        return Collections.unmodifiableMap(cycles);
    }

    public List<TrainsCycle> getCycles(TrainsCycleType type) {
        return Collections.unmodifiableList(this.getCyclesIntern(type));
    }

    public void addCycle(TrainsCycle cycle) {
        cycle.addListener(listener);
        this.getCyclesIntern(cycle.getType()).add(cycle);
    }

    public void removeCycle(TrainsCycle cycle) {
        cycle.clear();
        this.getCyclesIntern(cycle.getType()).remove(cycle);
        cycle.removeListener(listener);
    }
    
    public TrainsCycle getCycleById(String id) {
        for (Map.Entry<TrainsCycleType, List<TrainsCycle>> entry : cycles.entrySet()) {
            for (TrainsCycle cycle : entry.getValue()) {
                if (cycle.getId().equals(id))
                    return cycle;
            }
        }
        return null;
    }
    
    public TrainsCycle getCycleByIdAndType(String id, TrainsCycleType type) {
        for (TrainsCycle cycle : getCyclesIntern(type)) {
            if (cycle.getId().equals(id))
                return cycle;
        }
        return null;
    }

    private List<TrainsCycle> getCyclesIntern(TrainsCycleType type) {
        if (!cycles.containsKey(type)) {
            cycles.put(type, new ArrayList<TrainsCycle>());
        }
        return cycles.get(type);
    }

    public List<TimetableImage> getImages() {
        return Collections.unmodifiableList(images);
    }

    public void addImage(TimetableImage image) {
        this.images.add(image);
    }

    public void addImage(TimetableImage image, int position) {
        this.images.add(position, image);
    }

    public void removeImage(TimetableImage image) {
        this.images.remove(image);
    }

    public List<TrainType> getTrainTypes() {
        return Collections.unmodifiableList(trainTypes);
    }

    public void removeTrainType(TrainType type) {
        trainTypes.remove(type);
    }

    public void addTrainType(TrainType type) {
        type.setTrainsData(trainsData);
        trainTypes.add(type);
    }

    public void addTrainType(TrainType type, int position) {
        type.setTrainsData(trainsData);
        trainTypes.add(position, type);
    }

    public void setTrainType(TrainType type, int position) {
        type.setTrainsData(trainsData);
        trainTypes.set(position, type);
    }

    public TrainType getTrainTypeById(String id) {
        for (TrainType type : trainTypes) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Trains: " + trains.size() + ", Nodes: " + net.getNodes().size() + ", Lines: " + net.getLines().size();
    }

    @Override
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public Object removeAttribute(String key) {
        return attributes.remove(key);
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public List<EngineClass> getEngineClasses() {
        return Collections.unmodifiableList(engineClasses);
    }

    public void addEngineClass(EngineClass engineClass) {
        engineClasses.add(engineClass);
    }

    public void addEngineClass(EngineClass engineClass, int position) {
        engineClasses.add(position, engineClass);
    }

    public void removeEngineClass(EngineClass engineClass) {
        engineClasses.remove(engineClass);
    }
    
    public EngineClass getEngineClassById(String id) {
        for (EngineClass ec : engineClasses) {
            if (ec.getId().equals(id))
                return ec;
        }
        return null;
    }

    public TrainsData getTrainsData() {
        return trainsData;
    }

    public void setTrainsData(TrainsData trainsData) {
        this.trainsData = trainsData;
    }

    @Override
    public String getId() {
        return id;
    }
}
