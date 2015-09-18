package main
import "fmt"

func makeOddGenerator() func() int {
	odd := 1
	return func() (ret int) {
		ret = odd
		odd += 2
		return
	}
}

func main() {
	fn := makeOddGenerator()
	fmt.Println(fn())
	fmt.Println(fn())
	fmt.Println(fn())
}
