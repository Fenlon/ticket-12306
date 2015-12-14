package com.fenlonsky.ticket.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by fenlon on 15-12-14.
 */
public class FileIOUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileIOUtil.class);

    public static String readFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            logger.error("文件不存在,文件为{}", path);
            return null;

        }
        StringBuffer json = new StringBuffer("");
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.error("file not exist,file : {}", file.getAbsolutePath());
        }
        BufferedInputStream bfIn = new BufferedInputStream(in);
        byte[] buffer = new byte[1024];
        int read = 0;
        String chunk = null;
        try {
            while ((read = bfIn.read(buffer)) != -1) {
                chunk = new String(buffer, 0, read);
                json.append(chunk);
            }

            //关闭IO
            bfIn.close();
            in.close();
            return json.toString();
        } catch (IOException e) {
            logger.error("读取文件失败", e.getMessage());
        } finally {
            try {
                if (bfIn != null) {
                    bfIn.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("关闭IO失败", e.getMessage());
            }

        }
        return null;
    }

    public static void flushAndWriteFile(String path, String content) {
        File file = new File(path);
        FileWriter fileWriter = null;
        try {
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            logger.error("写文件失败", e.getMessage());
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    logger.error("关闭IO失败", e.getMessage());
                }
            }
        }
    }
}

