package org.softdays.mandy.dto.converter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.jboss.logging.Logger;

public class EnumToStringConverter implements CustomConverter {

    private static final Logger LOGGER = Logger.getLogger(EnumToStringConverter.class);

    @Override
    public Object convert(final Object destination, final Object source,
            final Class<?> destinationClass, final Class<?> sourceClass) {

        if (source == null) {
            return null;
        }

        if (destinationClass != null) {

            if (destinationClass.getSimpleName().equalsIgnoreCase("String")) {

                return this.getString(source);

            } else if (destinationClass.isEnum()) {

                return this.getEnum(destinationClass, source);

            } else {

                throw new MappingException(new StringBuilder("Converter ")
                        .append(this.getClass().getSimpleName())
                        .append(" was used incorrectly. Arguments were: ")
                        .append(destinationClass.getClass().getName()).append(" and ")
                        .append(source).toString());
            }

        }

        return null;

    }

    private Object getString(final Object object) {

        String value = object.toString();

        return value;

    }

    private Object getEnum(final Class<?> destinationClass, final Object source) {

        Object enumeration = null;

        Method[] ms = destinationClass.getMethods();

        for (Method m : ms) {

            if (m.getName().equalsIgnoreCase("valueOf")) {
                try {
                    enumeration = m.invoke(destinationClass.getClass(), (String) source);
                } catch (IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e) {
                    LOGGER.error(e.getMessage());
                }

                return enumeration;

            }

        }

        return null;

    }
}
