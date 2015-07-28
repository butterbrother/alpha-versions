package org.butterbrother.odbased;

import java.security.SecureRandom;

/**
 * Генератор фраз аутенификации
 */
public class secretPhraseGenerator {

    /**
     * Генерирует произвольную строку
     *
     * @param length необходимая длина
     * @return произвольная строка
     */
    public static String generate(int length) {
        byte[] seq = SecureRandom.getSeed(length);
        char[] gen = new char[length];
        for (int i = 0; i < length; i++) {
            gen[i] = (char) ((byte) (seq[i] % 25 + 'A'));
        }

        return new String(gen);
    }
}
