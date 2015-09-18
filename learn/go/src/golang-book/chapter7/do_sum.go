package main
import "fmt"

func sum(nums []int64) (result int64) {
	result = 0
	for _, val := range nums {
		result += val
	}
	return
}

func main() {
	x := []int64{
		5, 6, 7, 8,
		9, 10, 11, 12,
	}
	fmt.Println(x, sum(x))
}

