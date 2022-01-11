package gss.workshop.testing.tests;

import static io.restassured.RestAssured.baseURI;

import gss.workshop.testing.utils.PropertyReader;

public class TestBase {

  public static final int STATUS_CODE_200 = 200;
  public static final int STATUS_CODE_404 = 404;
  public static final String ZERO = "0";
  public static final String OBJ_BOARD = "board";
  public static final String OBJ_CARD = "card";
  public static final String LIST_TODO = "ToDo";
  public static final String LIST_DONE = "Done";

  protected static PropertyReader prop;
  protected static String token;
  protected static String key;
  protected static String version;

  public TestBase() {
    prop = PropertyReader.getInstance();
    baseURI = prop.getProperty("baseURI");
    token = prop.getProperty("token");
    key = prop.getProperty("key");
    version = prop.getProperty("version");
  }
}
