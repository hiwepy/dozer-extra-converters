package com.github.dozermapper.extra.converters;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.dozermapper.core.CustomConverter;
import com.github.dozermapper.core.MappingException;

public final class JSONArrayStringConverter implements CustomConverter {
	
	public Object convert(Object destinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
		
		if (sourceFieldValue == null) {
			return null;
		}
		
		//String to JSONObject
		if (sourceClass.equals(String.class)) {
			return JSONArray.parse(sourceFieldValue.toString());
		}
		//JSONOArray to String
		if (sourceClass.equals(JSONObject.class)) {
			return JSONArray.toJSONString(destinationFieldValue);
		}
		throw new MappingException( "Converter JSONObjectStringConverter used incorrectly. Arguments passed in were:" + destinationFieldValue + " and " + sourceFieldValue);
	}
}