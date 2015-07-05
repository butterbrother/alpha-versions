class MemoryDemo {
	public static void main(String args[]) {
		Runtime r = Runtime.getRuntime();
		long mem1, mem2;
		Integer someints[] = new Integer[1000];

		System.out.println("Всего памяти: " + r.totalMemory());
		mem1 = r.freeMemory();

		System.out.println("Свободной памяти вначале: " + mem1);
		r.gc();
		mem1 = r.freeMemory();
		System.out.println("Свободной памяти после сбора \"мусора\": " + mem1);

		for (int i=0; i<1000; i++)
			someints[i] = new Integer(i);
		mem2 = r.freeMemory();
		System.out.println("Свободной памяти после распределения: " + mem2);
		System.out.println("Использовано памяти для распределения: " + (mem1-mem2));

		for (int i=0; i<1000; i++) someints[i]=null;

		r.gc();

		mem2 = r.freeMemory();
		System.out.println("Свободной памяти после сбора отброшенных Integer: " + mem2);
	}
}
