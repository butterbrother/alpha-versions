package main

import "fmt"

func first() {
	fmt.Println("1st")
}

func second() {
	fmt.Println("2nd")
}

func main() {
	// Функция, вызванная defer, выполнится после заверщения main
	// Даже если при выполнении main будет ошибка
	defer second()
	first()
}
