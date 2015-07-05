package Annot;

import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@interface MyMarker { }

class Marker {
	@MyMarker
	public static void myMeth() {
		Marker ob = new Marker();

		try {
			Method m = ob.getClass().getMethod("myMeth");

			if (m.isAnnotationPresent(MyMarker.class))
				System.out.println("MyMarker присутствует.");
		} catch (NoSuchMethodException exc) {
			System.out.println("Метод не найден");
		}
	}

	public static void main(String args[]) {
		myMeth();
	}
}
