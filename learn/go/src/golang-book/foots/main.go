package main

import "fmt"

func main() {
	var meters float64
	fmt.Print("Enter meters: ")
	fmt.Scanf("%f", &meters)

	foots := meters * 0.3048
	fmt.Println(meters, "M. =", foots, "Ft.")
}
