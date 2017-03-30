package com.zhongbang.sxb.zxing;

import java.io.WriteAbortedException;
import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
/**
 * @author Ryan Tang
 *
 */
public final class EncodingHandler {
	private static final int BLACK = 0xff000000;
	private static final int green = 0xff00cc00;
	
	public static Bitmap createQRCode(String str,int widthAndHeight,Bitmap logo) throws WriteAbortedException, WriterException {
        //配置参数
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();  
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //容错级别,注意：必须设置此容错级别，否则如在二维码中间嵌入小图片传会导致扫描失败!!
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 1);
        // 图像数据转换，使用了矩阵转换
		 BitMatrix bitMatrix = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight, hints);
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		int[] pixels = new int[width * height];
		// 下面这里按照二维码的算法，逐个生成二维码的图片，
        // 两个for循环是图片横列扫描的结果
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (bitMatrix.get(x, y)) {
					pixels[y * width + x] = BLACK;
				}else{   
                    pixels[y * width + x] = 0xffffffff;   
                } 
			}
		}
		// 生成二维码图片的格式，使用ARGB_8888
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		bitmap=addLogo(bitmap, logo);
		return bitmap;
	}
	/**
     * 在二维码中间添加Logo图案
     */
    public static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return src;
        }
        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }
        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }
	/*
	public static Bitmap Create2DCode(String str) throws WriterException {       
        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败       
        BitMatrix matrix = new MultiFormatWriter().encode(str,BarcodeFormat.QR_CODE, 400, 400);       
        int width = matrix.getWidth();       
        int height = matrix.getHeight();       
        //二维矩阵转为一维像素数组,也就是一直横着排了       
        int[] pixels = new int[width * height];       
        for (int y = 0; y < height; y++) {       
            for (int x = 0; x < width; x++) {       
                if(matrix.get(x, y)){       
                    pixels[y * width + x] = 0xff000000;       
                }       
                       
            }       
        }       
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);       
        //通过像素数组生成bitmap,具体参考api       
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);       
        return bitmap;       
    }   
    */
}
