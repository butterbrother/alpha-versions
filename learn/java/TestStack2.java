class Stack {
	private int stck[];
	private int tos;
	final public int SIZE;

	Stack(int size) {
		stck = new int[size];
		tos = -1;
		SIZE = size;
	}

	void push(int item) {
		if (tos == stck.length-1)
			System.out.println("Стек полон.");
		else
			stck[++tos] = item;
	}

	int pop() {
		if (tos < 0) {
			System.out.println("Стек не загружен.");
			return 0;
		}
		else
			return stck[tos--];
	}
}

class TestStack2 {
	public static void main(String args[]) {
		Stack mystack1 = new Stack(5);
		Stack mystack2 = new Stack(8);

		for (int i=0; i<5; i++)
			mystack1.push(i);
		for (int i=0; i<8; i++)
			mystack2.push(i);

		System.out.println("Содержимое стека 1:");
		for (int i=0; i<5; i++)
			System.out.println("[" + i + "]: " + mystack1.pop());

		System.out.println("Содержимое стека 2:");
		for (int i=0; i<8; i++)
			System.out.println("[" + i + "]: " + mystack2.pop());

		System.out.println(mystack1.SIZE);
	}
}
