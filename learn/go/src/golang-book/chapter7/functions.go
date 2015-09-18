package main
import "fmt"

// Стандартная функция
func average(xs []float64) float64 {
	total := 0.0
	for _, v := range xs {
		total += v
	}

	return total/float64(len(xs))
}

// Эта функция возвращает 2 параметра
func two_params() (int, int) {
	return 0, 1
}

// Два возвращаемых именованных параметра
func named_two() (r int, ok bool) {
	r = 10
	ok = true
	return
}

func main() {
	xs := []float64{98,93,77,82,83}
	fmt.Println(average(xs))
	one, two := two_params()
	some, ok := named_two()
	fmt.Println(one, two, some, ok)

	// Мы встроили функцию в функцию
	add := func(x, y int) int {
		return x + y
	}
	fmt.Println(add(1,1))
}
