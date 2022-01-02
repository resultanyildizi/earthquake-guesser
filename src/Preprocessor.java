import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.Remove;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Preprocessor {

    Instances data;

    Instances testData;
    Instances trainData;

    EarthqInstance[] eqPoints;

        public  Preprocessor(String filename) throws Exception
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

            /*
            % Richter Scale:
            % 1-4 : Minor
            % 5-6 : Moderate
            % 7   : Strong
            % 8-9 : Major
            % 10  : Great
            */

            // Load data
            data = new Instances(new BufferedReader(new FileReader(filename)));
        }

        public void RemoveUnrelatedFeatures() {
            try {
                Remove remove = new Remove();
                remove.setAttributeIndicesArray(new int[] {0, 2, 8});// EventId, EpicenterAgency, Location bilgilerini çıkar
                remove.setInputFormat(data); // init filter
                Instances filtered = Filter.useFilter(data, remove); // apply filter

                data = filtered;
            }catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void SetRichterScaleClasses() {
            try {
                Add addFilter = new Add();
                addFilter.setAttributeIndex("last");
                addFilter.setNominalLabels("Minor, Moderate, Strong, Major, Great");
                addFilter.setAttributeName("RichterScale");
                addFilter.setInputFormat(data);
                Instances newData = Filter.useFilter(data, addFilter);

                int magnitudeIndex = data.attribute("Magnitude").index();

                for (int i = 0; i < newData.numInstances(); i++) {
                    double magnitude = newData.instance(i).value(magnitudeIndex);

                    String richter = "Minor";

                    if (magnitude < 5) {
                        richter = "Minor";
                    } else if (magnitude <= 6) {
                        richter = "Moderate";
                    } else if (magnitude <= 7) {
                        richter = "Strong";
                    } else if (magnitude <= 9) {
                        richter = "Major";
                    } else {
                        richter = "Great";
                    }

                    newData.instance(i).setValue(newData.attribute("RichterScale"), richter);
                }

                data = newData;
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void ClusterInstancesForFaultLines() {
            try {

                int lonIndex = data.attribute("EpicenterLon").index();
                int latIndex = data.attribute("EpicenterLat").index();


                Remove remove = new Remove();
                remove.setAttributeIndicesArray(new int[] {lonIndex, latIndex}); //enlem ve boylamı almak için filtre uygulanıyor
                remove.setInvertSelection(true);
                remove.setInputFormat(data); // init filter
                Instances filtered = Filter.useFilter(data, remove); // apply filter

                EM clusterer = new EM();
                clusterer.setNumClusters(3);
                clusterer.setNumFolds(10);
                clusterer.setMaxIterations(100);
                clusterer.buildClusterer(filtered);

                ClusterEvaluation eval;
                eval = new ClusterEvaluation();
                eval.setClusterer(clusterer);
                eval.evaluateClusterer(new Instances(filtered));
                System.out.println(eval.clusterResultsToString());

                double[] cluster = eval.getClusterAssignments();
                Add addFilter;
                addFilter = new Add();
                addFilter.setAttributeIndex("last");
                addFilter.setNominalLabels("0, 1, 2");
                addFilter.setAttributeName("FaultLine");

                addFilter.setInputFormat(data);
                Instances newdata = Filter.useFilter(data, addFilter);


                eqPoints = new EarthqInstance[newdata.numInstances()];
                for (int i = 0; i < data.numInstances(); i++) {
                    newdata.instance(i).setValue(newdata.numAttributes() - 1, String.valueOf( (int)cluster[i]));
                    eqPoints[i] = new EarthqInstance(data.instance(i).value(0), data.instance(i).value(1), (int) cluster[i]);
                }

                data = newdata;


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void SaveProcessedData(String filename) {
            try {
                ArffSaver s = new ArffSaver();
                s.setInstances(data);
                s.setFile(new File(filename));
                s.writeBatch();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



