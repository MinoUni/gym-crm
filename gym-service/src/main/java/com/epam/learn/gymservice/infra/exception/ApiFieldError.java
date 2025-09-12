package com.epam.learn.gymservice.infra.exception;

public record ApiFieldError(
    String code, String property, String message, Object rejectedValue, String path) {}
