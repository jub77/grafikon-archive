package net.parostroj.timetable.model.templates;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.model.ls.FileLoadSave;
import net.parostroj.timetable.model.ls.LSException;
import net.parostroj.timetable.model.ls.LSFileFactory;

/**
 * Class for loading model templates. It also returns list of available
 * templates.
 * 
 * @author jub
 */
public class TemplatesLoader {

    private static final Logger LOG = Logger.getLogger(TemplatesLoader.class.getName());
    private static final String TEMPLATE_LIST_FILE = "/templates/list.xml";
    private static final String TEMPLATES_LOCATION = "/templates/";
    private static TemplateList templateList;

    public static synchronized List<Template> getTemplates() {
        if (templateList == null) {
            // load template list
            try {
                JAXBContext context = JAXBContext.newInstance(TemplateList.class);
                Unmarshaller u = context.createUnmarshaller();
                templateList = (TemplateList) u.unmarshal(TemplatesLoader.class.getResourceAsStream(TEMPLATE_LIST_FILE));
                LOG.fine("Loaded list of templates.");
            } catch (JAXBException e) {
                LOG.log(Level.SEVERE, "Cannot load list of templates.", e);
                // empty template list
                templateList = new TemplateList();
            }
        }
        return templateList.getTemplates();
    }

    public TrainDiagram getTemplate(String name) throws LSException {
        for (Template template : getTemplates()) {
            if (template.getName().equals(name)) {
                // create file with template location
                ZipInputStream is = new ZipInputStream(TemplatesLoader.class.getResourceAsStream(TEMPLATES_LOCATION + template.getFilename()));
                FileLoadSave ls = LSFileFactory.getInstance().createForLoad(is);
                return ls.load(is);
            }
        }
        // no template found
        return null;
    }
}
