package main

import (
	"fmt"
	"time"
)

// Передающий в канал
// Здесь направление не указано, функция может
// посылать и принимать
func pinger(c chan string) {
	for i:= 0; ; i++ {
		c <- "ping"
	}
}

// Тоже передаёт в канал
// Указано направление, только передача
// Передача в канал - блокирующая процедура
func ponger(c chan <-string) {
	for i:= 0; ; i++ {
		c <- "pong"
	}
}

// Читающий из канала
// Только приём
func printer(c <- chan string) {
	for {
		msg := <- c
		fmt.Println(msg)
		time.Sleep(time.Second * 1)
	}
}

// main тоже поток, когда он завершается - завершаются все
// остальные потоки
func main() {
	// Создание канала
	var c chan string = make(chan string)
	
	go pinger(c)
	go ponger(c)
	go printer(c)

	var input string
	fmt.Scanln(&input)
}
