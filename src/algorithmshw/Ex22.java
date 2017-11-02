package algorithmshw;

import edu.princeton.cs.algs4.Interval1D;

/**
 * @author Levi Muriuki
 * 3.4.22
 */

public class Ex22 {
    private
    class Point2D
    {
        private double x;
        private double y;

        Point2D(double x, double y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode()
        {
            int hash = 17;
            hash = (31 * hash) + (int)((Double.doubleToLongBits(x))^(Double.doubleToLongBits(x) >>> 32));
            hash = (31 * hash) + (int)((Double.doubleToLongBits(y))^(Double.doubleToLongBits(y) >>> 32));

            return hash;
        }
    }

    class Interval
    {
        private double min;
        private double max;

        Interval(double min, double max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public int hashCode()
        {
            int hash = 17;
            hash = (31 * hash) + (int)((Double.doubleToLongBits(min))^(Double.doubleToLongBits(min) >>> 32));
            hash = (31 * hash) + (int)((Double.doubleToLongBits(max))^(Double.doubleToLongBits(max) >>> 32));

            return hash;
        }
    }

    class Interval2D
    {
        private Interval1D x;
        private Interval1D y;

        Interval2D(Interval1D x, Interval1D y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode()
        {
            int hash = 17;
            hash = (31 * hash) + x.hashCode();
            hash = (31 * hash) + y.hashCode();

            return hash;
        }
    }

    class Date
    {
        private int day;
        private int month;
        private int year;

        Date(int day, int month, int year) {
            this.day = day;
            this.month = month;
            this.year = year;
        }

        @Override
        public int hashCode()
        {
            int hash = 17;
            hash = (31 * hash) + day;
            hash = (31 * hash) + month;
            hash = (31 * hash) + year;

            return hash;
        }
    }

    public static void main(String[] args)
    {
        Ex22 ex = new Ex22();
        Point2D p = ex.new Point2D(7, 12);
        Point2D p1 = ex.new Point2D(7, 12);
        System.out.println("Point hash equal?: " + (p.hashCode() == p1.hashCode()));

        Interval i = ex.new Interval(3, 500);
        Interval i2 = ex.new Interval(3, 500);
        System.out.println("Interval hash equal?: " + (i.hashCode() == i2.hashCode()));

        Interval1D id = new Interval1D(9.6, 15.3);
        Interval1D id1 = new Interval1D(23.5, 99.6);
        Interval2D id2 = ex.new Interval2D(id, id1);
        Interval2D id3 = ex.new Interval2D(id, id1);

        System.out.println("Interval2D hash equal?: " + (id2.hashCode() == id3.hashCode()));

        Date d = ex.new Date(1, 11, 2017);
        Date d2 = ex.new Date(1, 11, 2017);
        System.out.println("Date hash equal?: " + (d.hashCode() == d2.hashCode()));
    }
}
