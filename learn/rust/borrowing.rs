fn main() {
    fn foo(v1: &Vec<i32>, v2: &Vec<i32>) -> i32 {
        42
    }

    let v1 = vec![1, 2, 3];
    let v2 = vec![1, 2, 3];

    let answer = foo(&v1, &v2);

    let mut x = 5;
    {
        let y = &mut x;
        *y += 1;
    }
    println!("{}", x);
}
