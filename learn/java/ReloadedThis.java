class ReloadedThis {
	int a;
	int b;

	ReloadedThis(int i, int j) {
		a = i;
		b = j;
	}

	ReloadedThis(int i) {
		this(i, i);
	}

	ReloadedThis() {
		this(0);
	}
}
