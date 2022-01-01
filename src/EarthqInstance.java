public class EarthqInstance {
    double lon;
    double lat;
    int faultline;


    public EarthqInstance(double lon, double lat, int faultline) {
        this.lon = lon;
        this.lat = lat;
        this.faultline = faultline;
    }

    @Override
    public String toString() {
        return "EarthqInstance{" +
                "lon=" + lon +
                ", lat=" + lat +
                ", faultline=" + faultline +
                '}';
    }
}
