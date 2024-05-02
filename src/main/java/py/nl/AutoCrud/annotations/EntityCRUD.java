package py.nl.AutoCrud.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface EntityCRUD {
	public String title();
	public String formTitle();
	public int columnCount()default 1;
	public int width() default 80;
	public int height() default 80;
}
