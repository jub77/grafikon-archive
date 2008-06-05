package net.parostroj.timetable.gui.views;

import javax.swing.table.TableCellEditor;
import net.parostroj.timetable.model.TimeInterval;

/**
 * Enumeration with columns for train view table.
 * 
 * @author jub
 */
public enum TrainTableColumn {
    NODE(0, "train.table.node", 120, 120, 120, "w", String.class, false, null),
    START(1, "train.table.starttime", 50, 50, 50, "w", String.class, true, null),
    END(2, "train.table.endtime", 50, 50, 50, "lo", String.class, true, null),
    STOP(3, "train.table.stop", 50, 50, 50, "flo", Integer.class, false, null),
    SPEED(4, "train.table.speed", 50, 50, 50, "e", Integer.class, false, null),
    PLATFORM(5, "train.table.platform", 50, 50, 50, "t", String.class, false, new TrackCellEditor()),
    CONFLICTS(6, "train.table.conflicts", 100, 100, 100, "w", String.class, false, null),
    COMMENT_SHOWN(7, "train.table.comment.shown", 30, 30, 30, "o", Boolean.class, false, null),
    COMMENT(8, "train.table.comment", 1, Integer.MAX_VALUE, 60, "", String.class, false, null),
    OCCUPIED_ENTRY(9, "train.table.occupied.track", 30, 30, 30, "fo", Boolean.class, false, null),
    SHUNT(10, "train.table.shunt", 30, 30, 30, "fo", Boolean.class, false, null);
    
    private int index;
    
    private String key;
    
    private int minWidth;
    
    private int prefWidth;
    
    private int maxWidth;
    
    private Class<?> clazz;
    
    private boolean rightAling;
    
    private boolean even;
    
    private boolean odd;
    
    private boolean first;
    
    private boolean last;
    
    private boolean all;
    
    private boolean oneTrack;
    
    private TableCellEditor editor;
    
    private TrainTableColumn(int index, String key, int minWidth, int maxWidth, int prefWidth, String forbidden, Class<?> clazz, boolean rightAling, TableCellEditor editor) {
        this.index = index;
        this.key = key;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.prefWidth = prefWidth;
        this.clazz = clazz;
        this.rightAling = rightAling;
        this.editor = editor;
        // parse limits
        for (char limit : forbidden.toCharArray()) {
            switch(limit) {
                case 'e':
                    even = true;
                    break;
                case 'o':
                    odd = true;
                    break;
                case 'f':
                    first = true;
                    break;
                case 'l':
                    last = true;
                    break;
                case 'w':
                    all = true;
                    break;
                case 't':
                    oneTrack = true;
                    break;
            }
        }
    }

    public int getIndex() {
        return index;
    }

    public String getKey() {
        return key;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public int getPrefWidth() {
        return prefWidth;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public boolean isRightAling() {
        return rightAling;
    }

    public TableCellEditor getEditor() {
        return editor;
    }
    
    public boolean isAllowedToEdit(int row, int max, TimeInterval interval) {
        if (all)
            return false;
        if (even && row % 2 == 0)
            return false;
        if (odd && row % 2 == 1)
            return false;
        if (first && row == 0)
            return false;
        if (last && row == max)
            return  false;
        if (oneTrack) {
            if (interval.getOwner().getTracks().size() == 1)
                return false;
        }
        return true;
    }
    
    public static TrainTableColumn getColumn(int index) {
        for (TrainTableColumn column : values()) {
            if (column.getIndex() == index)
                return column;
        }
        return null;
    }
}
