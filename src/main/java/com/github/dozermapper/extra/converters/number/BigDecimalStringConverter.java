package com.github.dozermapper.extra.converters.number;

import java.math.BigDecimal;

import org.apache.commons.beanutils.converters.BigDecimalConverter;

import com.github.dozermapper.core.DozerConverter;

public final class BigDecimalStringConverter extends DozerConverter<BigDecimal, String> {
	
	private final BigDecimalConverter converter = new BigDecimalConverter();
	
	public BigDecimalStringConverter() {
		super(BigDecimal.class, String.class);
	}
	
	@Override
	public BigDecimal convertFrom(String source, BigDecimal destination) {
		return converter.convert(BigDecimal.class, source);
	}

	@Override
	public String convertTo(BigDecimal source, String destination) {
		return source.toPlainString();
	}
	
}