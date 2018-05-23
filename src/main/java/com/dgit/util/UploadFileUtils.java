package com.dgit.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.util.FileCopyUtils;

public class UploadFileUtils {

	// return : /년/월/일/파일이름
	public static String uploadFile(String uploadPath, String originalName, byte[] fileData) throws IOException {
		UUID uid = UUID.randomUUID(); // 중복되지 않는 고유한 키값을 설정할 때 사용
		String saveName = uid.toString() + "_" + originalName;
		String savedPath = calcPath(uploadPath);

		// uploadPath - c://zzz/upload
		// savePath - /2018/05/23
		// savename - uuid_aaa.jpg
		File target = new File(uploadPath + savedPath + "/" + saveName);
		FileCopyUtils.copy(fileData, target);

		// thumbnail 파일만들어서 경로 리턴
		String thumPath = makeThumbnail(uploadPath, savedPath, saveName);

		return thumPath; // savedPath+"/"+saveName;
	}

	// uploadPath : 서버 업로드 주소
	private static String calcPath(String uploadPath) {
		// 년,월,일 폴더 만들기

		Calendar cal = Calendar.getInstance();
		String yearPath = "/" + cal.get(Calendar.YEAR);
		String monthPath = String.format("%s/%02d", yearPath, cal.get(Calendar.MONTH) + 1); // 2018/05
		String datePath = String.format("%s/%02d", monthPath, cal.get(Calendar.DATE));

		makeDir(uploadPath, yearPath, monthPath, datePath);

		return datePath;
	}

	public static void makeDir(String uploadPath, String... paths) {

		for (String path : paths) {
			File dirPath = new File(uploadPath + path);
			if (dirPath.exists() == false) {
				dirPath.mkdirs();
			}
		}
	}

	// uploadPath : c://zzz/upload
	// path: /년/월/일
	// filename : uuid_aaa.jpg
	private static String makeThumbnail(String uploadPath, String path, String filename) throws IOException {
		BufferedImage sourceImg = ImageIO.read(new File(uploadPath + path, filename));

		// FIT_TO_HEIGHT: 썸네일 이미지의 높이를 뒤의 100px로 동잏하게 만듦
		BufferedImage destImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 100);

		// thumbnail 파일이름 : "s_"붙임
		String thumbnailName = uploadPath + path + "/" + "s_" + filename;

		File newFile = new File(thumbnailName);

		// 확장자 찾기
		String formatName = filename.substring(filename.lastIndexOf(".") + 1);

		ImageIO.write(destImg, formatName.toUpperCase(), newFile);

		String savedName = thumbnailName.substring(uploadPath.length());
		// 년/월/일/s_uuid_aaa.jpg
		return savedName;
	}

	// 원본, 썸네일 2가지 파일 삭제
	public static void deleteFile(String outerUploadPath, String filename) {

		File file = new File(outerUploadPath + filename);
		if (file.exists()) {
			file.delete();
		}
		String front = filename.substring(0, 12);
		String end = filename.substring(14);
		String originalName = front + end;

		File file2 = new File(outerUploadPath + originalName);
		if (file2.exists()) {
			file2.delete();
		}
	}
}
