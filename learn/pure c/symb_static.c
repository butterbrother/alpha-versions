/* Напишите программу для вывода гистрограммы частот, с которыми
 * встречаются во входном потоке различные символы
 */

#include <stdio.h>

#define IN_BASE 1
#define NOT_IN_BASE 0

int main() {
	int symb[255];
	int index[255];
	int length = 0;
	int c, i, state;
	while ((c = getchar()) != EOF) {
		state = NOT_IN_BASE;
		for (i = 0; i < length; ++i)
			if (c == index[i]) {
				++symb[i];
				state = IN_BASE;
			}

		if (state == NOT_IN_BASE) {
			if (length <= 254) {
				symb[length] = 1;
				index[length] = c;
				++length;
			}
		}
	}

	int max = 0, j;
	for (i = 0; i < length; ++i)
		if (symb[i] > max)
			max = symb[i];

	if (max != 0)
		for (i = max; i > 0; --i) {
			for (j = 0; j < length; ++j) {
				if (symb[j] >= i)
					printf("   #");
				else
					printf("    ");
			}
			printf("\n");
		}

	for (i = 0; i < length; i++) {
		if (index[i] == ' ')
			printf("  SP");
		else if (index[i] == '\n')
			printf("  \\n");
		else if (index[i] == '\t')
			printf("  \\t");
		else 
			printf("%4c", index[i]);
	}
}
