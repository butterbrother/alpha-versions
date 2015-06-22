class Stack {
	int stack[] = new int[10];
	int tos;

	Stack() {
		tos = -1;
	}

	void push(int item) {
		if (tos==9)
			System.out.println("Стек полон");
		else
			this.stack[++tos] = item;
	}

	int pop() {
		if(tos < 0) {
			System.out.println("Стек не нагружен");
			return 0;
		}
		else
			return stack[tos--];
	}
}

class TestStack {
	public static void main(String args[]) {
		Stack mystack1 = new Stack();
		Stack mystack2 = new Stack();

		for (int i=0; i<10; i++)
			mystack1.push(i);

		for (int i=0; i<10; i++)
			mystack2.push(i);

		System.out.println("Стек в mystack1:");
		for (int i=0; i<10; i++)
			System.out.println(mystack1.pop());
		System.out.println("Стек в mystack2:");
		for (int i=0; i<10; i++)
			System.out.println(mystack2.pop());
	}
}
