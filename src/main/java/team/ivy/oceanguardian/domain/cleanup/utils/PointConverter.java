package team.ivy.oceanguardian.domain.cleanup.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class PointConverter  {
    public static Point convertToJTSPoint(org.geolatte.geom.Point geolattePoint) {
        // geolatte Point 객체의 x, y 좌표 추출
        double x = geolattePoint.getPosition().getCoordinate(0);
        double y = geolattePoint.getPosition().getCoordinate(1);

        // JTS GeometryFactory를 사용하여 JTS Point 객체 생성
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        return geometryFactory.createPoint(new Coordinate(x, y));
    }
}
