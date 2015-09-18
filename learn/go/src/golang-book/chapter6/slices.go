package main

import "fmt"

func AddOneToEathElement(slice []byte) {
	for i := range slice {
		slice[i]++
	}
}

func main() {
	var buffer [100]byte
	slice := buffer[10:20]
	for i := 0; i<len(slice); i++ {
		slice[i] = byte(i)
	}
	fmt.Println("before", slice)
	AddOneToEathElement(slice)
	fmt.Println("after", slice)
	fmt.Println("array", buffer)
}

