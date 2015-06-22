/* Задание - написать программу для копирования входного потока в выходной
 * с заменой каждой строки, состоящей из одного или нескольких пробеллов,
 * одним пробелом */
#include <stdio.h>

#define true 1
#define false 0
#define bool short

int main() {
	int rw;
	bool nosym = true, prevSpace = false;

	while ((rw = getchar()) != EOF)
		switch (rw) {
			case '\n' :
				if (prevSpace) 
					prevSpace = false;
				putchar('\n');
				nosym = true;
				break;
			case ' ' : if (nosym) {
					   if (!prevSpace) {
						   putchar(' ');
						   prevSpace = true;
					   }
				   } else
					   putchar(' ');
				   break;
			default: nosym = false;
				 putchar(rw);
		}
	return 0;
}
