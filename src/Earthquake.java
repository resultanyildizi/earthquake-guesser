

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.core.*;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;

public class Earthquake
{

    public  Earthquake(String filename) throws Exception
    {
        /*
        % 1:   EventID: Kayıtlı ID
        % 2:   EventDate :Tarih
        % 3:   EpicenterAgency: Deprem Bilgisi
        % 4:   EpicenterLon : Merkez üssü Boylam
        % 5:   EpicenterLat : Merkez üssü Enlem
        % 6:   Type : Deprem tipi
        % 7:   Magnitude :Genlik
        % 8:   Depth: Derinlik
        % 9:   Location: Yer
        */
        ClusterEvaluation eval;
        DataSource src = new DataSource(filename);
        Instances data = src.getDataSet();

        Instances newdata=data;

        Remove remove = new Remove();
        remove.setAttributeIndices("4,5"); //enlem ve boylamı almak için filtre uygulanıyor
        remove.setInvertSelection(true);
        remove.setInputFormat(data); // init filter
        Instances filtered = Filter.useFilter(data, remove); // apply filter
        data=  filtered;

        EM clusterer = new EM();

        clusterer.setNumClusters(3);
        clusterer.setNumFolds(10);
        clusterer.setMaxIterations(100);
        clusterer.buildClusterer(data);
        eval = new ClusterEvaluation();
        eval.setClusterer(clusterer);
        eval.evaluateClusterer(new Instances(data));
        System.out.println(eval.clusterResultsToString());

        // output predictions
        System.out.println("# - cluster - distribution");
        double [] cluster = eval.getClusterAssignments();
        Add filter;
        filter = new Add();
        filter.setAttributeIndex("last");
        filter.setAttributeName("FaultLine");
        filter.setInputFormat(newdata);
        newdata = Filter.useFilter(newdata, filter);

        System.out.println("# of clusters: " + eval.getNumClusters());

        EarthqInstance[] instances = new EarthqInstance[newdata.numInstances()];
        for (int i = 0; i < data.numInstances(); i++) {
         newdata.instance(i).setValue(newdata.numAttributes()-1, cluster[i]);
         instances[i] = new EarthqInstance(data.instance(i).value(0), data.instance(i).value(1), (int) cluster[i]);
     }

        EarthquakeMap map = new EarthquakeMap(instances);
        map.DrawMap();

        ArffSaver s = new ArffSaver();
        s.setInstances(newdata);
        s.setFile(new File("data/earthquake_with_faultline.arff"));
        s.writeBatch();
    }


    public static void main(String[] args) throws Exception {
        new Earthquake("data/earthquake.arff");
    }

}
