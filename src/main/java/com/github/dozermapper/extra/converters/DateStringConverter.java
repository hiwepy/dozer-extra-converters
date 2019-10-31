package com.github.dozermapper.extra.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.dozermapper.core.DozerConverter;
import com.github.dozermapper.core.MappingException;

public final class DateStringConverter extends DozerConverter<Date, String> {
	
	private SimpleDateFormat sdf;
	
	public DateStringConverter() {
		super(Date.class, String.class);
		sdf = new SimpleDateFormat();
	}

	public DateStringConverter(String pattern) {
		super(Date.class, String.class);
		sdf = new SimpleDateFormat(pattern);
	}

	@Override
	public Date convertFrom(String source, Date destination) {
		try {
			return sdf.parse(source);
		} catch (ParseException e) {
			throw new MappingException( "Converter DateStringConverter used incorrectly. Arguments passed in were:" + destination + " and " + source, e);
		}
	}

	@Override
	public String convertTo(Date source, String destination) {
		return sdf.format(source);
	}
}