package org.proton;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * ������������� � �������� ��������
 */
public class settings {
    private String source;  // ������� - ��������
    private String target;  // ������� - �������
    // ������� � ��������
    private String index;   // ���� �� �������
    private Path indexPath; // � ���� Path

    private int minCount;   // ����������� ����� ������ � ��������-��������
    private int uploadCount;// ����� ������������� ������
    private int pause;      // ����� ����� ����������

    /**
     * �������� �������������.
     * ��� ��������� �������� ���������� �� �������� � �������� ����� ����������� �����
     *
     * @param source      ��������
     * @param target      �������
     * @param index       �������
     * @param minCount    ����������� ����� ������
     * @param uploadCount ������������ ����� ������
     */
    private settings(String source, String target, String index, int minCount, int uploadCount, int pause) {
        this.source = source;
        this.target = target;

        this.index = index;
        this.indexPath = Paths.get(index);

        this.minCount = minCount;
        this.uploadCount = uploadCount;
        this.pause = pause;
    }

    /**
     * ������ � �������� ��������
     *
     * @return ������������ ���������
     */
    public static settings readSettings() {
        // ��������� ���������
        Properties set = new Properties();
        try (InputStream fileProp = Files.newInputStream(Paths.get("proton.properties"))) {
            set.load(fileProp);
        } catch (IOException err) {
            System.err.println("Unable to load config file: " + err);
            System.exit(1);
        }

        // �������� � ��������� ���������
        String source = set.getProperty("source");
        String target = set.getProperty("target");
        String index = set.getProperty("index");
        if (source == null || source.isEmpty()) {
            System.err.println("source not set");
            System.exit(1);
        }
        if (target == null || target.isEmpty()) {
            System.err.println("target not set");
            System.exit(1);
        }
        if (index == null || index.isEmpty()) {
            System.err.println("index not set");
            System.exit(1);
        }
        int minCount;
        int uploadCount;
        int pause;
        try {
            minCount = Integer.parseInt(set.getProperty("min", "10000"));
        } catch (NumberFormatException err) {
            System.err.println("Unable to parse min count, set default, 10000");
            minCount = 10000;
        }
        try {
            uploadCount = Integer.parseInt(set.getProperty("upload", "10000"));
        } catch (NumberFormatException err) {
            System.err.println("Unable to parse upload count, set default, 10000");
            uploadCount = 10000;
        }
        try {
            pause = Integer.parseInt(set.getProperty("pause", "30")) * 1000;
        } catch (NumberFormatException err) {
            System.err.println("Unable to parse pause, set default, 30 sec");
            pause = 30000;
        }

        return new settings(source, target, index, minCount, uploadCount, pause);
    }

    // ��������� ��������
    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getIndex() {
        return index;
    }

    public Path getIndexPath() {
        return indexPath;
    }

    public int getMinCount() {
        return minCount;
    }

    public int getUploadCount() {
        return uploadCount;
    }

    public int getPause() {
        return pause;
    }
}
