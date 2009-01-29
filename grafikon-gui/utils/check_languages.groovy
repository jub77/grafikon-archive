class Test {

    static void compareProperties(name, translation) {
        def orig = name + ".properties"
        def trans = name + "_" + translation + ".properties"
        def outp = name + "_" + translation + ".properties.diff"
        compare(orig, trans, outp)
        "native2ascii ${outp} ${outp}.trans".execute()
    }

    static void compare(originalFN, translatedFN, outputFN) {
        Properties original = new Properties();
        Properties translated = new SortedProperties();

        original.load(new FileReader(originalFN))
        translated.load(new FileReader(translatedFN))

        Set<String> keySet = original.keySet()
        keySet.removeAll(translated.keySet())
        for (Object key : keySet) {
            translated.setProperty((String)key, "%%MISSING%% " + original.getProperty((String)key))
        }

        if (!keySet.isEmpty())
        translated.store(new FileWriter(outputFN),null)
    }
}

class SortedProperties extends Properties {

    public Set<Object> keySet() {
        return Collections.unmodifiableSet(new TreeSet<Object>(super.keySet()));
    }

    public String getProperty(String key) {
        return super.getProperty(key);
    }

    public Enumeration keys() {
        Enumeration keysEnum = super.keys();
        Vector keyList = new Vector();
        while(keysEnum.hasMoreElements()){
            keyList.add(keysEnum.nextElement());
        }
        Collections.sort(keyList);
        return keyList.elements();
    }
}

Test.compareProperties("gt_texts","sk")
Test.compareProperties("gt_texts","hu")
Test.compareProperties("gt_texts","pl")
Test.compareProperties("dc_texts","sk")
Test.compareProperties("dc_texts","hu")
Test.compareProperties("dc_texts","pl")
Test.compareProperties("ec_texts","sk")
Test.compareProperties("ec_texts","hu")
Test.compareProperties("ec_texts","pl")
Test.compareProperties("n_timetable_texts","sk")
Test.compareProperties("n_timetable_texts","hu")
Test.compareProperties("n_timetable_texts","pl")
Test.compareProperties("sp_texts","sk")
Test.compareProperties("sp_texts","hu")
Test.compareProperties("sp_texts","pl")
Test.compareProperties("ep_texts","sk")
Test.compareProperties("ep_texts","hu")
Test.compareProperties("ep_texts","pl")
Test.compareProperties("t_timetable_texts","sk")
Test.compareProperties("t_timetable_texts","hu")
Test.compareProperties("t_timetable_texts","pl")
Test.compareProperties("tuc_texts","sk")
Test.compareProperties("tuc_texts","hu")
Test.compareProperties("tuc_texts","pl")
