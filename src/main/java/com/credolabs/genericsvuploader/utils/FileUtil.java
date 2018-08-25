package com.credolabs.genericsvuploader.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
    private static final DateFormat dayDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private static final String filePathPrefix = "/tmp/";

    public static File[] listNonDirectoryFiles(File inputFile){
        return inputFile.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
    }

    public static File[] listFilesExcludingProcessedAndCurrentDate(File inputFile){
        return inputFile.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return !file.getName().toLowerCase().contains("processed") && !file.getName().contains(dayDateFormat.format(new Date()));
            }
        });
    }

    public static Integer getRecursiveFileCountExcludingDirectories(String dirPath) {
        int count = 0;
        File f = new File(dirPath);
        File[] files = f.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (!file.isDirectory())
                    count++;
                if (file.isDirectory()) {
                    count += getRecursiveFileCountExcludingDirectories(file.getAbsolutePath());
                }
            }
        }
        return count;
    }

    public static File multipartToFile(MultipartFile multipart)
            throws IllegalStateException, IOException
    {
        String fileName = filePathPrefix+multipart.getOriginalFilename();
        File convFile = new File(fileName);
        multipart.transferTo(convFile);
        return convFile;
    }
}
