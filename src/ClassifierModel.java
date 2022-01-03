import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.ConfusionMatrix;
import weka.classifiers.evaluation.EvaluationMetricHelper;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.functions.LinearRegression;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.Remove;

import java.security.Guard;

public class ClassifierModel {
    public void TrainAndTestData(Instances trainData, Instances testData) {
        try {
            Attribute magnitude = trainData.attribute("Magnitude");
            Attribute date = trainData.attribute("EventDate");
            //Attribute richter = trainData.attribute("RichterScale");

            trainData.setClass(magnitude);
            testData.setClass(magnitude);


            Remove removeFilter = new Remove();
            removeFilter.setAttributeIndicesArray(new int[] {date.index()});
            removeFilter.setInputFormat(trainData);

            Instances fTrainData = Filter.useFilter(trainData, removeFilter);
            Instances fTestData = Filter.useFilter(testData, removeFilter);

            GaussianProcesses processes = new GaussianProcesses();
            processes.buildClassifier(fTrainData);

            Instances predictedData = new Instances(fTestData);
            for (int i = 0; i < testData.numInstances(); i++) {
                double mag = processes.classifyInstance(fTestData.instance(i));
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
