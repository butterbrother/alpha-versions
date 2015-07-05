class Light {
	public static void main(String args[]) {
		long Speed_Ms = 299792458;
		long Seconds = 1000;
		long Distance = Speed_Ms * Seconds;
		System.out.print("За " + Seconds + " секунд");
		System.out.print(" свет пройдёт расстояние ");
		System.out.println(Distance + " метров");
	}
}
