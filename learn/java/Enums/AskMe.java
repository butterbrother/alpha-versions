package Enums;

import java.util.Random;

enum Answer {
	NO, YES, MAYBE, LATER, SOON, NEVER
}

class Question {
	Random rand = new Random();
	Answer ask() {
		int prob = (int) (100 * rand.nextDouble());

		if (prob < 15)
			return Answer.MAYBE;
		else if (prob < 30)
			return Answer.NO;
		else if (prob < 60)
			return Answer.YES;
		else if (prob < 75)
			return Answer.LATER;
		else if (prob < 98)
			return Answer.SOON;
		else
			return Answer.NEVER;
	}
}

class AskMe {
	static void answer(Answer result) {
		switch(result) {
			case NO:
				System.out.println("Нет");
				break;
			case YES:
				System.out.println("Да");
				break;
			case LATER:
				System.out.println("Позже");
				break;
			case SOON:
				System.out.println("Скоро");
				break;
			case MAYBE:
				System.out.println("Возможно");
				break;
			case NEVER:
				System.out.println("Никогда");
				break;
		}
	}
	public static void main(String args[]) {
		Question q = new Question();
		answer(q.ask());
		answer(q.ask());
		answer(q.ask());
		answer(q.ask());
	}
}
