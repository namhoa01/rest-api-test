package gss.workshop.testing.utils;

import io.restassured.response.Response;
import org.junit.Assert;

import java.lang.reflect.Array;
import java.util.List;

public class ValidationUtils {

  /**
   * Http status code validation
   *
   * @param res Http response is returned after sending a request to specific service
   * @param expectedStatusCode any http status code that is returned expectation
   */
  public static void validateStatusCode(Response res, int expectedStatusCode) {
    res.then().statusCode(expectedStatusCode);
  }

  /**
   * String value validation for text equal
   *
   * @param expectedStr expected text
   * @param actualStr actual text
   */
  public static void validateStringEqual(Object expectedStr, Object actualStr) {
    Assert.assertEquals(expectedStr, actualStr);
  }

  /**
   * False value validation for boolean
   *
   * @param expectedStr expected value
   */
  public static void validateFalseValue(boolean expectedStr) {
    Assert.assertFalse(expectedStr);
  }

  /**
   * Empty list validation
   *
   * @param expectedList expected list
   */
  public static void validateEmptyList(List<Object> expectedList) {
    Assert.assertTrue(expectedList.isEmpty());
  }
}
