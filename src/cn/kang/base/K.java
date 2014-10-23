package cn.kang.base;

import java.io.File;

/**
 * 常量类,保存应用中各种常量
 */
public final class K {
    public static final class config {
        //生产模式,在打包发布时需要修改为false
        public static final boolean DEVELOPER_MODE = true;
        //SD卡应用根目录
        public static final String K_SD_BASE_DIR = "kang";
        //SD卡缓存目录
        public static final String CACHE_DATA_DIR = K_SD_BASE_DIR + File.separator + "caches";
        //日志目录
        public static final String LOG_DATA_DIR = K_SD_BASE_DIR + File.separator + "logs";
        //下载目录
        public static final String DOWNLOAD_DIR = K_SD_BASE_DIR + File.separator + "downloads";
        //dex目录
        public static final String SD_DEX_DIR = K_SD_BASE_DIR + File.separator + "dex";
    }
}
