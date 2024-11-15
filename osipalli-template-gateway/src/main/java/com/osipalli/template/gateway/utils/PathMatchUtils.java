package com.osipalli.template.gateway.utils;

/**
 * Path matching using dynamic programming algorithm
 */
public class PathMatchUtils {

  /**
   * @param s incoming path
   * @param p rule path
   * @return
   * @desription: In a given pattern pp, only three types of characters can appear:
   * <p>
   * Lowercase letters a-za−z, can match a corresponding lowercase letter;
   * <p>
   * Question mark ?, can match any lowercase letter;
   * <p>
   * Asterisk *, can match any string, can be empty, that is, match zero or any number of lowercase letters.
   * <p>
   * The matching of "lowercase letter" and "question mark" is certain, but the matching of "asterisk" is uncertain, so we need to enumerate all matching situations. In order to reduce repeated enumeration, we can use dynamic programming to solve this problem.
   */
  public static boolean isMatch(String s, String p) {
    int m = s.length();
    int n = p.length();
    boolean[][] dp = new boolean[m + 1][n + 1];
    dp[0][0] = true;
    for (int i = 1; i <= n; ++i) {
      if (p.charAt(i - 1) == '*') {
        dp[0][i] = true;
      } else {
        break;
      }
    }
    for (int i = 1; i <= m; ++i) {
      for (int j = 1; j <= n; ++j) {
        if (p.charAt(j - 1) == '*') {
          dp[i][j] = dp[i][j - 1] || dp[i - 1][j];
        } else if (p.charAt(j - 1) == '?' || s.charAt(i - 1) == p.charAt(j - 1)) {
          dp[i][j] = dp[i - 1][j - 1];
        }
      }
    }
    return dp[m][n];
  }

}
