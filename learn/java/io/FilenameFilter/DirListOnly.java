package io.FilenameFilter;

import java.io.*;

class DirListOnly {
	public static void main(String args[]) {
		String dirname = "io/FilenameFilter";
		File f1 = new File(dirname);
		FilenameFilter only = new OnlyExt("class");
		String s[] = f1.list(only);

		for (int i = 0; i < s.length; i++) {
			System.out.println(s[i]);
		}
	}
}
