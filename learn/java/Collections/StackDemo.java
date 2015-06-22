package Collections;

import java.util.*;

class StackDemo {
	static <T, V extends T> void showpush(Stack<T> st, V a) {
		st.push(a);
		System.out.println("push(" + a + ")");
		System.out.println("стек: " + st);
	}

	static <T> void showpop(Stack<T> st) {
		System.out.print("pop => ");
		T a = st.pop();
		System.out.println(a);
		System.out.println("стек: " + st);
	}

	public static void main(String args[]) {
		Stack<Integer> st = new Stack<Integer>();

		System.out.println("стек: " + st);

		showpush(st, 42);
		showpush(st, 66);
		showpush(st, 99);

		showpop(st);
		showpop(st);
		showpop(st);

		try {
			showpop(st);
		} catch (EmptyStackException e) {
			System.out.println("стек пуст");
		}
	}
}
