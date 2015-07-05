import static java.lang.Math.sqrt;
import static java.lang.Math.pow;

class StaticImport {
	public static void main(String args[]) {
		double side1 = 3.0, side2 = 4.0;

		double hypot=sqrt(pow(side1, 2) + pow(side2, 2));

		System.out.println(hypot);
	}
}
