package com.michael.testandroid;

import android.content.res.AssetManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.IllegalFormatException;

/**
 * assets资源访问辅助类
 * AssetManager 可以通过 context.getAssets();来获取。
 * 在不用的时候调用AssetManager的close()方法
 * Created by Jiang on 2015/6/6.
 */
public class AssetsUtils {

    /**
     * 拷贝Assets中的文件夹到指定目录
     * 当文件存在时，默认覆盖重写
     * @param assetManager Asset管理类
     * @param source assets中的文件夹，例如：folder 或 folder/subFolder
     * @param destination 目的文件夹，例如：Environment.getExternalStorageDirectory()+"/folder/"
     * @throws IOException 异常
     */
    public static void copyAssetsFolders(AssetManager assetManager,String source,String destination) throws IOException {
       copyAssetsFolders(assetManager, source, destination, true);
    }

    /**
     * 拷贝Assets中的文件夹到指定目录
     * @param assetManager Asset管理类
     * @param source assets中的文件夹，例如：folder 或 folder/subFolder
     * @param destination 目的文件夹，例如：Environment.getExternalStorageDirectory()+"/folder/"
     * @param isOverride 当目的文件存在时是否需要覆盖重写
     * @throws IOException 异常
     */
    public static void copyAssetsFolders(AssetManager assetManager, String source, String destination, boolean isOverride) throws IOException {
        //AssetManager assets = context.getAssets();

            String[] fileLists = assetManager.list(source);
            File dest = new File(destination+"/"+source);
            if (fileLists.length > 0) { //是目录
                if (!dest.exists()&&!dest.mkdirs()) {
                    throw new FileNotFoundException("copyAssets:在Sd卡创建文件夹失败,"+dest.getPath());
                }
                for (String fileName : fileLists) {
                    String nextSource=source.isEmpty()?fileName:source + "/" + fileName;
                    copyAssetsFolders(assetManager, nextSource, destination, isOverride);
                }
            } else { //是文件
                if(dest.exists()){
                    //文件存在
                    if(!isOverride){
                        return;//不需要重写
                    }
                }
                InputStream inputStream = assetManager.open(source);
                FileOutputStream outputStream = new FileOutputStream(dest);
                copy(inputStream,outputStream);
            }

        //assets.close();
    }

    /**
     * copy Assets中特定的文件，当文件不存在时会抛出IOException
     * @param assetManager Assets管理类
     * @param sourceFileName 源文件名称,如：folder/fileName 或 fileName
     * @param destination 保存到哪里，可以是目的文件夹或目的文件,如：Environment.getExternalStorageDirectory()+"/folder/" 或 Environment.getExternalStorageDirectory()+"/folder/fileName"
     *                    如果给的是全路径（包括文件名称，则直接复制到这里）如果给的是文件夹，则在这个文件夹创建个与源文件相同的文件名称
     * @param isOverride 如果文件存在是否需要覆盖重写
     * @throws IOException 读写异常
     */
    public static void copyAssetsFiles(AssetManager assetManager,String sourceFileName,String destination,boolean isOverride) throws IOException {

        int lastSeparator = sourceFileName.lastIndexOf("/");
        if(lastSeparator==0) {
            throw new RuntimeException("sourceFile给的格式不正确，不能以“/”为起始路径");
        }
        if(lastSeparator==sourceFileName.length()-1){
            throw new RuntimeException("sourceFile给的格式不正确，不能以“/”为结尾路径");
        }
        if(destination.substring(destination.length()-1).equals("/")) { //说明给的目的路径是文件夹
            String sourceName = sourceFileName.substring(lastSeparator + 1);
            destination += sourceName;
        }
        File destFile = new File(destination);
        if(destFile.exists()&&(!isOverride)){
            return;//不需要覆盖重写
        }
        new File(destination.substring(0, destination.lastIndexOf("/"))).mkdirs();//把目的路径中不存在的文件夹创建出来
        InputStream inputStream = assetManager.open(sourceFileName);
        FileOutputStream fileOutputStream = new FileOutputStream(destFile);
        copy(inputStream,fileOutputStream);
    }

    /**
     * copy Assets中特定的文件，当文件不存在时会抛出IOException
     * @param assetManager Assets管理类
     * @param sourceFileName 源文件名称,如：folder/fileName 或 fileName
     * @param destination 保存到哪里，可以是目的文件夹或目的文件,如：Environment.getExternalStorageDirectory()+"/folder/" 或 Environment.getExternalStorageDirectory()+"/folder/fileName"
     *                    如果给的是全路径（包括文件名称，则直接复制到这里）如果给的是文件夹，则在这个文件夹创建个与源文件相同的文件名称
     * @throws IOException 读写异常
     */
    public static void copyAssetsFiles(AssetManager assetManager,String sourceFileName,String destination) throws IOException {
        copyAssetsFiles(assetManager,sourceFileName,destination,true);
    }

    /**
     * 文件拷贝
     * @param inputStream 输入流
     * @param outputStream 输出流
     * @throws IOException 异常
     */
    private static void copy(InputStream inputStream,OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int byteCount;
        while ((byteCount=inputStream.read(buffer))!=-1){
            outputStream.write(buffer,0,byteCount);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }


}
