package com.testcore.utils;

/**
 * Created by Administrator on 2016/8/26.
 */
public class NdkJniUtils {
    static {
        System.loadLibrary("test");   //defaultConfig.ndk.moduleName
    }

    public native String getCLanguageString();
}