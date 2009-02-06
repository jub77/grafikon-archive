package net.parostroj.timetable.model.ls;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.*;

/**
 * Factory for loading/saving train diagram.
 * 
 * @author jub
 */
public class LSFileFactory {

    private static final String METADATA = "metadata.properties";
    private static final String METADATA_KEY_MODEL_VERSION = "model.version";
    private static final LSFileFactory instance = new LSFileFactory();
    private static final Map<Integer, Class<? extends FileLoadSave>> cache = new ConcurrentHashMap<Integer, Class<? extends FileLoadSave>>();
    private static boolean initialized = false;

    public static synchronized LSFileFactory getInstance() {
        if (!initialized) {
            ServiceLoader<FileLoadSave> loader = ServiceLoader.load(FileLoadSave.class);
            for (FileLoadSave fls : loader) {
                List<ModelVersion> versions = fls.getVersions();
                System.out.println("REGISTERED: " + fls.getClass().getName());
                for (ModelVersion version : versions) {
                    cache.put(version.getMajorVersion(), fls.getClass());
                }
            }
            initialized = true;
        }
        return instance;
    }

    public synchronized FileLoadSave createLatest() throws LSException {
        try {
            Class<? extends FileLoadSave> clazz = this.getLatestClass();
            if (clazz == null) {
                throw new LSException("No FileLoadSave registered.");
            } else {
                return clazz.newInstance();
            }
        } catch (Exception e) {
            throw new LSException(e);
        }
    }

    private Class<? extends FileLoadSave> getLatestClass() {
        Map.Entry<Integer, Class<? extends FileLoadSave>> selected = null;
        for (Map.Entry<Integer, Class<? extends FileLoadSave>> entry : cache.entrySet()) {
            if (entry == null || entry.getKey().compareTo(selected.getKey()) > 0) {
                selected = entry;
            }
        }
        return selected.getValue();
    }

    public synchronized FileLoadSave create(ZipInputStream is) throws LSException {
        try {
            ZipEntry entry = is.getNextEntry();
            if (entry == null || !entry.getName().equals(METADATA)) {
                throw new LSException("Metadata was not the first entry.");
            }
            Properties metadata = new Properties();
            if (entry != null) {
                // load metadata
                metadata.load(is);
            }
            return this.createFLSInstance(metadata);
        } catch (IOException ex) {
            throw new LSException(ex);
        }
    }

    public synchronized FileLoadSave create(File file) throws LSException {
        try {
            ZipFile zipFile = new ZipFile(file);
            ZipEntry entry = zipFile.getEntry(METADATA);
            Properties metadata = new Properties();
            if (entry != null) {
                // load metadata
                metadata.load(zipFile.getInputStream(entry));
            }

            return this.createFLSInstance(metadata);
        } catch (ZipException ex) {
            throw new LSException(ex);
        } catch (IOException ex) {
            throw new LSException(ex);
        }
    }

    private FileLoadSave createFLSInstance(Properties metadata) throws LSException {
        try {
            // set model version
            ModelVersion modelVersion = null;
            if (metadata.getProperty(METADATA_KEY_MODEL_VERSION) == null) {
                modelVersion = new ModelVersion("1.0");
            } else {
                modelVersion = new ModelVersion(metadata.getProperty(METADATA_KEY_MODEL_VERSION));
            }

            Class<?> clazz = cache.get(modelVersion.getMajorVersion());
            if (clazz == null) {
                throw new LSException("No FileLoadSave registered for version: " + modelVersion.getVersion());
            }
            return (FileLoadSave) clazz.newInstance();
        } catch (InstantiationException ex) {
            throw new LSException(ex);
        } catch (IllegalAccessException ex) {
            throw new LSException(ex);
        }
    }
}
