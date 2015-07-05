package Annot;

import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@interface MySingle {
	int value();
}

class Single {
	@MySingle(100)
	@Deprecated
	public static void myMeth() {
		Single ob = new Single();

		try {
			MySingle anno = ob.getClass().getMethod("myMeth").getAnnotation(MySingle.class);
			System.out.println(anno.value());
		} catch (NoSuchMethodException exc) {
			System.out.println("Метод не найден");
		}
	}

	public static void main(String args[]) {
		myMeth();
	}
}
