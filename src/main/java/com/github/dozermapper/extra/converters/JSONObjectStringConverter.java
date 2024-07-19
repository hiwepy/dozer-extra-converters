package com.github.dozermapper.extra.converters;

import com.alibaba.fastjson2.JSONObject;
import com.github.dozermapper.core.DozerConverter;

public final class JSONObjectStringConverter extends DozerConverter<JSONObject, String> {

	public JSONObjectStringConverter() {
		super(JSONObject.class, String.class);
	}

	@Override
	public JSONObject convertFrom(String source, JSONObject destination) {
		return JSONObject.parseObject(source);
	}

	@Override
	public String convertTo(JSONObject source, String destination) {
		return JSONObject.toJSONString(source);
	}

}
