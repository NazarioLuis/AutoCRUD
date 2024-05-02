package py.nl.AutoCrud.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface Input {
	String label() default "";
	String[] data() default {};
	boolean longText() default false;
	boolean tableColumn() default false;
}
