package net.parostroj.timetable.model.save.version01;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.parostroj.timetable.model.*;
import net.parostroj.timetable.model.save.LSTrainTypeList;

public class LSVisitorBuilder implements LSVisitor {

    private TrainDiagram diagram;
    private Map<Integer, Object> ids = new HashMap<Integer, Object>();
    private LSTrainTypeList trainTypeList;
    // last station
    private Node lastStation;
    // last train
    private Train lastTrain;
    private List<Train> trains;

    public LSVisitorBuilder(LSTrainTypeList list) {
        this.trainTypeList = list;
        this.trains = new LinkedList<Train>();
    }

    @Override
    public void visit(LSTrainDiagram lsDiagram) {
        // create empty net
        diagram = new TrainDiagram(UUID.randomUUID().toString(), trainTypeList.getTrainsData());
        for (TrainType type : trainTypeList.getTrainTypeList()) {
            diagram.addTrainType(type);
        }
        Net net = new Net();
        diagram.setNet(net);
    }

    @Override
    public void visit(LSNode lsNode) {
        NodeType type = NodeType.valueOf(lsNode.getNodeType());
        Node node = new Node(this.createId(), type, lsNode.getName(), lsNode.getAbbr());
        node.setAttribute("interlocking.plant", lsNode.getInterlockingPlant());
        node.setPositionX(lsNode.getX());
        node.setPositionY(lsNode.getY());
        ids.put(lsNode.getId(), node);

        // add to net
        diagram.getNet().addNode(node);
        // set last station
        lastStation = node;
    }

    @Override
    public void visit(LSNodeTrack lsNodeTrack) {
        NodeTrack stationTrack = new NodeTrack(this.createId(), lsNodeTrack.getNumber());
        ids.put(lsNodeTrack.getId(), stationTrack);

        stationTrack.setPlatform(lsNodeTrack.isPlatform());

        // add to last station
        lastStation.addTrack(stationTrack);
    }

    /**
     * @return created train diagram
     */
    public TrainDiagram getTrainDiagram() {
        for (Train t : trains) {
            diagram.addTrain(t);
        }
        return diagram;
    }

    @Override
    public void visit(LSLine lsLine) {
        Node from = (Node) ids.get(lsLine.getSourceId());
        Node to = (Node) ids.get(lsLine.getTargetId());
        Line line = new Line(this.createId(), lsLine.getLength(), from, to, Line.UNLIMITED_SPEED);
        LineTrack lt = new LineTrack(this.createId(), "1");
        line.addTrack(lt);
        lt.setFromStraightTrack((NodeTrack) ids.get(lsLine.getSourceTrackId()));
        lt.setToStraightTrack((NodeTrack) ids.get(lsLine.getTargetTrackId()));
        ids.put(lsLine.getId(), line);

        line.setTopSpeed(lsLine.getTopSpeed());

        // add to net
        Net net = diagram.getNet();
        net.addLine(from, to, line);
    }

    @Override
    public void visit(LSTrain lsTrain) {
        TrainType type = trainTypeList.getTrainType(lsTrain.getTrainType());
        Train train = new Train(this.createId(), lsTrain.getName(), type);
        ids.put(lsTrain.getId(), train);

        train.setTopSpeed(lsTrain.getTopSpeed());
        train.setType(type);
        train.setDescription(lsTrain.getDescription());
        train.setAttribute("electric", lsTrain.isElectric());
        train.setAttribute("diesel", lsTrain.isDiesel());
        train.setAttribute("weight.info", lsTrain.getWeightInfo());
        train.setAttribute("route.info", lsTrain.getRouteInfo());

        this.trains.add(train);

        // set last train
        lastTrain = train;
    }

    @Override
    public void visit(LSTimeInterval lsInterval) {
        // add station track
        Track track = (Track) ids.get(lsInterval.getStationTrackId());

        // add to the last train
        RouteSegment part = (RouteSegment) ids.get(lsInterval.getRoutePartId());
        TimeIntervalType type = TimeIntervalType.valueOf(lsInterval.getType());
        TimeInterval interval = new TimeInterval(lastTrain, part, lsInterval.getStart(), lsInterval.getEnd(), lsInterval.getSpeed(), TimeIntervalDirection.toTimeIntervalDirection(lsInterval.getDirection()), type, track);
        interval.setComment(lsInterval.getComment());

        // add interval to train
        lastTrain.addInterval(interval);

        // add backward compactibility - owner is a line - add first track from line
        if (part instanceof Line) {
            interval.setTrack(((Line) part).getTracks().get(0));
        }
    }

    @Override
    public void visit(LSModelInfo lsInfo) {
        if (lsInfo != null) {
            diagram.setAttribute("scale", Scale.valueOf(lsInfo.getScale()));
            diagram.setAttribute("time.scale", Double.valueOf(lsInfo.getTimeScale()));
        }
    }

    @Override
    public void visit(LSRoute lsRoute) {
        Route route = new Route(this.createId());
        for (int id : lsRoute.getIds()) {
            RouteSegment segment = (RouteSegment) ids.get(id);
            route.getSegments().add(segment);
        }
        // add route to diagram
        diagram.addRoute(route);
    }

    @Override
    public void visit(LSTrainsCycle lsCycle) {
        TrainsCycleType type = TrainsCycleType.valueOf(lsCycle.getType());
        TrainsCycle cycle = new TrainsCycle(this.createId(), lsCycle.getName(), lsCycle.getDescription(), type);
        cycle.setAttribute("comment", lsCycle.getComment());
        if (lsCycle.getItems() != null) {
            for (LSTrainsCycleItem item : lsCycle.getItems()) {
                Train train = (Train) ids.get(item.getTrainId());
                TrainsCycleItem tcItem = new TrainsCycleItem(cycle, train, item.getComment(), null, null);
                cycle.addItem(tcItem);
            }
        }

        diagram.addCycle(cycle);
    }

    @Override
    public void visit(LSImage lsImage) {
        TimetableImage image = new TimetableImage(lsImage.getFilename());
        image.setHeight(lsImage.getHeight());
        diagram.addImage(image);
    }

    private String createId() {
        return UUID.randomUUID().toString();
    }
}
