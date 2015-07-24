/* Напишите программу для копирования входного потока в выходной с
 * заменой знаков табуляции на \t, символов возврата на \b, а обратных
 * косых черт - на \\
 */
#include <stdio.h>

int main() {
    int rw;
    while ((rw = getchar()) != EOF) {
        switch (rw) {
            case '\t': putchar('\\'); putchar('t');
                   break;
            case '\b': putchar('\\'); putchar('b');
                   break;
            case '\\': putchar('\\'); putchar('\\');
                   break;
            default: putchar(rw);
        }
    }
    return 0;
}
