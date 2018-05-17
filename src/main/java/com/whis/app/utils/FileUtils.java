package com.whis.app.utils;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    final String REDIS_FILE_PREVIEW_PDF_KEY = "converted-preview-pdf-file";
    final String REDIS_FILE_PREVIEW_IMGS_KEY = "converted-preview-imgs-file";//压缩包内图片文件集合


    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public String getSuffixFromFileName(String fileName) {
//        return fileName.substring(fileName.lastIndexOf("."));
        return FilenameUtils.getExtension(fileName);
    }
}
