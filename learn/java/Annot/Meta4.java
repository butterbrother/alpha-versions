package Annot;

import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@interface MyAnno {
	String str() default "Тестирование";
	int val() default 9000;
}

class Meta4 {
	@MyAnno()
	public static void myMeth() {
		Meta4 ob = new Meta4();

		try {
			Class<?> c = ob.getClass();
			Method m = c.getMethod("myMeth");
			MyAnno anno = m.getAnnotation(MyAnno.class);

			System.out.println(anno.str() + " " + anno.val());
		} catch (NoSuchMethodException exc) {
			System.out.println("Метод не найден.");
		}
	}

	public static void main(String args[]) {
		myMeth();
	}
}
