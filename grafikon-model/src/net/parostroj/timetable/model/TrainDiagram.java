package net.parostroj.timetable.model;

import java.util.*;
import net.parostroj.timetable.model.events.GTEvent;
import net.parostroj.timetable.model.events.TrainDiagramEvent;
import net.parostroj.timetable.model.events.TrainDiagramListener;
import net.parostroj.timetable.model.events.TrainDiagramListenerWithNested;

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
    /** Penalty table. */
    private PenaltyTable penaltyTable;

    private GTListenerTrainDiagramImpl listener;
    private GTListenerSupport<TrainDiagramListener, TrainDiagramEvent> listenerSupport;
    private GTListenerSupport<TrainDiagramListenerWithNested, TrainDiagramEvent> listenerSupportAll;

    /**
     * Default constructor.
     */
    public TrainDiagram(String id, TrainsData data) {
        this.id = id;
        this.routes = new ArrayList<Route>();
        this.trains = new ArrayList<Train>();
        this.cycles = new EnumMap<TrainsCycleType, List<TrainsCycle>>(TrainsCycleType.class);
        this.images = new LinkedList<TimetableImage>();
        this.engineClasses = new LinkedList<EngineClass>();
        this.penaltyTable = new PenaltyTable();
        this.net = new Net();
        this.trainTypes = new LinkedList<TrainType>();
        this.attributes = new Attributes();
        this.trainsData = data;
        this.listener = new GTListenerTrainDiagramImpl(this);
        this.listenerSupport = new GTListenerSupport<TrainDiagramListener, TrainDiagramEvent>(new GTEventSender<TrainDiagramListener, TrainDiagramEvent>() {

            @Override
            public void fireEvent(TrainDiagramListener listener, TrainDiagramEvent event) {
                listener.trainDiagramChanged(event);
            }
        });
        this.listenerSupportAll = new GTListenerSupport<TrainDiagramListenerWithNested, TrainDiagramEvent>(new GTEventSender<TrainDiagramListenerWithNested, TrainDiagramEvent>() {

            @Override
            public void fireEvent(TrainDiagramListenerWithNested listener, TrainDiagramEvent event) {
                if (event.getNestedEvent() != null)
                    listener.trainDiagramChangedNested(event);
                else
                    listener.trainDiagramChanged(event);
            }
        });
        this.net.addListener(listener);
    }

    /**
     * @return net
     */
    public Net getNet() {
        return net;
    }

    /**
     * @return predefined routes
     */
    public List<Route> getRoutes() {
        return Collections.unmodifiableList(this.routes);
    }

    public void addRoute(Route route) {
        this.routes.add(route);
        this.fireEvent(new TrainDiagramEvent(this, TrainDiagramEvent.Type.ROUTE_ADDED, route));
    }

    public void removeRoute(Route route) {
        this.routes.remove(route);
        this.fireEvent(new TrainDiagramEvent(this, TrainDiagramEvent.Type.ROUTE_REMOVED, route));
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
        this.fireEvent(new TrainDiagramEvent(this, TrainDiagramEvent.Type.TRAIN_ADDED, train));
    }

    public void removeTrain(Train train) {
        train.detach();
        this.trains.remove(train);
        train.removeListener(listener);
        this.fireEvent(new TrainDiagramEvent(this, TrainDiagramEvent.Type.TRAIN_REMOVED, train));
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
        type.removeListener(listener);
        this.fireEvent(new TrainDiagramEvent(this, TrainDiagramEvent.Type.TRAIN_TYPE_REMOVED, type));
    }

    public void addTrainType(TrainType type) {
        this.addTrainType(type, trainTypes.size());
    }

    public void addTrainType(TrainType type, int position) {
        type.addListener(listener);
        type.setTrainsData(trainsData);
        trainTypes.add(position, type);
        this.fireEvent(new TrainDiagramEvent(this, TrainDiagramEvent.Type.TRAIN_TYPE_ADDED, type));
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

    public PenaltyTable getPenaltyTable() {
        return penaltyTable;
    }

    public void setPenaltyTable(PenaltyTable penaltyTable) {
        this.penaltyTable = penaltyTable;
    }

    @Override
    public String getId() {
        return id;
    }
    
    public void addListener(TrainDiagramListener listener) {
        listenerSupport.addListener(listener);
    }
    
    public void removeListener(TrainDiagramListener listener) {
        listenerSupport.removeListener(listener);
    }
    
    public void addListenerWithNested(TrainDiagramListenerWithNested listener) {
        listenerSupportAll.addListener(listener);
    }
    
    public void removeListenerWithNested(TrainDiagramListenerWithNested listener) {
        listenerSupportAll.removeListener(listener);
    }
    
    protected void fireNestedEvent(GTEvent<?> nestedEvent) {
        TrainDiagramEvent event = new TrainDiagramEvent(this, nestedEvent);
        this.fireEvent(event);
    }
    
    protected void fireEvent(TrainDiagramEvent e) {
        listenerSupportAll.fireEvent(e);
        if (e.getNestedEvent() == null) {
            listenerSupport.fireEvent(e);
        }
    }
}
