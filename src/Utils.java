import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;

public class Utils {
    public static String ConvertMagnitudeToRichterScale(double magnitude) {
        String richter = "Minor";

        if (magnitude <= 3.5) {
            richter = "Minor";
        } else if (magnitude <= 4.5) {
            richter = "Moderate";
        } else {
            richter = "Strong";
        }

//
//        else if (magnitude <= 9) {
//            richter = "Major";
//        } else {
//            richter = "Great";
//        }
        return richter;
    }

    public static Instances CalculateRichterClass(Instances data) {
        try {
            Add addFilter = new Add();
            addFilter.setAttributeIndex("last");
            addFilter.setNominalLabels("Minor, Moderate, Strong");
            addFilter.setAttributeName("RichterScale");
            addFilter.setInputFormat(data);

            Instances newData = Filter.useFilter(data, addFilter);

            for (int i = 0; i < newData.numInstances(); i++) {
                double magnitude = newData.instance(i).value(newData.attribute("Magnitude"));

                String richter = Utils.ConvertMagnitudeToRichterScale(magnitude);

                newData.instance(i).setValue(newData.attribute("RichterScale"), richter);
            }

            return newData;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }

    }
}
