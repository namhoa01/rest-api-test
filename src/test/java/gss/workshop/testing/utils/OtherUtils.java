package gss.workshop.testing.utils;

import java.util.logging.Logger;

public class OtherUtils {

  private static final Logger logger = Logger.getLogger(String.valueOf(OtherUtils.class));

  public static String randomName(String obj) {
    String name = "My " + obj + " " + (int) (Math.random() * 10000);
    logger.info(String.format("Random text: %s", name));
    return name;
  }
}
