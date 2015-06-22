fn main() {
    let x = 1;

    match x {
        1 => println!("one"),
        2 => println!("two"),
        3 => println!("three"),
        _ => println!("anothing"), // _ означает "что угодно"
    }

    match x {
        1 | 2 => println!("one or two"), // | или
        3 => println!("three"),
        _ => println!("anything"),
    }

    match x {
        1 ... 5 => println!("one through five"), // ... диапазон
        _ => println!("anything"),
    }

    let a = '1';
    match a {
        'a' ... 'j' => println!("earyl letter"),
        'k' ... 'z' => println!("late letter"),
        _ => println!("something else"),
    }

    let x = 1;

    // С присваиванием
    match x {
        e @ 1 ... 5 => println!("got a range element {}", e),
        _ => println!("anything"),
    }
    
    match x {
        // Присваивание происходит в каждом выражении
        e @ 1...5 | e @ 8...10 => println!("got a range element {}", e),
        _ => println!("anothing"),
    }

    enum OptionalInt {
        Value(i32),
        Missing,
    }

    let c = OptionalInt::Value(5);
    match c {
        // Игнорирование типов и значений при переборе перечислений
        OptionalInt::Value(..) => println!("Got an int!"),
        OptionalInt::Missing => println!("No such luck."),
    }

    match c {
        // Ограничения совпадения
        OptionalInt::Value(i) if i > 5 => println!("Got an int bigger than five!"),
        OptionalInt::Value(..) => println!("Got an int!"),
        OptionalInt::Missing => println!("Not such luck."),
    }

    match x {
        // создание ссылки для использования при совпадении
        ref r => println!("Got a reference to {}", r),
    }

    let mut d = 5;
    match d {
        ref mut mr => println!("Got a mutable reference to {}", mr),
    }
}
