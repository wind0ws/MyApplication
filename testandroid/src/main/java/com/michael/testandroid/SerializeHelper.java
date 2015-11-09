package com.michael.testandroid;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化帮助类
 */
public class SerializeHelper {

     /*
    public SerializeHelper(Context context) {
        this.context = context;
    }
    private Context context;


    *//**
     * 从App的data目录下的File文件夹下取出文件反序列化出对象
     * 需强制转换成你需要的对象
     * @param fileName 序列化文件的名称，如gear.config
     * @return 返回反序列化出的对象
     * @throws IOException 当文件不存在或者反序列化出错时
     * @throws ClassNotFoundException 反序列化readObject()可能抛出这个错误
     *//*
    public  Object deserializeFromAppFile(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = context.openFileInput(fileName);//文件不存在时会引发FileNotFoundException
//            int length = fileInputStream.available();
//            byte[] buffer = new byte[length];
//            fileInputStream.read(buffer);
        *//*ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object readObject = objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();*//*
        return deserializeFromStream(fileInputStream);
    }

    *//**
     * 序列化对象到App目录下的file文件夹中
     * @param object 要序列化的对象，比如一个类的实例
     * @param fileName 文件名称，比如gear.config
     * @throws IOException 序列化出错时可能抛出这个错误
     *//*
    public  void serializeObjectToAppFile(Object object,String fileName) throws IOException {
        FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        serializeObjectToStream(object,outputStream);
        *//*ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        outputStream.close();*//*
    }
   */


    /**
     * 从外部存储中反序列化出对象
     * @param fullName 外部文件全路径
     * @return 返回序列化出的对象
     * @throws Exception 可能外部存储不可读写
     */
    public static Object deserializeFromFile(String fullName) throws Exception {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(fullName);
            if(file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                return deserializeFromStream(fileInputStream);
            }else {
                throw new FileNotFoundException();
            }
        }else {
            throw new Exception("外部存储不可读写。");
        }
    }

    /**
     * 从文件从反序列化出对象
     * @param file 文件
     * @return 返回Object对象
     * @throws IOException 读写异常
     * @throws ClassNotFoundException readObject()可能抛出的异常
     */
    public static Object deserializeFromFile(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        return deserializeFromStream(fileInputStream);
    }

    /**
     * 序列化对象到外部存储的文件中
     * @param object 对象
     * @param fullName 外部文件全路径
     * @throws Exception 外部存储可能不可读写
     */
    public static void serializeObjectToFile(Object object,String fullName) throws Exception {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File file=new File(fullName);
           /* File dirs = file.getParentFile();
            if(!dirs.exists()) {
                dirs.mkdirs();
            }*/
            if(!file.exists()){
                throw new FileNotFoundException(fullName+"不存在，请检查。");
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            serializeObjectToStream(object,fileOutputStream);
        }else {
            throw new Exception("外部存储不可读写");
        }
    }


    /**
     * 序列化对象到文件中
     * @param object 对象
     * @param file 文件
     * @throws IOException 读写异常
     */
    public static void serializeObjectToFile(Object object,File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        serializeObjectToStream(object,fileOutputStream);
    }

    /**
     * 从文件输入流中反序列出对象
     * @param fileInputStream 文件输入流
     * @return 返回出对象
     * @throws IOException 读写错误
     * @throws ClassNotFoundException readObject()可能出错
     */
    public static Object deserializeFromStream(FileInputStream fileInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object readObject = objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return readObject;
    }

    /**
     * 从文件输出流中序列化对象
     * @param object 对象
     * @param fileOutputStream 文件输出流
     * @throws IOException
     */
    public static void serializeObjectToStream(Object object,FileOutputStream fileOutputStream) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        fileOutputStream.close();
    }



}
