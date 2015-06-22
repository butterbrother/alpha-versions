enum Message {
    Quit,
    ChangeColor(i32, i32, i32),
    Move { x: i32, y: i32 },
    Write(String),
}

enum BoardGameTurn {
    Move { squares: i32 },
    Pass,
}

fn main() {
    // Перечисления
    let x: Message = Message::Move{ x:3, y:4 };
    let y: BoardGameTurn = BoardGameTurn::Move { squares: 1};
}
