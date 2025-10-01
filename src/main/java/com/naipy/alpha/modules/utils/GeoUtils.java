package com.naipy.alpha.modules.utils;

import com.naipy.alpha.modules.exceptions.services.InvalidParameterException;

public class GeoUtils {
    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double RADIUS_LIMIT = 0.05;
    private static final double RADIUS_IN_KM_DEFAULT = 2.0;

    public static BoundingBox calculateBoundingBox(Double lat, Double lng, Double radiusKm) {
        if (radiusKm == null)
            radiusKm = RADIUS_IN_KM_DEFAULT;
        else if (radiusKm > RADIUS_LIMIT)
            throw new InvalidParameterException("The radius exceeds the limit. Radius: " + radiusKm);
        double latDegree = radiusKm / 111.0; // 1 grau de latitude ~111 km
        double lngDegree = radiusKm / (111.0 * Math.cos(Math.toRadians(lat)));

        double latMin = lat - latDegree;
        double latMax = lat + latDegree;
        double lngMin = lng - lngDegree;
        double lngMax = lng + lngDegree;

        return new BoundingBox(latMin, latMax, lngMin, lngMax);
    }

    public record BoundingBox(double latMin, double latMax, double lngMin, double lngMax) {}
}
