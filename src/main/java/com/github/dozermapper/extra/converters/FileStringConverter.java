package com.github.dozermapper.extra.converters;

import java.io.File;

import org.apache.commons.beanutils.converters.FileConverter;

import com.github.dozermapper.core.DozerConverter;

public final class FileStringConverter extends DozerConverter<File, String>  {
	
	private FileConverter converter = new FileConverter() ;
	
	public FileStringConverter() {
		super(File.class, String.class);
	}

	@Override
	public String convertTo(File source, String destination) {
		return source.getAbsolutePath();
	}

	@Override
	public File convertFrom(String source, File destination) {
		return converter.convert(File.class, source);
	}

}