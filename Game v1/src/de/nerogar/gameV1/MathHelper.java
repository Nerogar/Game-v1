package de.nerogar.gameV1;

public final class MathHelper {

	private static double[] sin;
	private static double[] cos;
	private static double[] tan;

	private static final int LOOKUPLENGTH = 100;

	private static final int SINLENGTH = LOOKUPLENGTH;
	private static final int COSLENGTH = LOOKUPLENGTH;
	private static final int TANLENGTH = LOOKUPLENGTH;

	private static final double PI = 3.1415927F;

	static {

		sin = new double[(int) (SINLENGTH * PI) + 1];
		for (int i = 0; i < sin.length; i++) {
			sin[i] = Math.sin((double) i / SINLENGTH);
		}

		cos = new double[(int) (COSLENGTH * PI) + 1];
		for (int i = 0; i < cos.length; i++) {
			cos[i] = Math.cos((double) i / COSLENGTH);
		}

		tan = new double[(int) (TANLENGTH * PI) + 1];
		for (int i = 0; i < tan.length; i++) {
			tan[i] = Math.tan((double) i / TANLENGTH);
		}

	}

	public static double sin(float x) {

		double x2 = x;
		x2 = x2 % PI;
		if (x2 < 0) x2 += PI;
		x2 = x2 * SINLENGTH;

		return sin[(int) x2];

	}

	public static double cos(float x) {

		double x2 = x;
		x2 = x2 % PI;
		if (x2 < 0) x2 += PI;
		x2 = x2 * COSLENGTH;

		return cos[(int) x2];

	}

	public static double tan(float x) {

		double x2 = x;
		x2 = x2 % PI;
		if (x2 < 0) x2 += PI;
		x2 = x2 * TANLENGTH;

		return tan[(int) x2];

	}

	public static double sqrt(double x) {

		return Math.sqrt(x);

	}

	public static double abs(double x) {

		if (x < 0) return -1 * x;
		return x;

	}

	public static int roundTo0(double d) {
		return (int) d;
	}

	public static int divDownToInt(double d, int n) {
		int ret = (int) (d / n);
		if (((int) (d / n)) != (d / n)) if (d < 0) ret--;

		return ret;
	}

	public static int divUpToInt(double d, int n) {
		int ret = (int) ((d + n) / n);
		if (((int) (d / n)) != (d / n)) if (d < 0) ret--;

		return ret;
	}

	public static int roundDownToInt(double d, int n) {
		int ret = (int) (d / n);
		ret = ret * n;
		if (((int) (d / n)) != (d / n)) if (d < 0) ret -= n;
		return ret;
	}

	public static int roundUpToInt(double d, int n) {
		int ret = (int) (d / n);
		ret = (ret + 1) * n;
		if (((int) (d / n)) == (d / n)) ret -= n;
		return ret;
	}

	public static double modToInt(double d, int n) {
		return d - roundDownToInt(d, n);
	}

	public static double getHightest(double... d) {
		double dH = d[0];
		for (int i = 0; i < d.length; i++) {
			if (d[i] > dH) dH = d[i];
		}
		return dH;
	}

	public static int getHightest(int... d) {
		int dH = d[0];
		for (int i = 0; i < d.length; i++) {
			if (d[i] > dH) dH = d[i];
		}
		return dH;
	}

	public static double getLowest(double... d) {
		double dH = d[0];
		for (int i = 0; i < d.length; i++) {
			if (d[i] < dH) dH = d[i];
		}
		return dH;
	}

	public static int getLowest(int... d) {
		int dH = d[0];
		for (int i = 0; i < d.length; i++) {
			if (d[i] < dH) dH = d[i];
		}
		return dH;
	}
}
