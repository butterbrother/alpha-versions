struct Foo<'a> {
    x: &'a i32,
}

fn main() {
    let y = &5;
    let f = Foo { x: y };

    println!("{}", f.x);

    let x: &'static str = "Эта строка хранится всё время";
    static FOO: i32 = 5; // Статичная, аки константа
    let x: &'static i32 = &FOO; // Ссылка на статичную константу
}
