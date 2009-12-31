package net.parostroj.timetable.model;

import net.parostroj.timetable.visitors.TrainDiagramVisitor;

/**
 * Track in the station.
 *
 * @author jub
 */
public class NodeTrack extends Track {

    /** Platform. */
    private boolean platform;
    
    // node reference
    Node node;

    /**
     * Constructor.
     * 
     * @param id id
     */
    public NodeTrack(String id) {
        super(id);
    }

    /**
     * creates instance with specified track number.
     *
     * @param id id
     * @param number track number
     */
    public NodeTrack(String id, String number) {
        super(id, number);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * @return the platform
     */
    public boolean isPlatform() {
        return platform;
    }

    /**
     * @param platform the platform to set
     */
    public void setPlatform(boolean platform) {
        this.platform = platform;
        this.fireAttributeChanged("platform");
    }

    @Override
    void fireAttributeChanged(String attributeName) {
        if (node != null)
            node.fireTrackAttributeChanged(attributeName, this);
    }

    /**
     * accepts visitor.
     *
     * @param visitor visitor
     */
    void accept(TrainDiagramVisitor visitor) {
        visitor.visit(this);
    }
}
