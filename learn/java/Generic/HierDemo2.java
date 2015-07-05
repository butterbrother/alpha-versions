package Generic;

class NonGen {
	int num;

	NonGen(int i) {
		num = i;
	}

	int getnum() {
		return num;
	}
}

class Gen<T> extends NonGen {

