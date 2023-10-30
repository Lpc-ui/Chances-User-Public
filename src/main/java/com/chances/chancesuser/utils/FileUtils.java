package com.chances.chancesuser.utils;

import java.io.*;
import java.nio.file.*;

/**
 * 文件工具
 */
public class FileUtils {

    /**
     * 将输入流保存到指定的文件路径
     *
     * @param inputStream 输入流
     * @param targetFilePath 目标文件的绝对路径
     * @throws IOException 如果发生I/O错误
     */
    public static void saveInputStreamToFile(InputStream inputStream, String targetFilePath) throws IOException {
        // 确保InputStream不为null
        if (inputStream == null) {
            throw new IOException("输入流不能为空");
        }

        // 准备目标文件的Path对象
        Path targetPath = Paths.get(targetFilePath);

        // 确保目标文件的父目录存在
        Path parentDir = targetPath.getParent();
        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }

        // 使用Files.copy方法将输入流的数据复制到目标文件。如果文件已存在，它将被替换
        Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }
}
