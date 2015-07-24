/*
 * Напишите программу для вывода гистрограммы длин слов во входном потоке.
 * Построить гистрограмму с горизонтальными рядами довольно легко, а вот с
 * вертиальными столбцами труднее
 */
#include <stdio.h>
#define SIZE 15 /* Максимальная длина слов */
#define IN_WORD 1
#define OUT_WORD 0

int main() {
	int words[SIZE];
	int c, i, max, state;

	for (i = 0; i < SIZE; ++i)
		words[i] = 0;

	i = 0;
	state = OUT_WORD;
	while ((c = getchar()) != EOF)
		if (c == ' ' || c == '\n' || c == '\t') {
			if (state == IN_WORD) {
				state = OUT_WORD;
				if (i >= SIZE)
					i = SIZE - 1;
				++words[i-1];
				i = 0;
			}
		} else {
			if (state == OUT_WORD) 
				state = IN_WORD;
			++i;
		}

	max = words[0];
	for (i = 0; i < SIZE; ++i)
		if (words[i] > max)
			max = words[i];

	int j;
	for (i = max; i > 0; --i) {
		for (j = 1; j <= SIZE; ++j) {
			if (words[j-1] >= i)
				printf("  #");
			else
				printf("   ");
		}
		printf("\n");
	}
	for (i = 1; i < SIZE; ++i)
	       printf("%3d", i);

	printf("\n");
}
