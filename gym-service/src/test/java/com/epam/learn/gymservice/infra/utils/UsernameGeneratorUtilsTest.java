package com.epam.learn.gymservice.infra.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UsernameGeneratorUtilsTest {

  private static final String FIRST_NAME = "John";
  private static final String LAST_NAME = "Doe";

  @Test
  void test_generateUsername_unique() {
    String username = UsernameGeneratorUtils.generateUsername(FIRST_NAME, LAST_NAME);
    String anotherUsername = UsernameGeneratorUtils.generateUsername(FIRST_NAME, LAST_NAME);

    assertThat(username).isNotNull();
    assertThat(anotherUsername).isNotNull();
    assertThat(username).matches("John.Doe-\\w{3}");
    assertThat(anotherUsername).matches("John.Doe-\\w{3}");
    assertThat(username).isNotEqualTo(anotherUsername);
  }
}
