package Utils;

import java.util.*;

class FormatDemo3 {
	public static void main(String arga[]) {
		Formatter fmt = new Formatter();

		fmt.format("Копирование файла%nПеремещение на %d%% завершено", 88);
		System.out.println(fmt);
		fmt.close();
	}
}
