package Annot;

import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@interface MyAnno {
	String str();
	int val();
}

@Retention(RetentionPolicy.RUNTIME)
@interface What {
	String description();
}

@What(description = "Аннотация текстового класса")
@MyAnno(str = "Meta2", val = 99)
class Meta3 {

	@What(description = "Аннотация тестового метода")
	@MyAnno(str = "Testing", val = 100)
	public static void myMeth() {
		Meta3 ob = new Meta3();

		try {
			Annotation annos[] = ob.getClass().getAnnotations();

			System.out.println("Все аннотации для Meta3:");
			for (Annotation a : annos)
				System.out.println(a);

			System.out.println();

			Method m = ob.getClass().getMethod("myMeth");
			annos = m.getAnnotations();

			System.out.println("Все аннотации для myMeth:");
			for (Annotation a : annos)
				System.out.println(a);
		} catch (NoSuchMethodException exc) {
			System.out.println("Метод не найден.");
		}
	}

	public static void main(String args[]) {
		myMeth();
	}
}
