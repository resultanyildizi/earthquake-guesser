class EarthquakeGuesser {

    public static void main(String[] args) throws Exception {
        Preprocessor pp = new Preprocessor("data/earthquake.arff");
        pp.RemoveUnrelatedFeatures();

//        pp.SetRichterScaleClasses();
//        System.out.println(pp.data.instance(30).stringValue(pp.data.numAttributes() - 1));
        pp.ClusterInstancesForFaultLines();
        System.out.println(pp.data.instance(30).stringValue(pp.data.numAttributes() - 1));

        pp.SaveProcessedData("data/preprocessed_earthquake.arff");

        EarthqInstance[] earthqInstances = new EarthqInstance[pp.data.numInstances()];

        for (int i = 0; i < pp.data.numInstances(); i++) {
            var lon = pp.data.instance(i).value(pp.data.attribute("EpicenterLon").index());
            var lat = pp.data.instance(i).value(pp.data.attribute("EpicenterLat").index());
            var faultLine = pp.data.instance(i).stringValue(pp.data.attribute("FaultLine").index());
            earthqInstances[i] = new EarthqInstance(lon,lat, Integer.valueOf(faultLine));
        }

        EarthquakeMap map = new EarthquakeMap(earthqInstances);
        map.DrawMap();

        TrainTestSplitter tts = new TrainTestSplitter(pp.data);
        tts.SaveTrainAndTestData();

        ClassifierModel classifierModel = new ClassifierModel();
        classifierModel.TrainAndTestData(tts.trainData, tts.testData, true);
        classifierModel.TrainAndTestData(tts.trainData, tts.testData, false);

    }

}
