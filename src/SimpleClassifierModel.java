import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;

public class SimpleClassifierModel {
public  void  TrainAndTestData(Instances trainData, Instances testData) {
    try {
        var newTrainData = Utils.CalculateRichterClass(trainData);
        var newTestData = Utils.CalculateRichterClass(testData);

        newTrainData.setClass(newTrainData.attribute("RichterScale"));
        newTestData.setClass(newTestData.attribute("RichterScale"));



        RandomForest RF = new RandomForest();
        RF.buildClassifier(newTrainData);

        Evaluation eval = new Evaluation(newTrainData);

        eval.evaluateModel(RF, newTestData);

        System.out.println(eval.toSummaryString("Evaluation results:\n", false));

        for (int i = 0; i < newTestData.numInstances(); i++) {

            System.out.print("Actual  = " + newTestData.instance(i).stringValue(0) + ", =" + newTestData.instance(i).classValue());
            System.out.println(", Predicted  = " + RF.classifyInstance(newTestData.instance(i)));
        }




        System.out.println("Correct % = " + eval.pctCorrect());
        System.out.println("Incorrect % = " + eval.pctIncorrect());
        System.out.println("AUC = " + eval.areaUnderROC(1));
        System.out.println("kappa = " + eval.kappa());
        System.out.println("MAE = " + eval.meanAbsoluteError());
        System.out.println("RMSE = " + eval.rootMeanSquaredError());
        System.out.println("RAE = " + eval.relativeAbsoluteError());
        System.out.println("RRSE = " + eval.rootRelativeSquaredError());
        System.out.println("Error Rate = " + eval.errorRate());

        System.out.println("Precision = " + eval.precision(1));
        System.out.println("Recall = " + eval.recall(1));
        System.out.println("fMeasure = " + eval.fMeasure(1));
        //the confusion matrix
        System.out.println(eval.toMatrixString("=== Overall Confusion Matrix ===\n"));
    }catch (Exception e) {
        e.printStackTrace();
    }
}
}
