package com.github.dozermapper.extra.converters;

import com.alibaba.fastjson.JSONArray;
import com.github.dozermapper.core.DozerConverter;

public final class JSONArrayStringConverter extends DozerConverter<JSONArray, String> {
	
	public JSONArrayStringConverter() {
		super(JSONArray.class, String.class);
	}

	@Override
	public JSONArray convertFrom(String source, JSONArray destination) {
		return JSONArray.parseArray(source);
	}

	@Override
	public String convertTo(JSONArray source, String destination) {
		return JSONArray.toJSONString(source);
	}
	 
}