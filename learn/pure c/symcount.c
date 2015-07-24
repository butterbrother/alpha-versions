#include <stdio.h>

int main() {
	long spaces=0, tabs=0, newline=0;
	int rsym;

	while ((rsym = getchar()) != EOF)
		switch (rsym) {
			case ' ' : ++spaces;
				   break;
			case '\t' : ++tabs;
				    break;
			case '\n' : ++newline;
		}

	printf("Spaces: %ld\nTabs: %ld\nNewlines: %ld\n", spaces, tabs, newline);

	return 0;
}
