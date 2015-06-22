fn main() {
    let mut x: [usize; 10] = [0; 10];
    for i in 0..x.len() {
        x[i] = i;
        print!("{}\t", &x[i]);
    }
    println!("");
}
