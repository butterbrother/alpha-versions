package org.bicycles.yadbcmon.core;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Проверка возможности запуска самого себя в фоне
 * под различными ОС
 */
public class itselfBackground_test {
    public static void main(String args[]) {
        // С параметром - работаем как демон
        if (args.length > 0) {
            try (PrintWriter stepWriter = new PrintWriter(
                    new OutputStreamWriter(
                            Files.newOutputStream(
                                    Paths.get("./itselfBackground.log")
                            )
                    )
            )) {
                // Создаём файл, останавливаемся при его удалении
                Path pointer = Paths.get(".", "deleteMe.txt");
                Files.createFile(pointer);
                while (Files.exists(pointer)) {
                    stepWriter.append("waiting\n").flush();
                    Thread.sleep(150);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            // Без параметров - запускаем себя в фоне с каким-нибудь параметром
            try {
                itselfBackground selfrun = new itselfBackground("-param", "2>&1", ">> ./itselfBackground.log");
                for (String item : selfrun.getCmdLine()) System.out.println(item);
                selfrun.exec();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
