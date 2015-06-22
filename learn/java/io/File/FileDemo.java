package io.File;

import java.io.File;

class FileDemo {
	public static void main(String args[]) {
		File f1 = new File("test.txt");
		StringBuilder p = new StringBuilder();

		p.append("Имя файла: ").append(f1.getName()).append('\n')
			.append("Путь: ").append(f1.getPath()).append('\n')
			.append("Абсолютный путь: ").append(f1.getAbsolutePath()).append('\n')
			.append("Родительский каталог: ").append(f1.getParent()).append('\n')
			.append(f1.exists() ? "Существует" : "Не существует").append('\n')
			.append(f1.canWrite() ? "Доступен для записи" : "Не доступен для записи").append('\n')
			.append(f1.canRead() ? "Доступен для чтения" : "Не доступен для чтения").append('\n')
			.append(f1.isDirectory() ? "Является каталогом" : "Не является каталогом").append('\n')
			.append(f1.isFile() ? "Является обычным файлом" : "Может быть именованным каналом").append('\n')
			.append(f1.isAbsolute() ? "Является абсолютным" : "Не является абсолютным").append('\n')
			.append("Время модификации: ").append(f1.lastModified()).append('\n')
			.append("Размер: ").append(f1.length()).append(" байт");

		System.out.println(p);
	}
}
