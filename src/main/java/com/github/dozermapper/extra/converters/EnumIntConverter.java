package com.github.dozermapper.extra.converters;

import com.github.dozermapper.core.DozerConverter;

public class EnumIntConverter extends DozerConverter<Enum, Integer>  {

	public EnumIntConverter() {
		super(Enum.class, Integer.class);
	}

	@Override
	public Enum convertFrom(Integer source, Enum destination) {
		Enum[]  consts = destination.getClass().getEnumConstants();
		return consts[((Integer) source).intValue()];
	}

	@Override
	public Integer convertTo(Enum source, Integer destination) {
		return Integer.valueOf(((Enum) source).ordinal());
	}
	 
}
