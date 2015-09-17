package main

import "fmt"

func main() {
	var faren float64
	fmt.Print("Enter farengheit value: ")
	fmt.Scanf("%f", &faren)

	celsium := (faren - 32) * 5/9

	fmt.Println("In celsium:", celsium)
}
