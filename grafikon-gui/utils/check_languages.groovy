compareProperties("gt_texts","sk")
compareProperties("gt_texts","hu")
compareProperties("gt_texts","pl")
compareProperties("dc_texts","sk")
compareProperties("dc_texts","hu")
compareProperties("dc_texts","pl")
compareProperties("ec_texts","sk")
compareProperties("ec_texts","hu")
compareProperties("ec_texts","pl")
compareProperties("n_timetable_texts","sk")
compareProperties("n_timetable_texts","hu")
compareProperties("n_timetable_texts","pl")
compareProperties("sp_texts","sk")
compareProperties("sp_texts","hu")
compareProperties("sp_texts","pl")
compareProperties("ep_texts","sk")
compareProperties("ep_texts","hu")
compareProperties("ep_texts","pl")
compareProperties("t_timetable_texts","sk")
compareProperties("t_timetable_texts","hu")
compareProperties("t_timetable_texts","pl")
compareProperties("tuc_texts","sk")
compareProperties("tuc_texts","hu")
compareProperties("tuc_texts","pl")

void compareProperties(name, translation) {
          def orig = name + ".properties"
    def trans = name + "_" + translation + ".properties"
    def outp = name + "_" + translation + ".properties.diff"
    compare(orig, trans, outp)
    "native2ascii ${outp} ${outp}.trans".execute()
}

void compare(originalFN, translatedFN, outputFN) {
    Properties original = new Properties();
    Properties translated = new Properties();
    original.load(new FileReader(originalFN))
    translated.load(new FileReader(translatedFN))

    Properties result = new Properties()
    Set<String> keySet = original.keySet()
    keySet.removeAll(translated.keySet())
    for (Object key : keySet) {
        result.setProperty((String)key, original.getProperty((String)key))
    }

    if (!result.keySet().isEmpty())
        result.store(new FileWriter(outputFN),null)
}
