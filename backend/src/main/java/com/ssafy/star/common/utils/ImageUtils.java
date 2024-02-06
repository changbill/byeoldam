package com.ssafy.star.common.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    private static final int TARGET_HEIGHT = 1024;

    //이미지 리사이징
    public static BufferedImage resizeImage(MultipartFile multipartFile) throws IOException {

        BufferedImage sourceImage = turnImage(multipartFile);

        int newWidth, newHeight;

        if(sourceImage.getWidth() > TARGET_HEIGHT || sourceImage.getHeight() > TARGET_HEIGHT){
            // 가로가 세로보다 큰 경우
            if (sourceImage.getWidth() >= sourceImage.getHeight()) {
                newWidth = TARGET_HEIGHT;
//                newHeight = (int) (TARGET_HEIGHT * ((double) sourceImage.getHeight() / sourceImage.getWidth()));
                newHeight = TARGET_HEIGHT;
            } else { // 세로가 가로보다 큰 경우
//                newWidth = (int) (TARGET_HEIGHT * ((double) sourceImage.getWidth() / sourceImage.getHeight()));
                newWidth = TARGET_HEIGHT;
                newHeight = TARGET_HEIGHT;
            }
        } else {
            newWidth = TARGET_HEIGHT;
            newHeight = TARGET_HEIGHT;
        }

        return Scalr.resize(sourceImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, newWidth, newHeight);
    }


    //썸네일 리사이징
    public static BufferedImage resizeThumbnail(MultipartFile multipartFile) throws IOException {

        BufferedImage sourceThumbnail = turnImage(multipartFile);

        int width = sourceThumbnail.getWidth();
        int height = sourceThumbnail.getHeight();
        int minLen = Math.min(width, height);

        BufferedImage thumbImage = Scalr.crop(sourceThumbnail, (width - minLen) / 2, (height - minLen) / 2, minLen, minLen);

        return Scalr.resize(thumbImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 200);
    }

    //BufferedImage를 File로 변환
    public static File convert(BufferedImage bufferedImage, String sourceFileName) throws IOException {
        try {
            File file = new File(sourceFileName);
            String extension = sourceFileName.substring(sourceFileName.lastIndexOf(".") + 1);
            if (extension.equals("blob")) extension = "png";
            ImageIO.write(bufferedImage, extension, file);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Cannot convert BufferedImage to File");
    }


    private static BufferedImage turnImage(MultipartFile multipartFile) throws IOException {
        //원본 파일의 Orientation 정보를 읽는다.
        int orientation = 1;    // 회전정보, 1. 0도 3. 180도 6. 270도 8. 90도 회전한 정보

        Metadata metadata;  //이미지 메타 데이터 객체
        Directory directory;
        JpegDirectory jpegDirectory;


        try {
            metadata = ImageMetadataReader.readMetadata(multipartFile.getInputStream());
            directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
            if (directory != null) {
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);  //회전 정보
            }
        } catch (Exception e) {
            orientation = 1;
        }

        //imageFile
        BufferedImage sourceImage = ImageIO.read(multipartFile.getInputStream());    //현재 여기서 오류가 남

        //회전시키기
        switch (orientation) {
            case 1:
                break;
            case 3:
                sourceImage = Scalr.rotate(sourceImage, Scalr.Rotation.CW_180, null);
                break;
            case 6:
                sourceImage = Scalr.rotate(sourceImage, Scalr.Rotation.CW_90, null);
            case 8:
                sourceImage = Scalr.rotate(sourceImage, Scalr.Rotation.CW_270, null);
            default:
                orientation = 1;
                break;
        }
        return sourceImage;
    }


}
