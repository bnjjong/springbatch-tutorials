/*
 * Created by Jongsang Han on 2021/07/28
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Dev Backend Team <jongsang@bigin.io>, 2021/07/28
 */

package io.jjong.springbatchtutorials.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;

/**
 * create on 2021/07/28. create by IntelliJ IDEA.
 *
 * <p> 클래스 설명 </p>
 * <p> {@link } and {@link }관련 클래스 </p>
 *
 * @author Jongsang Han
 * @version 1.0
 * @see
 * @since 지원하는 자바버전 (ex : 5+ 5이상)
 */
@Slf4j
public class FileHelper {
  /**
   * <pre>
   * 객체 생성할 수 없음.
   * </pre>
   */
  private FileHelper() {}
  /**
   * 해당 경로에 파일이 있는지 체크 한다.
   *
   * @param path 파일 경로
   * @return {@code true} 넘어온 {@code path} 값에 파일이 존재할 경우, {@code false} 파일이 존재하지 않을 경우
   */
  public static boolean isExistFile(String path) {
    try {
      return getFileFromResource(path).exists();
    } catch (Exception e) {
      // 파일이 없을 경우 에러가 발생하는데 로그는 따로 찍지 않는다.
      return false;
    }
  }

  /**
   * <p>
   * 해당 경로에서 파일을 읽어와 파일 객체를 리턴해 준다.
   * </p>
   *
   * @param path 파일 경로
   * @return {@code File} 해당 {@code path}에서 읽어온 {@code File} 객체를 리턴 한다.
   * @throws FileReadException 파일을 읽다가 에러가 발생한 경우
   * @see File
   */
  public static File getFileFromResource(String path) throws FileReadException {
    try {
      URL res = null;
      res = FileHelper.class.getClassLoader().getResource(path);

      if (res == null) {
        throw new IllegalArgumentException("path is not valid!>>>" + path);
      }
      log.info(res.getPath());
      return Paths.get(res.toURI()).toFile();
    } catch (Exception e) {
      InputStream in = FileHelper.class.getClassLoader().getResourceAsStream(path);
      try {
        return convertInputStreamToFile(in);
      } catch (IOException ioException) {
        throw new FileReadException(e);
      }
    }
  }


  public static File convertInputStreamToFile(InputStream in) throws IOException {

    File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
    tempFile.deleteOnExit();

    copyInputStreamToFile(in, tempFile);

    return tempFile;
  }

  private static void copyInputStreamToFile(InputStream inputStream, File file)
      throws IOException {

    try (FileOutputStream outputStream = new FileOutputStream(file)) {
      int read;
      byte[] bytes = new byte[1024];

      while ((read = inputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }
    }
  }

  public static void main(String[] args) throws IOException {
    InputStream in = FileHelper.class.getClassLoader()
        .getResourceAsStream("properties/datasource.properties");

    File file = convertInputStreamToFile(in);
    System.out.println(file.getName());
  }
}
