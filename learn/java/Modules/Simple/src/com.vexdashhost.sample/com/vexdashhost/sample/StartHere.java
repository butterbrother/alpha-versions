package com.vexdashhost.sample;

public class StartHere {
	public static void main(String ...args) {

		System.out.println("Welcome");
		Class<StartHere> cls = StartHere.class;
		// С Java 9+ у всех объектов появился метод, возвращающий
		// модуль, к которому объект принадлежит
		Module module = cls.getModule();
		String moduleName = module.getName();
		String className = cls.getName();
		System.out.format("Class %s contains into module %s\n",
				className, moduleName);
	}
}
