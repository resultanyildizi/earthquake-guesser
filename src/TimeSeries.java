import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.timeseries.HoltWinters;
import weka.classifiers.timeseries.WekaForecaster;
import weka.core.Instances;

import java.util.List;

public class TimeSeries {
    public void TrainAndTestModel(Instances trainData, Instances testData) {
        try {
            trainData.setClass(trainData.attribute("Magnitude"));
            testData.setClass(trainData.attribute("Magnitude"));

            WekaForecaster forecaster = new WekaForecaster();
            forecaster.setFieldsToForecast("Magnitude");

            forecaster.getTSLagMaker().setTimeStampField("EventDate"); // date time stamp


            // add a month of the year indicator field
            forecaster.getTSLagMaker().setAddMonthOfYear(true);

            // add a quarter of the year indicator field
            forecaster.getTSLagMaker().setAddQuarterOfYear(true);

            // build the model
            forecaster.buildForecaster(trainData, System.out);

            // prime the forecaster with enough recent historical data
            // to cover up to the maximum lag. In our case, we could just supply
            // the 12 most recent historical instances, as this covers our maximum
            // lag period
            forecaster.primeForecaster(trainData);

            // forecast for 12 units (months) beyond the end of the
            // training data
            List<List<NumericPrediction>> forecast = forecaster.forecast(12, System.out);

            // output the predictions. Outer list is over the steps; inner list is over
            // the targets
            for (int i = 0; i < 12; i++) {
                List<NumericPrediction> predsAtStep = forecast.get(i);
                NumericPrediction predForTarget = predsAtStep.get(0);
                System.out.println("" + predForTarget.predicted() + " ");
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void TestModel(Instances data) {

    }

    public void CalculateConfusionMatrix() {

    }
}
