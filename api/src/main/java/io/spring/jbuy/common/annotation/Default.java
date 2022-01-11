package io.spring.jbuy.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom Annotation @Default for using with mapstruct mapper
 * The annotation let mapstruct choose which constructor in entity class to use for entity mapping
 * (in case there are multiple constructors in the entity class)
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.CLASS)
public @interface Default {
}
