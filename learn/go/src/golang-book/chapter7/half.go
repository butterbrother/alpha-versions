package main
import "fmt"

func half(val uint) (uint, bool) {
	return val / 2, val % 2 == 0
}

func main() {
	fmt.Println(half(5))
	fmt.Println(half(6))
}
