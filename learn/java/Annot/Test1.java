package Annot;

@interface MyAnno {
	String str();
	int val();
}


@MyAnno(str = "Тест", val = 100)

class Test1 {
	public static void main(String args[]) {
		System.out.println("nothing");
	}
}
