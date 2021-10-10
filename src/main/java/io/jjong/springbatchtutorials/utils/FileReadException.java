/*
 * Created by Jongsang Han on 2021/05/20
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Dev Backend Team <jongsang@bigin.io>, 2021/05/21
 */

package io.jjong.springbatchtutorials.utils;

/**
 * create on 2021/05/21. create by IntelliJ IDEA.
 *
 * <p> File 읽어올때 문제가 발생할 경우 해당 Exception이 발생 한다. </p>
 *
 * @author Jongsang Han
 * @version 1.0
 * @see io.bigin.share.file.FileHelper
 * @since 1.0
 */
public class FileReadException extends Exception {

  public FileReadException(Exception e) {
    super(e);
  }
}
