package com.epam.learn.gymservice.infra.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.Duration;

@Converter(autoApply = true)
public class DurationAttributeConverter implements AttributeConverter<Duration, Short> {

  @Override
  public Short convertToDatabaseColumn(Duration attribute) {
    return attribute == null ? null : (short) attribute.toMinutes();
  }

  @Override
  public Duration convertToEntityAttribute(Short dbData) {
    return dbData == null ? null : Duration.ofMinutes(dbData);
  }
}
