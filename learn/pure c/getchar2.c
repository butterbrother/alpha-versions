#include <stdio.h>

int main() {
	char menuitem;
	int press;
	do {
		printf("1-exit\n>> ");
		press = getchar();
		menuitem = (press != EOF ? press : 0);
	} while (press != '1');

	return 0;
}
