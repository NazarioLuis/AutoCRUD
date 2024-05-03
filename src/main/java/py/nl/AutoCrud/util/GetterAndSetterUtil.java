package py.nl.AutoCrud.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

public class GetterAndSetterUtil
{
	
	public static void callSetter(Object obj, Field field, Object val) throws Exception {
		PropertyDescriptor pd;
		pd = new PropertyDescriptor(field.getName(), obj.getClass());
		pd.getWriteMethod().invoke(obj, val);
	}
	public static Object callGetter(Object obj, Field field) throws Exception {
		PropertyDescriptor pd;
		pd = new PropertyDescriptor(field.getName(), obj.getClass());
		return pd.getReadMethod().invoke(obj);
	}
}
