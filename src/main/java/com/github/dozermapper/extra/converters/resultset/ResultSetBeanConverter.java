package com.github.dozermapper.extra.converters.resultset;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.github.dozermapper.core.CustomConverter;
import com.github.dozermapper.core.MappingException;

public final class ResultSetBeanConverter implements CustomConverter {
	
	/**
	 * 从ResultSet中的到查询结果的列
	 * @param rs {@link ResultSet} 结果集
	 * @return  列名称集合
	 * @throws SQLException
	 */
	public String[] getColNames(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		String[] colNames = new String[count];
		for (int i = 1; i <= count; i++) {
			colNames[i - 1] = rsmd.getColumnLabel(i).toLowerCase();
		}
		return colNames;
	}
	
	/**
	 * 转换接口实现 
	 * @param destinationFieldValue：目标字段值
	 * @param sourceFieldValue：源字段值
	 * @param destinationClass:目标字段类型
	 * @param sourceClass：源字段类型
	 * @return 转换后的结果
	 */
	public Object convert(Object destinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
		if (sourceFieldValue == null) {
			return null;
		}else{
			//ResultSet to JavaBean
			if (sourceClass.equals(ResultSet.class)&& sourceFieldValue instanceof ResultSet) {
				try {
					// 获取类属性
					BeanInfo beanInfo = Introspector.getBeanInfo(destinationClass);
					Object object = (destinationFieldValue!=null)?destinationFieldValue: destinationClass.newInstance();// 创建JavaBean对象
					ResultSet rest = (ResultSet) sourceFieldValue;
					String[] colNames = getColNames(rest);
					while (rest.next()) {
						for (int i = 0; i < colNames.length; i++) {
							String colName = colNames[i].toLowerCase();
							// 给JavaBean对象的属性赋值
							for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
								String propertyName = descriptor.getName();
								if (colName.equals(propertyName)) {
									descriptor.getWriteMethod().invoke(object,rest.getObject(colName));
								}
							}
						}
					}
					return object;
				} catch (Exception e) {
					throw new MappingException(e);
				}
			}
		}
		throw new MappingException("Converter ResultSetBeanConverter used incorrectly. Arguments passed in were:" + destinationFieldValue + " and " + sourceFieldValue);
	}
}