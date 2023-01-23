package com.raken.sendgridwrapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;

public class WriteLogUtil {

    public static void writeStringToFile(String content) {
        try {
            String srcPath = WriteLogUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String basePath = srcPath.substring(0, srcPath.lastIndexOf(File.separator + "build" + File.separator));
            String logPath = basePath + File.separator + "logs";
            Files.createDirectories(Paths.get(logPath));
            String logFileName = File.separator + Instant.now().getEpochSecond() +".txt";

            File file = new File(logPath + logFileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
