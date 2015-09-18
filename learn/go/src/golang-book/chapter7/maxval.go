package main
import "fmt"

func t(valueIf bool, valueTrue float64, valueFalse float64) float64 {
	if valueIf {
		return valueTrue
	} else {
		return valueFalse
	}
}

func max(nums ...float64) (ret float64) {
	for _, val := range nums {
		ret = t(val > ret, val, ret)
	}
	return
}

func main() {
	fmt.Println(max(6.4, 7.8, 3.1, 2.5))
}
