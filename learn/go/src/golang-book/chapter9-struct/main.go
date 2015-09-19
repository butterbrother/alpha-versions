package main
import ("fmt"; "math")

// Задание типа - структура
type Circle struct {
    x float64
	y,r float64
}

// Интерфейс
func (c *Circle) area() float64 {
    return math.Pi * c.r * c.r
}

// Встраиваемый тип
type Person struct {
    Name string
}
func (p *Person) Talk() {
    fmt.Println("Hi, my name is", p.Name)
}
// Данная структура содержит в себе структуру Person
// ей доступен метод Talk() напрямую
type Android struct {
	Person	// анонимное поле
	Model string
}

func circleArea(circle Circle) float64 {
    return math.Pi * circle.r * circle.r
}

func main() {
    /*
    {
		// Стандартная инициализация
		// Локальная переменная
		var c Circle
		
		// Инициализация с указателем
		m := new(Circle)
	}
	*/
	
	{
	    // Инициализация с заданными полями
		c := Circle{x: 0, y: 0, r: 5}
		
		// Инициализация без имён полей
		m := Circle{0, 0, 5}
		
		// Доступ к полям - как обычно
		fmt.Println(c.x, c.y, c.r)
		c.x=5
		fmt.Println(circleArea(m))
		
		// Доступ к интерфейсу
		fmt.Println(c.area())
	}
	{
	    a:=new(Android)
		a.Person.Talk()
		a.Talk()
	}
}