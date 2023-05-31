package com.inretailpharma.digital.ordertracker.utils;

import java.math.BigDecimal;

public class SpaceUtils {
	
	private SpaceUtils() {
		
	}
	
	public static class Point {

        private BigDecimal latitude;
        private BigDecimal longitude;

        public Point(BigDecimal latitude, BigDecimal longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public String toString() {
            return "{\"Point\":{"
                + "\"latitude\":\"" + latitude + "\""
                + ", \"longitude\":\"" + longitude + "\""
                + "}}";
        }
    }

    public static double distanceBetweenPoints(Point firstPoint, Point secondPoint) {
        double latitude = Math.toRadians(secondPoint.latitude.subtract(firstPoint.latitude).doubleValue());
        double longitude = Math.toRadians(secondPoint.longitude.subtract(firstPoint.longitude).doubleValue());

        double latitudeSin = Math.sin(latitude / Constant.Integers.TWO);
        double longitudeSin = Math.sin(longitude / Constant.Integers.TWO);

        double preResult = Math.pow(latitudeSin, Constant.Integers.TWO) + Math.pow(longitudeSin, Constant.Integers.TWO)
            * Math.cos(Math.toRadians(firstPoint.latitude.doubleValue()))
            * Math.cos(Math.toRadians(secondPoint.latitude.doubleValue()));
        double postResult = Constant.Integers.TWO * Math.atan2(Math.sqrt(preResult), Math.sqrt(Constant.Integers.ONE - preResult));

        return Constant.EARTH_RADIUS * postResult;
    }

}
