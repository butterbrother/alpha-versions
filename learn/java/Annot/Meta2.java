package Annot;

import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@interface MyAnno {
	String str();
	int val();
}

class Meta2 {
	@MyAnno(str = "Два параметра", val = 19)
	public static void myMeth(String str, int i) {
		Meta ob = new Meta();
		try {
			Class<?> c = ob.getClass();

			Method m = c.getMethod("myMeth", String.class, int.class);

			MyAnno anno = m.getAnnotation(MyAnno.class);

			System.out.println(anno.str() + " " + anno.val());
		} catch (NoSuchMethodException exc) {
			System.out.println("Метод не найден.");
		}
	}

	public static void main(String args[]) {
		myMeth("тест", 10);
	}
}


