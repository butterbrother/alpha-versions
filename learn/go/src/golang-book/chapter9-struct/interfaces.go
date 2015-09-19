package main
import ("fmt"; "math")

type Circle struct{
	radius float32
	x,y uint32
}
func (c *Circle) area() float32 {
    return math.Pi * c.radius * c.radius
}
// метод с параметром
func (c *Circle) delay(s float32) float32 {
    return c.area() - s
}
// Перегрузки методов нет

type Rectangle struct {
    w, l float32
	x, y uint32
}
func (c *Rectangle) area() float32 {
    return c.w * c.l
}

type Shape interface {
    area() float32
}

// Интерфейс используется как аргумент функций
func totalArea(shapes ...Shape) float32 {
    area := float32(0.0)
	for _, s := range shapes {
	    area += s.area()
	}
	return area
}

// Интерфейсы модут быть полями других структур
type MultiShape struct {
    shapes [] Shape
}
// MultiShape можно хранить как Shape, реализовав функцию метода
func (m *MultiShape) area() float32 {
    area := float32(0.0)
	for _, s := range m.shapes {
	    area += s.area()
	}
	return area
}

func main() {
    fmt.Println("Nope")
}