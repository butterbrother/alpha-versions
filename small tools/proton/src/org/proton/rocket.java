package org.proton;

import org.apache.tools.ant.DirectoryScanner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Formatter;

/**
 * �������� ����������� �����
 */
public class rocket {
    private settings set;   // ���������

    /**
     * �������������
     *
     * @param set   ���������
     */
    public rocket(settings set) {
        this.set = set;
    }

    public void execute() {
        createLockFile();
        while (checkLockFile()) {
            showDateTime("Started at");
            checkIndexPath();
            upload();
            showDateTime("Ended at");
            try {
                Thread.sleep(set.getPause());
            } catch (InterruptedException err) {
                System.exit(0);
            }
        }
        System.out.println("Process stopped");
    }

    /**
     * ������ lock-����
     */
    private void createLockFile() {
        if (checkLockFile()) {
            System.err.println("Another process already run");
            System.exit(1);
        }
        try {
            Files.createFile(Paths.get("proton.lock"));
            System.out.println("Created lock file proton.lock");
            System.out.println("Delete it to graceful stop process");
        } catch (IOException err) {
            System.err.println("Unable to create lock file!");
        }
    }

    /**
     * ��������� ������� lock-�����
     *
     * @return  �������
     */
    private boolean checkLockFile() {
        return Files.exists(Paths.get("proton.lock"));
    }

    /**
     * ���������� ����-����� � ���������
     *
     * @param prefix    �������
     */
    private void showDateTime(String prefix) {
        System.out.format("%s %TY-%<Tm-%<Td %<TH:%<TM:%<TS\n", prefix, Calendar.getInstance());
    }

    /**
     * �������� ������� �������� �������
     * ���������� ������, ���� �� ����
     */
    private void checkIndexPath() {
        // ������ ������� �������, ���� ��� ���
        try {
            if (Files.notExists(set.getIndexPath()))
                Files.createDirectories(set.getIndexPath());
        } catch (IOException err) {
            System.err.println("Unable to create index path " + set.getIndex());
            System.exit(1);
        }

        // ��������� �������-��������, ���������� �������, ���� �� ���
        DirectoryScanner indexator = new DirectoryScanner();
        indexator.setBasedir(set.getIndex());
        indexator.scan();

        if (indexator.getIncludedFilesCount() == 0) {
            System.out.println("Index is empty, build");
            // ������ ����������� �������
            // ���������, ���� ��� �����
            DirectoryScanner sourceIndex = new DirectoryScanner();
            sourceIndex.setBasedir(set.getSource());
            System.out.println("Scan files list");
            sourceIndex.scan();

            // ���� �������� ������ ������
            String[] filesList = sourceIndex.getIncludedFiles();
            if (filesList.length > 0) {
                //��������� � �������� �������
                System.out.println("Sorting files list");
                Arrays.sort(filesList, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        // ����������� ������� ����������
                        return o1.compareTo(o2) * -1;
                    }
                });

                // ���������, �������� ������
                System.out.println("Write files list info index files");
                int size = 0;
                int prefix = 0;
                BufferedWriter outFile = null;
                try {
                    outFile = Files.newBufferedWriter(Paths.get(set.getIndex(), new Formatter().format("upl_%05d", prefix).toString()), Charset.defaultCharset());
                } catch (IOException err) {
                    System.err.println("Unable to create index file");
                    System.exit(1);
                }
                for (String file : filesList) {
                    // ���� ����� ����� ��������� �������� �������� - ����������
                    if (size > set.getUploadCount()) {
                        size = 0;
                        prefix++;
                        try {
                            outFile.close();
                            outFile = Files.newBufferedWriter(Paths.get(set.getIndex(), new Formatter().format("upl_%05d", prefix).toString()), Charset.defaultCharset());
                        } catch (IOException err) {
                            System.err.println("Unable to create index file");
                            System.exit(1);
                        }
                    }
                    // ����� ��� ����� � ������
                    try {
                        outFile.append(file).append("\n");
                    } catch (IOException err) {
                        System.err.println("Unable to write to index file");
                        System.exit(1);
                    }
                    // ��������� ������, +1
                    size++;
                }
                // ���������
                try {
                    outFile.close();
                } catch (IOException err) {
                    System.err.println("Unable to close index file");
                    System.exit(1);
                }
                System.out.println("Done");
            } else {
                System.out.println("No files in target dir");
            }
        } else {
            System.out.println("Index available");
        }
    }

    /**
     * ��������� ���������� � �������-�������
     */
    private void upload() {
        // ��������� ������� � ���������
        DirectoryScanner indexator = new DirectoryScanner();
        indexator.setBasedir(set.getIndex());
        indexator.scan();
        String[] indexList = indexator.getIncludedFiles();

        if (indexator.getIncludedFilesCount() > 0) {
            // ������� ����� ������
            DirectoryScanner count = new DirectoryScanner();
            count.setBasedir(set.getTarget());
            count.scan();

            if (count.getIncludedFilesCount() < set.getMinCount()) {
                System.out.println("Files into target less that " + set.getMinCount() + ": " + count.getIncludedFilesCount());
                // ���� ������ ����
                System.out.println("Use index file " + indexList[0]);
                try (BufferedReader indexFile = Files.newBufferedReader(Paths.get(set.getIndex(), indexList[0]), Charset.defaultCharset())) {
                    String fName;
                    // ���������� ��� �����, ��������� � �������
                    while ((fName = indexFile.readLine()) != null) {
                        if (!fName.isEmpty()) {
                            try {
                                Files.move(
                                        Paths.get(set.getSource(), fName),
                                        Paths.get(set.getTarget(), Paths.get(set.getSource(), fName).getFileName().toString()));
                            } catch (IOException moveErr) {
                                System.err.println("Cant move file " + fName);
                            }
                        }
                    }
                } catch (IOException err) {
                    System.err.println("Unable to open or read index file" + indexList[0]);
                    err.printStackTrace();
                    System.exit(1);
                }
                // ������� ���� ����
                try {
                    Files.delete(Paths.get(set.getIndex(), indexList[0]));
                } catch (IOException err) {
                    System.err.println("Unable to delete index file" + indexList[0]);
                }
                System.out.println("Done");
            } else {
                System.out.println("Files into target great that " + set.getMinCount());
            }
        }
    }
}
