package main
import "fmt"

func main() {
	for i:=1; i<=100; i++ {
		if (i % 3 == 0) && (i % 5 == 0) {
			fmt.Print("[",i," FizzBuzz","] ")
		} else if i % 3 == 0 {
			fmt.Print("[",i," Fizz","] ")
		} else if i % 5 == 0 {
			fmt.Print("[",i," Buzz","] ")
		}
	}
	fmt.Println()
	// Альтернативное решение
	for i:=1; i<=100; i++ {
		// switch умеет без параметра
		switch {
			case i % 3 == 0 && i % 5 == 0: fmt.Print("[", i, " FizzBuzz] ")
			case i % 3 == 0 : fmt.Print("[", i, " Fizz] ")
			case i % 5 == 0 : fmt.Print("[", i, " Buzz] ")
		}
	}
	fmt.Println()
}
