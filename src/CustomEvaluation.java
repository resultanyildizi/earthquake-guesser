import weka.core.Instances;

public class CustomEvaluation {

    private int predAsMinorButMinor = 0;
    private int predAsModerateButMinor = 0;
    private int predAsStrongButMinor = 0;

    private int predAsMinorButModerate = 0;
    private int predAsModerateButModerate = 0;
    private int predAsStrongButModerate = 0;

    private int predAsMinorButStrong = 0;
    private int predAsModerateButStrong = 0;
    private int predAsStrongButStrong = 0;

    private int tpMinor = 0;
    private int tnMinor = 0;
    private int fpMinor = 0;
    private int fnMinor = 0;

    private int tpModerate = 0;
    private int tnModerate = 0;
    private int fpModerate = 0;
    private int fnModerate = 0;

    private int tpStrong = 0;
    private int tnStrong = 0;
    private int fpStrong = 0;
    private int fnStrong = 0;

    private double precisMinor = 0;
    private double recallMinor = 0;
    private double f1Minor = 0;
    private double accMinor = 0;
    private double suppMinor= 0;

    private double precisModerate = 0;
    private double recallModerate = 0;
    private double f1Moderate = 0;
    private double accModerate = 0;
    private double suppModerate= 0;

    private double precisStrong = 0;
    private double recallStrong = 0;
    private double f1Strong = 0;
    private double accStrong = 0;
    private double suppStrong= 0;


    Instances testData;
    Instances predictedData;

    public CustomEvaluation(Instances testData, Instances predictedData) {
        for (int i = 0; i < testData.numInstances(); i++) {

            var real = testData.instance(i).stringValue(testData.attribute("RichterScale"));
            var pred = predictedData.instance(i).stringValue(predictedData.attribute("RichterScale"));


            if (real.equals("Minor") && pred.equals("Minor")) {
                predAsMinorButMinor++;
            } else if (real.equals("Moderate") && pred.equals("Minor")) {
                predAsMinorButModerate++;
            } else if (real.equals("Strong") && pred.equals("Minor")) {
                predAsMinorButStrong++;
            } else if (real.equals("Minor") && pred.equals("Moderate")) {
                predAsModerateButMinor++;
            } else if (real.equals("Moderate") && pred.equals("Moderate")) {
                predAsModerateButModerate++;
            } else if (real.equals("Strong") && pred.equals("Moderate")) {
                predAsModerateButStrong++;
            } else if (real.equals("Minor") && pred.equals("Strong")) {
                predAsStrongButMinor++;
            } else if (real.equals("Moderate") && pred.equals("Strong")) {
                predAsStrongButModerate++;
            } else if (real.equals("Strong") && pred.equals("Strong")) {
                predAsStrongButStrong++;
            }
        }

            tpMinor = predAsMinorButMinor;
            tnMinor = predAsModerateButModerate + predAsStrongButModerate + predAsStrongButStrong + predAsModerateButStrong;
            fpMinor = predAsMinorButModerate + predAsMinorButStrong;
            fnMinor = predAsModerateButMinor + predAsStrongButMinor;

            accMinor = (double) (tpMinor + tnMinor) / (double) (tpMinor + tnMinor + fpMinor + fnMinor);
            precisMinor = (double) (tpMinor) / (double) (tpMinor + fpMinor);
            recallMinor = (double) (tpMinor) / (double) (tpMinor + fnMinor);

            suppMinor = predAsMinorButMinor + predAsModerateButMinor + predAsStrongButMinor;
            f1Minor = 2 * precisMinor * recallMinor / (precisMinor + recallMinor);


            tpModerate = predAsMinorButModerate;
            tnModerate = predAsMinorButMinor + predAsStrongButMinor + predAsMinorButStrong + predAsStrongButStrong;
            fpModerate = predAsModerateButMinor + predAsModerateButStrong;
            fnModerate = predAsMinorButModerate + predAsStrongButModerate;

            accModerate = (double) (tpModerate + tnModerate) / (double) (tpModerate + tnModerate + fpModerate + fnModerate);
            precisModerate = (double) (tpModerate) / (double) (tpModerate + fpModerate);
            recallModerate = (double) (tpModerate) / (double) (tpModerate + fnModerate);
            suppModerate = predAsMinorButModerate + predAsModerateButModerate + predAsStrongButModerate;
            f1Moderate = 2 * precisModerate * recallModerate / (precisModerate + recallModerate);

            tpStrong = predAsStrongButStrong;
            tnStrong = predAsMinorButMinor + predAsModerateButMinor + predAsMinorButModerate + predAsModerateButModerate;
            fpStrong = predAsStrongButMinor + predAsStrongButModerate;
            fnStrong = predAsMinorButStrong + predAsModerateButStrong;

            accStrong = (double) (tpStrong + tnStrong) / (double) (tpStrong + tnStrong + fpStrong + fnStrong);
            precisStrong = (double) (tpStrong) / (double) (tpStrong + fpStrong);
            recallStrong = (double) (tpStrong) / (double) (tpStrong + fnStrong);
            suppStrong = predAsMinorButStrong + predAsModerateButStrong + predAsStrongButStrong;
            f1Strong = 2 * precisStrong * recallStrong / (precisStrong + recallStrong);


    }

    public void PrintConfusionMatrix() {
        System.out.println("              === Confusion Matrix ===");
        System.out.println("         " + String.format("%1$10s%2$10s%3$10s", "Minor", "Moderate", "Strong"));
        System.out.println("Minor    " + String.format("%1$10s%2$10s%3$10s", predAsMinorButMinor, predAsMinorButModerate, predAsMinorButStrong));
        System.out.println("Moderate " + String.format("%1$10s%2$10s%3$10s", predAsModerateButMinor, predAsModerateButModerate, predAsModerateButStrong));
        System.out.println("Strong   " + String.format("%1$10s%2$10s%3$10s", predAsStrongButMinor, predAsStrongButModerate, predAsStrongButStrong));
        System.out.println();
        System.out.println("         " + String.format("%1$10s%2$10s%3$10s%4$10s%5$10s", "Accuracy", "Precision", "Recall", "F1","Support"));
        System.out.println("Minor    " + String.format("%1$10.2f%2$10.2f%3$10.2f%4$10.2f%5$10.2f", accMinor,  precisMinor, recallMinor, f1Minor, suppMinor));
        System.out.println("Moderate " + String.format("%1$10.2f%2$10.2f%3$10.2f%4$10.2f%5$10.2f", accModerate, precisModerate, recallModerate, f1Moderate, suppModerate));
        System.out.println("Strong   " + String.format("%1$10.2f%2$10.2f%3$10.2f%4$10.2f%5$10.2f", accStrong, precisStrong, recallStrong, f1Strong, suppStrong));
        System.out.println("avg / tot" + String.format("%1$10.2f%2$10.2f%3$10.2f%4$10.2f%5$10.2f", (accStrong + accMinor + accModerate) /3 , (precisStrong + precisModerate + precisMinor) /3, (recallStrong + recallModerate + recallMinor) /3, (f1Strong + f1Moderate + f1Minor) /3, suppStrong + suppModerate + suppMinor));

    }

}
