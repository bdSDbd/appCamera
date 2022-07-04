package com.example.appcamera;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;
import java.util.List;



public class DIGI_SSOCR_func {

    public static String qrcodeText = "4/611006003/3";

    public static int channel_color_code() {
        String[] qrArray;
        qrArray = qrcodeText.split("/");
        int code = Integer.parseInt(qrArray[13]); //通道顏色
        return code;
    }

    private static List<Scalar> SS_color(int i) {
        Scalar mincolor;
        Scalar maxcolor;

        if (i == 1) { //紅色
            mincolor = new Scalar(0, 0, 220);
            maxcolor = new Scalar(155, 255, 255);
        } else if (i == 2) { //綠色
            mincolor = new Scalar(40, 58, 100);
            maxcolor = new Scalar(95, 255, 255);
        } else if (i == 3) { //藍色
            mincolor = new Scalar(15, 0, 200);
            maxcolor = new Scalar(160, 255, 255);
        } else if (i == 4) { //黃色
            mincolor = new Scalar(0, 0, 10);
            maxcolor = new Scalar(129, 255, 255);
        } else {
            mincolor = new Scalar(0, 0, 0);
            maxcolor = new Scalar(255, 255, 255);
        }
        List output = Arrays.asList(mincolor, maxcolor);

        return output;
    }

    private List<Mat> prebuild1(Mat mat) {
        Mat open_kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Mat dilate_kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 11));

        Mat hsv = new Mat();
        Imgproc.cvtColor(mat, hsv, Imgproc.COLOR_BGR2HSV);

        Imgproc.GaussianBlur(mat, mat, new Size(11, 11), 0, 0);

        Scalar min = SS_color(channel_color_code()).get(0);
        Scalar max = SS_color(channel_color_code()).get(1);
        System.out.println("min: " + min);
        System.out.println("max: " + max);

        Mat mask = new Mat();
        Core.inRange(hsv, min, max, mask);


        Mat open = new Mat();
        Imgproc.morphologyEx(mask, open, Imgproc.MORPH_OPEN, open_kernel);

        Mat dilate = new Mat();
        Imgproc.dilate(open, dilate, dilate_kernel, new Point(-1, -1), 2);

        List output = Arrays.asList(dilate, open);


        return output;
    }
}
