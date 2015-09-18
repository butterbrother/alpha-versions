package main
import "fmt"

func second() (result bool){
	defer func() {
		if r:= recover(); r != nil {
			result = false
		}
	}()
	result = true
	panic("SOME")
}

func main() {
	// recover-обработчик должен вызываться defer-функцией
	// Иначе выполнение текущей функции и приложения прервётся
	fmt.Println(second())

	defer func() {
		str := recover()
		fmt.Println(str)
	}()
	panic("PANIC")
}
