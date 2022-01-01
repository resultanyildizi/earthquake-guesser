import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Dictionary;
import java.util.HashMap;

public class EarthquakeMap {
    private static HashMap<Integer, Color> colormap;
    private static int MAP_WIDTH = 1387;
    private static int MAP_HEIGHT = 640;
    private static double MAX_LON = -999;
    private static double MAX_LAT = -999;
    private static double MIN_LAT = 99999;
    private static double MIN_LON = 99999;

    private HashMap<Integer, Color> GetColorMap() {
        if(colormap == null) {
            colormap = new HashMap<>();
            colormap.put(0, Color.RED);
            colormap.put(1, Color.GREEN);
            colormap.put(2, Color.BLUE);
        }
        return colormap;
    }


    EarthqInstance[] instances;

    public EarthquakeMap(EarthqInstance[] instances) {
        this.instances = instances;
    }

    public void DrawMap() {
        Frame frame = new Frame("Turkey Map");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.add(new JLabel(new ImageIcon("data/turkey_map.png")));
        frame.pack();
        frame.setSize(MAP_WIDTH, MAP_HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);

        Graphics g = frame.getGraphics();
        DrawPoints(g);

    }

    private void DrawPoints(Graphics g) {
        for (var instance: instances) {
            DrawInstance(g, instance);
        }
    }

    private double FindMaxLon() {
        if(MAX_LON == -999) {
            double max = instances[0].lon;
            for (var instance: instances) {
                if(instance.lon > max)
                    max = instance.lon;
            }
            MAX_LON = max;
        }
        return MAX_LON;
    }
    private double FindMinLon() {
        if(MIN_LON == 99999) {
            double min = instances[0].lon;
            for (var instance: instances) {
                if(instance.lon < min)
                    min = instance.lon;
            }
            MIN_LON = min;
        }
        return MIN_LON;
    }

    private double FindMaxLat() {
        if(MAX_LAT == -999) {
            double max = instances[0].lat;
            for (var instance : instances) {
                if (instance.lat > max)
                    max = instance.lat;
            }
            return max;
        }
        return MAX_LAT;
    }

    private double FindMinLat() {
        if(MIN_LAT == 99999) {
            double min = instances[0].lat;
            for (var instance : instances) {
                if (instance.lat < min)
                    min = instance.lat;
            }
            MIN_LAT = min;
        }
        return MIN_LAT;
    }



    private void DrawInstance(Graphics g, EarthqInstance instance) {
        var normalizedLocation = NormalizeInstance(instance);
        g.setColor(GetColorMap().get(instance.faultline));
        g.fillOval(normalizedLocation.f, MAP_HEIGHT - (int)normalizedLocation.s, 6,6);
    }

    private Tuple<Integer, Integer> NormalizeInstance(EarthqInstance instance) {
        var maxLat = FindMaxLat();
        var minLat = FindMinLat();
        var y = (int) (((instance.lat - minLat ) * (MAP_HEIGHT) / (maxLat - minLat)));

        var maxLon = FindMaxLon();
        var minLon = FindMinLon();
        var x = (int) (((instance.lon - minLon ) * (MAP_WIDTH) / (maxLon - minLon)));

        return new Tuple<Integer, Integer>(x,y);
    }

    private class Tuple<X, Y> {
        X f;
        Y s;
        public Tuple(X f, Y s) {
            this.f = f;
            this.s = s;
        }

        public X getF() {
            return f;
        }

        public Y getS() {
            return s;
        }

        @Override
        public String toString() {
            return "Tuple{" +
                    "f=" + f +
                    ", s=" + s +
                    '}';
        }
    }
}
