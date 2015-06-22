fn print_number(x: i32) {
    println!("x is: {}", x);
}

fn print_summ(x: i32, y: i32) {
    println!("Summ x + y = {}", x + y);
}

fn add_one(x: i32) -> i32 {
    x + 1
}

fn foo(x : i32) -> i32 {
    x
}

fn main() {
    print_number(5);
    print_summ(4,5);
    println!("{}", add_one(1));

    // Указатель на экземпляр функции
    // Указывается тип, аналогичный её определению
    let x: fn(i32) -> i32 = foo;
    //println!("{}", x);
}
