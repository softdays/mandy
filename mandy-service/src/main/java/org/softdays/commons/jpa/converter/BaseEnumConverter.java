package org.softdays.commons.jpa.converter;

import java.lang.reflect.ParameterizedType;

import javax.persistence.AttributeConverter;

/**
 * A bug will force subclasses to redondly implements AttributeConverter.
 * 
 * @author repatriarche
 * 
 * @param <T>
 *            Le type énuméré concerné
 * 
 * @see
 * 
 *      <pre>
 * https://hibernate.atlassian.net/browse/HHH-8854 This bug will force
 *      subclasses to redondly implements AttributeConverter
 *      </pre>
 */
public class BaseEnumConverter<S extends Enum<S> & GenericPersistentEnum<T>, T>
        implements AttributeConverter<S, T> {

    private final Class<S> persistentClass;

    /**
     * Constructeur.
     */
    @SuppressWarnings("unchecked")
    public BaseEnumConverter() {
        // show limitations and drawbacks og this reflection technique:
        // http://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass()
                .getGenericSuperclass();
        this.persistentClass = (Class<S>) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    public T convertToDatabaseColumn(final S attribute) {
        return attribute == null ? null : attribute.getPk();
    }

    @Override
    public S convertToEntityAttribute(final T pk) {
        return pk == null ? null : EnumConverterUtil.valueOf(this.persistentClass, pk);
    }

}
