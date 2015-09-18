package main
import "fmt"

func main() {
	// Пример работы с картой
	elements := make(map[string]string)
	elements["H"] = "Hydrogen"
	elements["He"] = "Helium"
	elements["Li"] = "Lithium"
	elements["Be"] = "Beryllium"
	elements["B"] = "Boron"
	elements["C"] = "Carbon"
	elements["N"] = "Nitrogen"
	elements["O"] = "Oxygen"
	elements["F"] = "Fluorine"
	elements["Ne"] = "Neon"

	// Доступ к существующему элементу
	fmt.Println(elements["Li"])

	// Их можно перечислять for-each, как массив
	for _, i := range elements {
		fmt.Print(i, " ")
	}
	fmt.Println()

	// Попытка получить несуществующий элемент
	{
		name, ok := elements["Un"]
		// ok - bool, будет false
		fmt.Println(name, ok)
	}
	// Аналогичная попытка, но с проверкой успеха
	{
		if name, ok := elements["Un"]; ok {
			fmt.Println(name, ok)
		} else {
			fmt.Println("Element not found")
		}
	}
	// Объявление карт с инициализацией и содержимым
	// Всё как у массивов
	elm := map[string]string {
		"H": "Hydrogen",
		"He": "Helium",
		"Li": "Lithium",
	}
	// Карты можно вкладывать друг в друга, создавая карты
	// из карт
	elem := map[string]map[string]string {
		"H": map[string]string {
			"name": "Hydrogen",
			"state": "gas",
		},
		"Li": map[string]string {
			"name": "Lithium",
			"state": "solid",
		},
	}
	if el, ok := elem["Li"]; ok {
		fmt.Println(el["name"], el["state"])
	} else {
		fmt.Println(elm)
	}
}
