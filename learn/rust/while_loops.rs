fn main() {
    let mut x = 5;
    let mut done = false;

    while !done {
        x += x - 3;

        print!("{} ", x);

        if x % 5 == 0 {
            done = true;
        }
    }
    println!("");

    x = 5;
    loop {
        x += x - 3;

        print!("{} ", x);

        if x % 5 == 0 { break; }
    }
    println!("");

    for y in 0..10 {
        if y % 2 == 0 { continue; }
        print!("{} ", y);
    }
    println!("");
}
