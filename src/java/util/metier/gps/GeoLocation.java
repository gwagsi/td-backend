/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util.metier.gps;

/**
 *
 * @author erman
 */
public class GeoLocation {

    private float minLat = 0; // in degree
    private float maxLat = 0;//in degree
    private float minLong = 0;//in degree
    private float maxLong = 0;//in degree

    private static final double RADUIS = 3963.105;// rayon de la terre en miles.
    // = 6378 en kilometre
    //public static final double radius= 3958.756;// rayon de la terre en miles.
    // = 6371 en kilometre

    private static final double MIN_LAT = Math.toRadians(-90d);  // -PI/2
    private static final double MAX_LAT = Math.toRadians(90d);   //  PI/2
    private static final double MIN_LON = Math.toRadians(-180d); // -PI
    private static final double MAX_LON = Math.toRadians(180d);  //  PI

    public GeoLocation() {
    }

    /**
     *
     * Cette fonction permet de calculer les latitude maximal et minimal
     *
     * @param latp latitude du point a centrale
     * @param lonp longitude du point central
     * @param distance distance consideree en miles ,
     */
    public void setBoundingCoordinates(double latp, double lonp, double distance) {
        double lat = Math.toRadians(latp);
        double lon = Math.toRadians(lonp);

        if (lat < MIN_LAT || lat > MAX_LAT || lon < MIN_LON || lon > MAX_LON) {
            IllegalArgumentException ex = new IllegalArgumentException();
            Throwable msg = new Throwable("latitude or  longitude out of range");
            ex.addSuppressed(msg);
            throw ex;
        }
        if (distance < 0d) {
            IllegalArgumentException ex = new IllegalArgumentException();
            Throwable msg = new Throwable("Distance negative");
            ex.addSuppressed(msg);
            throw ex;
        }

        double radDist = distance / RADUIS;

        double minLataux = lat - radDist;
        double maxLataux = lat + radDist;
        double minLonaux = 0;
        double maxLonaux = 0;

        if (minLataux > MIN_LAT && maxLataux < MAX_LAT) {
            double deltaLon = Math.asin(Math.sin(radDist) / Math.cos(lat));
            minLonaux = lon - deltaLon;
            if (minLonaux < MIN_LON) {
                minLonaux += 2d * Math.PI;
            }
            maxLonaux = lon + deltaLon;
            if (maxLonaux > MAX_LON) {
                maxLonaux -= 2d * Math.PI;
            }
        } else {
            // a pole is within the distance
            minLataux = Math.max(minLataux, MIN_LAT);
            maxLataux = Math.min(maxLataux, MAX_LAT);
            minLonaux = MIN_LON;
            maxLonaux = MAX_LON;
        }

        this.maxLat = (float) Math.toDegrees(maxLataux);
        this.minLat = (float) Math.toDegrees(minLataux);
        this.maxLong = (float) Math.toDegrees(maxLonaux);
        this.minLong = (float) Math.toDegrees(minLonaux);
        /*
        System.out.println(
                " latp = " + latp + " degree et lonp = " + lonp + " degree et distance = " + distance + " miles"
                + "\n minLat = " + this.minLat
                + "\n maxLat = " + this.maxLat
                + "\n minLon = " + this.minLong
                + "\n maxLon = " + this.maxLong
                + "\n Distance (minLat,minLon) = " + this.distanceTo(latp, lonp, this.minLat, this.minLong)
                + "\n Distance (maxLat,minLon) = " + this.distanceTo(latp, lonp, this.maxLat, this.minLong)
                + "\n Distance (maxLat,maxLon) = " + this.distanceTo(latp, lonp, this.minLat, this.maxLong)
                + "\n Distance (minLat,maxLon) = " + this.distanceTo(latp, lonp, this.minLat, this.maxLong)
        );
        */
    }

    public float getMinLat() {
        return minLat;
    }

    public void setMinLat(float minLat) {
        this.minLat = minLat;
    }

    public float getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(float maxLat) {
        this.maxLat = maxLat;
    }

    public float getMinLong() {
        return minLong;
    }

    public void setMinLong(float minLong) {
        this.minLong = minLong;
    }

    public float getMaxLong() {
        return maxLong;
    }

    public void setMaxLong(float maxLong) {
        this.maxLong = maxLong;
    }

    
    public float distanceTo(double latp, double lonp, double latx, double lonx) {
        return (float) (Math.acos(Math.sin(Math.toRadians(latx)) * Math.sin(Math.toRadians(latp))
                + Math.cos(Math.toRadians(latx)) * Math.cos(Math.toRadians(latp))
                * Math.cos(Math.toRadians(lonp) - Math.toRadians(lonx))) * RADUIS);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        GeoLocation object = new GeoLocation();
        object.setBoundingCoordinates(37.0619736, -97.03837120000003, 1000);// ici je passe dans l'ordre latitude, longitude du point centrale et distance en miles
		/*
         * Pour acceder aux valeur min et max, tu fais :
         * object.minLat, object.minLong, object.maxLat,object.maxLong
         * 
         * */

    }

}
