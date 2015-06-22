class DynStack implements IntStack {
	private int stck[];
	private int tos;

	DynStack(int size) {
		stck = new int[size];
		tos = -1;
	}

	public void push(int item) {
		if (tos==stck.length-1) {
			int temp[] = new int[stck.length * 2];
			for (int i=0; i < stck.length; i++)
				temp[i] = stck[i];
			stck = temp;
			stck[++tos] = item;
		}
		else
			stck[++tos] = item;
	}

	public int pop() {
		if(tos < 0) {
			System.out.println("Стек пуст.");
			return 0;
		}
		else
			return stck[tos--];
	}
}

class IFTest2 {
	private static void push(IntStack ob, int start, int end) {
		for (int i = start; i < end; i++)
			ob.push(i);
	}
	private static void pop(IntStack ob, int start, int end) {
		for (int i = start; i < end; i++)
			System.out.println(ob.pop());
	}
	public static void main(String args[]) {
		IntStack mystack1 = new DynStack(5);
		IntStack mystack2 = new DynStack(8);

		push(mystack1, 0, 12);
		push(mystack2, 0, 20);

		System.out.println("Стек в mystack1:");
		pop(mystack1, 0, 12);

		System.out.println("Стек в mystack2:");
		pop(mystack2, 0, 20);
	}
}
