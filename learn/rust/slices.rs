fn main() {
    let a = [0,1,2,3,4];
    let middle = &a[1..4]; // слайс содержит элементы от 1 до 4, не включая 4
    let complete = &a[..]; // Слайс содержит все элементы
    println!("Array a contains {} elements, but slice middle only {}", a.len(), middle.len());
}
