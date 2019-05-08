package group4.maths;

public abstract class Spline {

    protected Vector3f[] points;

    /**
     * Creates a new spline with the given points
     *
     * @param points
     */
    public Spline(Vector3f[] points) {
        this.points = points;
    }


    /**
     * Returns the point on the spline at the (t/1.0) part of the spline.
     * @param u place in time of the point (between 0.0 and 1.0)
     * @return vector of the point on the spline
     */
    public abstract Vector3f getPoint(float u);


    /**
     * Returns the tangent on the spline at the (t/1.0) part of the spline.
     * @param u place in time of the point (between 0.0 and 1.0)
     * @return vector of the tangent
     */
    public abstract Vector3f getTangent(float u);


}
