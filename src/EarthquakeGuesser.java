class EarthquakeGuesser {

    public static void main(String[] args) throws Exception {
        Preprocessor pp = new Preprocessor("data/earthquake.arff");
        pp.RemoveUnrelatedFeatures();

//        pp.SetRichterScaleClasses();
//        System.out.println(pp.data.instance(30).stringValue(pp.data.numAttributes() - 1));
        pp.ClusterInstancesForFaultLines();
        System.out.println(pp.data.instance(30).stringValue(pp.data.numAttributes() - 1));

        pp.SaveProcessedData("data/preprocessed_earthquake.arff");

        TrainTestSplitter tts = new TrainTestSplitter(pp.data);
        tts.SaveTrainAndTestData();

        SimpleClassifierModel simpleClassifierModel = new SimpleClassifierModel();
        simpleClassifierModel.TrainAndTestData(tts.trainData, tts.testData);

        ClassifierModel classifierModel = new ClassifierModel();
        classifierModel.TrainAndTestData(tts.trainData, tts.testData);


    }

}
