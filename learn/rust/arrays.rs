fn main() {
    let a = [1,2,3];
    let b = [0; 20]; // Массив из 20 элементов, инициализирован нулями
    let c : [i8; 20] = [0; 20]; // С указанием типа
    println!("Array a has {} elements", a.len());
    
    let strings = [ "Graydon", "Brian", "Niko" ];
    let some : [&str; 2] = [ "some", "some" ];
    let one : [&str; 3] = [ ""; 3 ];
    println!("The second name is {}", strings[1]);

    for i in &strings {
        print!("{} ", i);
    }
}
