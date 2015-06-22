/*
 * Напишите программу для вывода входного потока по одному слову с строке
 */
#include <stdio.h>
int main() {
	int c;
	for (;(c = getchar()) != EOF; putchar(c == ' ' ? '\n' : c));
}
