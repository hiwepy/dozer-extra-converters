package com.github.dozermapper.extra.converters;

import com.alibaba.fastjson.JSONObject;
import com.github.dozermapper.core.CustomConverter;
import com.github.dozermapper.core.MappingException;

public final class JSONObjectStringConverter implements CustomConverter {
	
	public Object convert(Object destinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
		
		if (sourceFieldValue == null) {
			return null;
		}
		
		//String to JSONObject
		if (sourceClass.equals(String.class)) {
			return JSONObject.parse(sourceFieldValue.toString());
		}
		//JSONObject to String
		if (sourceClass.equals(JSONObject.class)) {
			return JSONObject.toJSONString(destinationFieldValue);
		}
		throw new MappingException( "Converter JSONObjectStringConverter used incorrectly. Arguments passed in were:" + destinationFieldValue + " and " + sourceFieldValue);
	}
}