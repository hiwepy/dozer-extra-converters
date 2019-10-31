package com.github.dozermapper.extra.converters;

import org.apache.commons.beanutils.converters.BooleanConverter;

import com.github.dozermapper.core.DozerConverter;

public final class BooleanStringConverter extends DozerConverter<Boolean, String>  {
	
	private final BooleanConverter converter = new BooleanConverter();
    
	public BooleanStringConverter() {
		super(Boolean.class, String.class);
	}

	@Override
	public Boolean convertFrom(String source, Boolean destination) {
		return Boolean.parseBoolean(source);
	}

	@Override
	public String convertTo(Boolean source, String destination) {
		return converter.convert(String.class, source);
	}
	 
}