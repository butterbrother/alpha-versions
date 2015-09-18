package main
import "fmt"

func fib(n int) int {
	switch {
		case n == 0: return 0
		case n == 1: return 1
		default: return fib(n-1) + fib(n-2)
	}
}

func main() {
	num := int(0)
	fmt.Print("Enter number: ")
	fmt.Scanf("%d", &num)
	for i := 0; i <= num; i++ {
		fmt.Print(fib(i), " ")
	}
	fmt.Println()
}
