import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.ConfusionMatrix;
import weka.classifiers.evaluation.EvaluationMetricHelper;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.*;
import weka.classifiers.functions.supportVector.RegOptimizer;
import weka.classifiers.functions.supportVector.RegSMO;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.Remove;

import javax.sound.sampled.Line;
import java.security.Guard;

public class ClassifierModel {
    public void TrainAndTestData(Instances trainData, Instances testData, boolean useFaultLine) {
        try {
            Attribute magnitude = trainData.attribute("Magnitude");
            Attribute date = trainData.attribute("EventDate");
            Attribute faultline = trainData.attribute("FaultLine");

            trainData.setClass(magnitude);
            testData.setClass(magnitude);


            Remove removeFilter = new Remove();

            if(!useFaultLine) {
                removeFilter.setAttributeIndicesArray(new int[] {date.index(), faultline.index()});
            }else    {
                removeFilter.setAttributeIndicesArray(new int[] {date.index()});
            }
            removeFilter.setInputFormat(trainData);

            Instances fTrainData = Filter.useFilter(trainData, removeFilter);
            Instances fTestData = Filter.useFilter(testData, removeFilter);

            LinearRegression model = new LinearRegression();
            model.buildClassifier(fTrainData);

            Instances predictedData = new Instances(fTestData);
            for (int i = 0; i < testData.numInstances(); i++) {
                double mag = model.classifyInstance(fTestData.instance(i));
                predictedData.instance(i).setValue(predictedData.attribute("Magnitude"), mag);
            }


            var fNewTestData = Utils.CalculateRichterClass(fTestData);
            var fNewPredData = Utils.CalculateRichterClass(predictedData);

            fNewTestData.setClass(fNewTestData.attribute("RichterScale"));

            CustomEvaluation eval = new CustomEvaluation(fNewTestData, fNewPredData);
            eval.PrintConfusionMatrix();





        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

 // System.out.println(fTestData.instance(i).stringValue(date) + ": " +  (fTestData.instance(i).value(magnitude) -  predictedData.instance(i).value(magnitude)));
