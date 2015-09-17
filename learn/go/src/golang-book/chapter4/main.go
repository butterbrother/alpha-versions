package main

import "fmt"

func main() {
	var x string = "Hello World" // Явное указание типа
	var y = "Some" // Автоматическое
	var b string // Без присваивания, указывается тип
	b = "test"
	a := 5 // Инициализация и автоопределение типа с присваиванием
	fmt.Println(x,y,b,a) // Форматтер почему-то сам пихает пробелы

	const c string = "Constant" // Некая константа
	fmt.Println(c)

	// Определение нескольких переменных подряд
	var (
		d int8 = 5
		e uint = 10
		f = 15
	)
	fmt.Println(d,e,f)

	fmt.Print("Enter number: ")
	var input float64
	fmt.Scanf("%f", &input)

	output := input * 2
	fmt.Println(output)
}
