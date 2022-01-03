import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.timeseries.HoltWinters;
import weka.classifiers.timeseries.WekaForecaster;
import weka.core.Instances;
import weka.filters.supervised.attribute.TSLagMaker;

import java.util.List;

public class TimeSeries {
    public void TrainAndTestModel(Instances trainData, Instances testData) {
        try {
            trainData.setClass(trainData.attribute("Magnitude"));
            testData.setClass(trainData.attribute("Magnitude"));

            WekaForecaster forecaster = new WekaForecaster();
            forecaster.setFieldsToForecast("Magnitude");

            forecaster.getTSLagMaker().setTimeStampField("EventDate"); // date time stamp

            forecaster.setBaseForecaster(new HoltWinters());

            forecaster.getTSLagMaker().setMinLag(1);
            forecaster.getTSLagMaker().setMaxLag(12);


            forecaster.getTSLagMaker().setPeriodicity(TSLagMaker.Periodicity.MONTHLY);

            // build the model
            forecaster.buildForecaster(trainData, System.out);

            // prime the forecaster with enough recent historical data
            // to cover up to the maximum lag. In our case, we could just supply
            // the 12 most recent historical instances, as this covers our maximum
            // lag period

            forecaster.primeForecaster(trainData);

            // forecast for 12 units (months) beyond the end of the
            // training data
            List<List<NumericPrediction>> forecast = forecaster.forecast(100, System.out);

            System.out.println(forecast.size());
            System.out.println(forecast.get(0).size());

            // output the predictions. Outer list is over the steps; inner list is over
            // the targets
            for (int i = 0; i < forecast.size(); i++) {
                List<NumericPrediction> predsAtStep = forecast.get(i);

                for (int j = 0; j < predsAtStep.size(); j++) {
                    NumericPrediction predForTarget = predsAtStep.get(j);
                    System.out.println(predForTarget.predicted());
                }
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
