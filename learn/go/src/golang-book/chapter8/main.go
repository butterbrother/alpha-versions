package main
import "fmt"

func one(xPtr *int) {
	*xPtr = 1
}

func swap(one, two *int) {
	// Писать напрямую в указатели нельзя,
	// прямой обмен адесами не выйдет
	*one, *two = *two, *one
}

func main() {
	n := 5
	// Создание указателя
	// var xPtr *int
	xPtr := &n
	// Можно создавать объект
	// new вернёт указатель на объект
	xPtr = new(int)
	one(xPtr)
	fmt.Println(*xPtr)

	one, two := 5, 6
	swap(&one, &two)
	fmt.Println(one, two)
}
