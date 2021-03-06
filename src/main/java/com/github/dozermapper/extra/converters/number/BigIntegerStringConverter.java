package com.github.dozermapper.extra.converters.number;

import java.math.BigInteger;

import org.apache.commons.beanutils.converters.BigIntegerConverter;

import com.github.dozermapper.core.DozerConverter;

public final class BigIntegerStringConverter extends DozerConverter<BigInteger, String> {
	
	private final BigIntegerConverter converter = new BigIntegerConverter();
     
	public BigIntegerStringConverter() {
		super(BigInteger.class, String.class);
	}
	
	@Override
	public BigInteger convertFrom(String source, BigInteger destination) {
		return converter.convert(BigInteger.class, source);
	}

	@Override
	public String convertTo(BigInteger source, String destination) {
		return source.toString();
	}
	
}