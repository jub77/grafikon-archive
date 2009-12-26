package net.parostroj.timetable.gui;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * Application preferences.
 *
 * @author jub
 */
public class AppPreferences {

    private static final String PREFERENCES_NAME = ".grafikon";
    private static AppPreferences instance = null;
    private Properties preferencesProps = new Properties();

    /**
     * returns preferences for the application.
     * 
     * @return preferences
     * @throws java.io.IOException
     */
    public synchronized static AppPreferences getPreferences() throws IOException {
        if (instance == null) {
            instance = new AppPreferences();
            instance.load();
        }
        return instance;
    }

    public String getString(String key) {
        return preferencesProps.getProperty(key);
    }

    public void setString(String key, String value) {
        preferencesProps.setProperty(key, value);
    }

    public void setInt(String key, Integer value) {
        preferencesProps.setProperty(key, value.toString());
    }

    public Integer getInt(String key) {
        String value = this.getString(key);
        if (value == null) {
            return null;
        } else {
            return Integer.valueOf(value);
        }
    }

    public void setBoolean(String key, Boolean value) {
        preferencesProps.setProperty(key, value.toString());
    }

    public Boolean getBoolean(String key) {
        String value = this.getString(key);
        if (value == null) {
            return null;
        } else {
            return Boolean.valueOf(value);
        }
    }

    public void remove(String key) {
        preferencesProps.remove(key);
    }

    public void removeWithPrefix(String keyPrefix) {
        Set<String> removedKeys = null;
        for (Object obj : preferencesProps.keySet()) {
            String key = (String)obj;
            if (key.startsWith(keyPrefix)) {
                if (removedKeys == null)
                    removedKeys = new HashSet<String>();
                removedKeys.add(key);
            }
        }
        if (removedKeys != null)
            for (String key : removedKeys)
                preferencesProps.remove(key);
    }

    /**
     * loads preferences.
     * 
     * @throws java.io.IOException
     */
    public void load() throws IOException {
        String homeDir = this.getSaveDirectory();
        if (homeDir != null) {
            File propsFile = new File(homeDir, PREFERENCES_NAME);
            if (propsFile.exists()) {
                preferencesProps.load(new FileReader(propsFile));
            }
        }
    }

    /**
     * saves preferences.
     * 
     * @throws java.io.IOException
     */
    public void save() throws IOException {
        String homeDir = this.getSaveDirectory();
        if (homeDir != null) {
            Properties savedProperties = new Properties() {
                @Override
                public Set<Object> keySet() {
                    return new TreeSet<Object>(super.keySet());
                }

                @Override
                public synchronized Enumeration<Object> keys() {
                    final Iterator<Object> iterator = keySet().iterator();
                    return new Enumeration<Object>() {

                        @Override
                        public boolean hasMoreElements() {
                            return iterator.hasNext();
                        }

                        @Override
                        public Object nextElement() {
                            return iterator.next();
                        }
                    };
                }


            };
            for (String key : preferencesProps.stringPropertyNames()) {
                savedProperties.put(key, preferencesProps.getProperty(key));
            }
            File propsFile = new File(homeDir, PREFERENCES_NAME);
            savedProperties.store(new FileWriter(propsFile), null);
        }
    }

    private String getSaveDirectory() {
        return System.getProperty("user.home");
    }
}
