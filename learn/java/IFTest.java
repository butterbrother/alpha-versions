class FixedStack implements IntStack {
	private int stck[];
	private int tos;
	FixedStack(int size) {
		stck = new int[size];
		tos = -1;
	}

	public void push(int item) {
		if (tos == stck.length - 1)
			System.out.println("Стек полон");
		else
			stck[++tos] = item;
	}

	public int pop() {
		if (tos < 0) {
			System.out.println("Стек пуст.");
			return 0;
		}
		else
			return stck[tos--];
	}
}

class IFTest {
	private static void push(IntStack on, int value) {
		on.push(value);
	}
	private static void pop(IntStack ob) {
		System.out.println(ob.pop());
	}
	public static void main(String args[]) {
		FixedStack mystack1 = new FixedStack(5);
		FixedStack mystack2 = new FixedStack(8);

		for (int i = 0; i < 5; i++)
			push(mystack1, i);
		for (int i = 0; i < 8; i++)
			push(mystack2, i);

		System.out.println("Стек в mystack1:");
		for (int i=0; i<5; i++)
			pop(mystack1);

		System.out.println("Стек в mystack2:");
		for (int i=0; i<5; i++)
			pop(mystack2);
	}
}
