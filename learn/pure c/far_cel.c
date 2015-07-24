#include <stdio.h>

int main() {
	float fahr = 0.0;
	int min = 0;
	int max = 100;
	int step = 10;
	float fdn = 5.0/9.0;

	printf("%6s\t%6s\n", "cel", "fah");
	float cel = min;
	while (cel <= max) {
		fahr = cel * fdn + 32.0;
		printf("%3.0f\t%6.1f\n", cel, fahr);
		cel += step;
	}
}
