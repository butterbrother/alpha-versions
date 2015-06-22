#include <stdio.h>

int main() {
	int step = 20;
	float fahr = 0;
	int upper = 300;
	float celsius = 0;
	float fdn = 5.0/9.0;

	printf("%3s\t%6s\n", "cel", "far");
	while (fahr <= upper) {
		celsius = fdn * (fahr - 32.0);
		printf("%3.0f\t%6.1f\n", fahr, celsius);
		fahr += step;
	}
}
