

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.core.*;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;

class EarthquakeGuesser {

    public static void main(String[] args) throws Exception {
        Preprocessor pp = new Preprocessor("data/earthquake.arff");
        pp.RemoveUnrelatedFeatures();

        pp.SetRichterScaleClasses();
        System.out.println(pp.data.instance(30).stringValue(pp.data.numAttributes() - 1));
        pp.ClusterInstancesForFaultLines();
        System.out.println(pp.data.instance(30).stringValue(pp.data.numAttributes() - 1));

        pp.SaveProcessedData("data/preprocessed_earthquake.arff");

    }

}
