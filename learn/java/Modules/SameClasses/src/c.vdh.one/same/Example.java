package same;

public class Example {
	public void action() {
		var className = Example.class.getName();
		var module = Example.class.getModule();
		var classLoader = module != null ? module.getClassLoader().toString() : "(null)";
		var moduleName = module != null ? module.getName() : "(null)";
		System.out.printf("Class \"%s\" into module \"%s\", classloader \"%s\"\n", className, classLoader, moduleName);
	}
}
