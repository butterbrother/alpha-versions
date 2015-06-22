fn main() {
    simple();
    with_else();
    with_else_if();
    with_returns();
    with_returns_one_line();
}

fn simple() {
    let x = 5;

    if x == 5 {
        println!("x is five!");
    }
}

fn with_else() {
    let x = 5;

    if x == 5 {
        println!("x is five!");
    } else {
        println!("nope");
    }
}

fn with_else_if() {
    let x = 5;

    if x == 5 {
        println!("x if five");
    } else if x == 6 {
        println!("x is six");
    } else {
        println!("i'm don't know");
    }
}

fn with_returns() {
    let x = 5;

    let y = if x == 5 {
        10
    } else {
        15
    };

    println!("y is {}, it equals x: {}", y, y == x);
}

fn with_returns_one_line() {
    let x = 5;

    let y = if x == 5 { 10 } else { 15 };

    println!("y is {}", y);
}
