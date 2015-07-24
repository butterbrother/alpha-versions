// Деструктурирование
struct Point {
    x: i32,
    y: i32,
}

fn main() {
    let origin = Point { x: 0, y: 0 };
    
    // Полное
    match origin {
        // Справа можно указывать одноимённые ссылки
        Point {x: a, y: b} => println!("({},{})", a, b),
    }

    // Частичное
    match origin {
        // Можно указывать первым y
        Point { x: a, ..} => println!("x is {}", a),
    }
}
