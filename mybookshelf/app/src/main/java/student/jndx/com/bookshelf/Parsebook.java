package student.jndx.com.bookshelf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by starry_mei on 2019/4/25.
 */

public class Parsebook {


    public static class ImageManager {
        public static final int GET_DATA_SUCCESS = 1;

        public static boolean IsImageExists(Context context, String fileName){
            String dirsrc = context.getFilesDir()+"";
            File dir = new File(dirsrc);
            if(!dir.exists()){
                dir.mkdirs();
            }

            String src = context.getFilesDir() + "/" + fileName + ".png";
            File outputFile = new File(src);

            return outputFile.exists();
        }

        public static boolean SaveImage(Context context, Bitmap bitmap, String saveName){
            if(bitmap == null)
                return false;

            String dirsrc = context.getFilesDir()+"";
            File dir = new File(dirsrc);
            if(!dir.exists()){
                dir.mkdirs();
            }

            String src = context.getFilesDir() + "/" + saveName + ".png";
            File outputFile = new File(src);

            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                Log.e("ImageManager", "SaveImage: 创建文件失败");
                return false;
            }

            FileOutputStream fileOutputStream=null;

            try {
                fileOutputStream = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("ImageManager", "SaveImage: 文件不存在");
                return false;
            } finally {
                if(fileOutputStream != null){
                    try {
                        fileOutputStream.close();
                        fileOutputStream=null;
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("ImageManager", "SaveImage: 关闭流失败");
                    }

                }
            }

        }

        public static Bitmap GetLocalBitmap(Context context, String imageName){
            FileInputStream fis=null;
            Bitmap bitmap=null;
            try {
                fis=new FileInputStream(context.getFilesDir() + "/" + imageName + ".png");
                bitmap = BitmapFactory.decodeStream(fis);
                return bitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("ImageManager", "GetLocalBitmap: 图片文件不存在");
                return null;
            } finally{
                if(fis != null){
                    try {
                        fis.close();
                        fis = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("ImageManager", "GetLocalBitmap: 关闭流失败");
                    }
                }
            }
        }


        public static Bitmap decodeSampledBitmapFromUrl(Context context, final String imageUrl) throws FileNotFoundException {
            // 先将inJustDecodeBounds设置为true来获取图片的长宽属性
            Bitmap bitmap = null;
            try {
                URL url = new URL(imageUrl);
                //获取连接
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //使用GET方法访问网络
                connection.setRequestMethod("GET");
                //超时时间为10秒
                connection.setConnectTimeout(10000);
                //获取返回码
                int code = connection.getResponseCode();
                if (code == 200) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    InputStream inputStream = connection.getInputStream();
                    byte[] bytes=toByteArray(inputStream);
                    //使用工厂把网络的输入流生产Bitmap
                    //bitmap=BitmapFactory.decodeStream(inputStream, null, options);
                    bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
                    options.inSampleSize = 4;
                    // 加载压缩版图片
                    options.inJustDecodeBounds = false;
                    // 根据具体情况选择具体的解码方法
                    //inputStream.reset();
                    bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
                    //利用Message把图片发给Handler
                    //handler.sendMessage(msg);
                    inputStream.close();
                    return bitmap;
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        public static byte[] toByteArray(InputStream inputStream) throws IOException {
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

            byte[] bytes=new byte[4096];
            int n=0;
            while (-1!=(n=inputStream.read(bytes))){
                outputStream.write(bytes,0,n);
            }
            return outputStream.toByteArray();

        }
    }
}

