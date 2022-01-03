import weka.core.Instances;
import weka.core.converters.ArffSaver;

import java.io.File;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TrainTestSplitter {
    Instances trainData;
    Instances testData;

    public TrainTestSplitter(Instances data) {
        var att = data.attribute("EventDate");

        trainData = new Instances(data);
        testData = new Instances(data);
        try {
            for (int i = trainData.numInstances() - 1; i >= 0; i--) {
                int year = GetYear(trainData.instance(i).stringValue(att));
                if (year > 2018) trainData.delete(i);
            }
            trainData.sort(att);


            for (int i = testData.numInstances() - 1; i >= 0;  i--) {
                int year = GetYear(testData.instance(i).stringValue(att));
                if (year <= 2018) testData.delete(i);
            }

            testData.sort(att);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int GetYear(String dateStr) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        Date date = simpleDateFormat.parse(dateStr);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
}
