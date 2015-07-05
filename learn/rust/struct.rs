struct Point {
    x: i32,
    y: i32,
}

struct Point3d {
    x: i32,
    y: i32,
    z: i32,
}

fn main() {
    let origin = Point { x: 0, y: 0 };
    println!("The origin is at ({}, {})", origin.x, origin.y);

    let mut point = Point { x: 0, y: 0 };
    point.x = 5;
    println!("The point is at ({}, {})", point.x, point.y);

    let mut point2 = Point3d { x: 0, y: 0, z: 0};
    // Заполнение значений из другой структуры, либо своей же
    point2 = Point3d { y: 1, .. point2 };
    let origin2 = Point3d { x: 0, y : 0, z : 0 };
    let npoint = Point3d { z: 1, x: 2, .. origin2 };

    // Кортежные структуры
    struct Color(i32, i32, i32);
    struct Pointer(i32, i32, i32);
    // Они не эквивалентны, хотя хранят однотипные значения

    let black = Color(0,0,0);
    let position = Pointer(0,0,0);

    // Использование кортежных структур для создания новых типов
    struct Inches(i32);
    let length = Inches(10);
    let Inches(integer_length) = length;
    println!("length is {} inches", integer_length);
}
