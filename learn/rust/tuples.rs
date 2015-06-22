fn main() {
    let x = (1, "hello");
    let t: (i32, &str) = (1, "hello");

    let mut a = (1,2);
    let b = (2,3);
    a = b;  // Можно присваивать кортежу значение другого, при условии совпадения размерности и типов

    let (c, d, e) = (1, 2, 3);
    println!("c is {}", c);

    // Доступ к элементу кортежа
    let turple = (1, 2, 3);
    let f = turple.0;
    let g = turple.1;
    let h = turple.2;
    println!("f is {}, and second is {}", f, turple.1);
}
