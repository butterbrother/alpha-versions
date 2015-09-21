package math
import "testing"

// Полезно тестировать сразу несколько вариантов
type testpair struct {
	values []float64
	average float64
}

// Поэтому записываем параметры и предполагамемый ответ
var tests = []testpair {
	{ []float64{1,2}, 1.5 },
	{ []float64{1,1,1,1,1,1}, 1 },
	{ []float64{-1,1}, 0},
}

// Для каждой функции создаётся функция с префиксом Test
// аргумент - *testing.T
func TestAverage(t *testing.T) {
	for _, pair := range tests {
		v := Average(pair.values)
		if v != pair.average {
			t.Error(
				"For", pair.values,
				"expected", pair.average,
				"got", v,
			)
		}
	}
}
