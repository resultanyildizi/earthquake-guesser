import weka.core.Instances;
import weka.core.converters.ArffSaver;

import java.io.File;

public class TrainTestSplitter {
    Instances trainData;
    Instances testData;

    public TrainTestSplitter(Instances data) {

        Instances sorted = data;

        // sorted.sort(sorted.attribute("EventDate"));

        trainData = sorted.trainCV(7, 0);
        testData = sorted.testCV(7, 0);
    }

    public void SaveTrainAndTestData() {
        try {
            ArffSaver s = new ArffSaver();
            s.setInstances(trainData);
            s.setFile(new File("data/train_earthquake_data.arff"));
            s.writeBatch();

            s = new ArffSaver();
            s.setInstances(testData);
            s.setFile(new File("data/test_earthquake_data.arff"));
            s.writeBatch();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
