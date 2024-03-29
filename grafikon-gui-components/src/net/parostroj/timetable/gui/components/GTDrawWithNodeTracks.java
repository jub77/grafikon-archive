package net.parostroj.timetable.gui.components;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.*;
import java.util.List;
import net.parostroj.timetable.model.*;
import net.parostroj.timetable.model.Interval;

/**
 * Graphical timetable with node tracks.
 *
 * @author jub
 */
public class GTDrawWithNodeTracks extends GTDraw {
    
    // basic display
    private static final Stroke TRAIN_STROKE = new BasicStroke(1.5f);
    private static final Stroke TRAIN_SS_STROKE = new BasicStroke(5.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
    private static final Stroke STATION_STROKE = new BasicStroke(1.1f);
    private static final Stroke TECH_TIME_STROKE = new BasicStroke(1.1f);
    
    // extended display
    private static final Stroke STATION_STROKE_STOP_EXT = new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,1.0f,new float[]{3f,3f},0f);
    private static final Stroke STATION_STROKE_STOP_WITH_FREIGHT_EXT = new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,1.0f,new float[]{16f,5f},0f);
    private static final Stroke STATION_STROKE_ROUTE_SPLIT_EXT = new BasicStroke(0.8f);
    
    private static final int TRACK_GAP = 5;

    private Map<Track,Integer> trackPositions;
    
    public GTDrawWithNodeTracks(int borderX, int borderY, int gapStationX, Dimension size, Route route, GraphicalTimetableView.TrainColors colors,TrainColorChooser chooser, HighlightedTrains hTrains, TrainRegionCollector collector) {
        super(borderX, borderY, gapStationX, size,route,colors,chooser,hTrains,collector);
    }
    
    @Override
    protected void computePositions() {
        positions = new HashMap<Node, Integer>();
        
        trackPositions = new HashMap<Track, Integer>();
        
        stations = new LinkedList<Node>();
        
        int completeLength = 0;
        int trackGaps = 0;
        for (RouteSegment segment : route.getSegments()) {
            if (segment.asLine() != null)
                completeLength = completeLength + segment.asLine().getLength();
            if (segment.asNode() != null)
                trackGaps = trackGaps + segment.asNode().getTracks().size() - 1;
        }
        
        double position = 0;
        int height = size.height - trackGaps * TRACK_GAP;
        double step = (double)height / (double)completeLength;
        for (RouteSegment segment : route.getSegments()) {
            if (segment.asLine() != null)
                position = position + segment.asLine().getLength() * step;
            if (segment.asNode() != null) {
                Node node = segment.asNode();
                stations.add(node);
                int tracks = node.getTracks().size();
                positions.put(node, (int)position);
                trackPositions.put(node.getTracks().get(0),(int)position);
                for (int i = 1; i < tracks; i++) {
                    position = position + TRACK_GAP;
                    trackPositions.put(node.getTracks().get(i), (int)position);
                }
            }
        }
    }

    @Override
    protected void paintStations(Graphics2D g) {
        g.setStroke(STATION_STROKE);
        g.setColor(Color.orange);
        for (Node s : stations) {
            // skip over signals ...
            if (s.getType() == NodeType.SIGNAL)
                continue;
            if (preferences.get(GTDrawPreference.EXTENDED_LINES) == Boolean.TRUE) {
                switch (s.getType()) {
                    case STOP:
                        g.setStroke(STATION_STROKE_STOP_EXT);
                        break;
                    case STOP_WITH_FREIGHT:
                        g.setStroke(STATION_STROKE_STOP_WITH_FREIGHT_EXT);
                        break;
                    case ROUTE_SPLIT:
                        g.setStroke(STATION_STROKE_ROUTE_SPLIT_EXT);
                        break;
                    default:
                        g.setStroke(STATION_STROKE);
                        break;
                }
            }
            for (NodeTrack track : s.getTracks()) {
                int y = start.y + trackPositions.get(track);
                g.drawLine(start.x, y, start.x + size.width, y);
            }
        }
    }

    @Override
    protected void paintTrains(Graphics2D g) {
        double timeStep = (double)size.width / (24 * 3600);
        
        for (RouteSegment part : route.getSegments()) {
            if (part.asNode() != null) {
                this.paintTrainsInStation(part.asNode(), g, timeStep);
            } else if (part.asLine() != null) {
                this.paintTrainsOnLine(part.asLine(), g, timeStep, TRAIN_STROKE);
            }
        }
    }

    @Override
    protected Line2D createTrainLine(TimeInterval interval, Interval i, double timeStep) {
        int x1 = (int)(start.x + i.getStart() * timeStep);
        int x2 = (int)(start.x + i.getEnd() * timeStep);
        int y1 = start.y + trackPositions.get(interval.getTrain().getIntervalBefore(interval).getTrack());
        int y2 = start.y + trackPositions.get(interval.getTrain().getIntervalAfter(interval).getTrack());

        Line2D line2D = new Line2D.Float(x1, y1, x2, y2);
        return line2D;
    }
    
    private void paintTrainsInStation(Node station, Graphics2D g, double timeStep) {
        for (NodeTrack nodeTrack : station.getTracks()) {
            for (TimeInterval interval : nodeTrack.getTimeIntervalList()) {
                if (interval.isTechnological() && preferences.get(GTDrawPreference.TECHNOLOGICAL_TIME) != Boolean.TRUE)
                    continue;
                if (!interval.isBoundary()) {
                    g.setStroke(TRAIN_STROKE);
                } else if (interval.isTechnological()) {
                    g.setStroke(TECH_TIME_STROKE);
                } else {
                    g.setStroke(TRAIN_SS_STROKE);
                }
                g.setColor(this.getIntervalColor(interval));

                Interval normalized = interval.getInterval().normalize();
                Line2D line = this.createTrainLineInStation(interval, normalized, timeStep);
                g.draw(line);
                Interval overMidnight = normalized.getNonNormalizedIntervalOverMidnight();
                if (overMidnight != null) {
                    line = this.createTrainLineInStation(interval, overMidnight, timeStep);
                    g.draw(line);
                }
            }
        }
    }

    private Line2D createTrainLineInStation(TimeInterval interval, Interval i, double timeStep) {
        int y = start.y + trackPositions.get(interval.getTrack());
        int x1 = (int)(start.x + i.getStart() * timeStep);
        int x2 = (int)(start.x + i.getEnd() * timeStep);
        Line2D line2D = new Line2D.Float(x1, y, x2, y);
        return line2D;
    }
}
