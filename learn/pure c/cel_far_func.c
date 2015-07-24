#include <stdio.h>

#define LOW 0
#define HIGH 100

double fahrenheit(double celsius);
double celsius(double fahrenheit);

int main() {
	int i;
	printf("Celsius to fahrenheit:\n");
	printf("%4s\t%6s\n", "C:", "F:");
	for (i = 0; i <= 100; ++i)
		printf("%4d\t%6.2f\n", i, fahrenheit(i));

	printf("\nFahrenheit to celsius:\n");
	printf("%4s\t%6s\n", "C:", "F:");
	for (i = 0; i <= 100; ++i)
		printf("%4d\t%6.2f\n", i, celsius(i));

	return 0;
}

double fahrenheit(double celsius) {
	return celsius * (9.0 / 5.0) + 32.0;
}

double celsius(double fahrenheit) {
	return (fahrenheit - 32.0) * (5.0 / 9.0);
}
