package main
import (
	"fmt"
	"time"
)

func main() {
	// Синхронизированный канал, стороны ждут получения-отправки
	c1 := make(chan string)
	// Буферизированный канал, пока не забит - стороны работают
	// асинхронно
	c2 := make(chan string, 5)

	go func() {
		for {
			c1 <- "from 1"
			time.Sleep(time.Second * 2)
		}
	}()
	go func() {
		for {
			c2 <- "from 2"
			time.Sleep(time.Second * 3)
		}
	}()
	go func() {
		for {
			// Примерно аналогичен switch
			// ждёт прихода сообщения в первый же канал
			// и исполняет соответсвующе
			// указывать принимающую переменную не
			// обязательно, достаточно наличия сообщения
			select {
				case msg1 := <- c1:
					fmt.Println(msg1)
				case msg2 := <- c2:
					fmt.Println(msg2)
					// After порождает поток и
					// функцию, которая пишет
					// в этот поток по указанному
					// времени значения меток времени
				case <- time.After(time.Second):
					fmt.Println("timeout")
				// Вызывается, когда все каналы заняты
				default:
					fmt.Println("nothing ready")
					time.Sleep(time.Second)
			}
		}
	}()
	
	var input string
	fmt.Scanln(&input)
}
