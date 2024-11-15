package com.osipalli.template.gateway.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
public class SignUtil {

  /**
   * Whether sign timed out
   *
   * @param time
   * @return
   */
  public static boolean checkTime(long time) {
    long now = System.currentTimeMillis();
    //超过5分钟
    if (now - time > 30 * 60 * 1000) {
      return false;
    } else {
      return true;
    }
  }

  public static String utf8Encoding(String value, String sourceCharsetName) {
    try {
      return new String(value.getBytes(sourceCharsetName), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(e);
    }
  }


  private static byte[] getMD5Digest(String data) throws IOException {
    byte[] bytes = null;
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      bytes = md.digest(data.getBytes("UTF-8"));
    } catch (GeneralSecurityException gse) {
      throw new IOException(gse);
    }
    return bytes;
  }


  private static String byte2hex(byte[] bytes) {
    StringBuilder sign = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      String hex = Integer.toHexString(bytes[i] & 0xFF);
      if (hex.length() == 1) {
        sign.append("0");
      }
      sign.append(hex.toUpperCase());
      //sign.append(hex.toLowerCase());
    }
    return sign.toString();
  }


  /**
   * get signed
   *
   * @param params parameter set does not contain secretkey
   * @param secret The secretkey of the verification interface
   * @return
   */
  public static String getSign(Map<String, String> params, String timestamp, String secret) {
    String sign = "";
    StringBuilder sb = new StringBuilder();
    //step1: first sort the request parameters
    Set<String> keyset = params.keySet();
    TreeSet<String> sortSet = new TreeSet<String>();
    sortSet.addAll(keyset);
    Iterator<String> it = sortSet.iterator();
    //step2：Link the key value of the parameter and put the secretkey at the end to get the string to be encrypted
    while (it.hasNext()) {
      String key = it.next();
      String value = params.get(key);
      sb.append(key).append("=").append(value).append("&");
    }
    sb.append("timestamp=").append(timestamp).append("&");
    sb.append("secret=").append(secret);

    log.debug(sb.toString());
    byte[] md5Digest;
    try {
      //Get Md5 encryption to get sign
      md5Digest = getMD5Digest(sb.toString());
      sign = byte2hex(md5Digest);
    } catch (IOException e) {
      log.error("generate signature error", e);
    }
    log.debug("sign:" + sign);
    return sign;
  }


}
