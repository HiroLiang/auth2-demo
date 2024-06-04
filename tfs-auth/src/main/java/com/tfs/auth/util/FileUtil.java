package com.tfs.auth.util;

import com.tfs.auth.model.exception.BaseException;

import java.io.*;
import java.net.URL;

public class FileUtil {

    public static File getResourcesFile(String path) {
        URL url = FileUtil.class.getClassLoader().getResource(path);
        if (url == null)
            throw new BaseException("Load Resource failed");
        return new File(url.getFile());
    }

    public static String readAsString(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null){
                result.append(line);
            }
        } catch (FileNotFoundException e) {
            throw new BaseException("Can not find file");
        } catch (IOException e) {
            throw new BaseException("Fail to read file : " + file.getPath());
        }
        return result.toString();
    }

}
