package org.softdays.commons.jpa.converter;

import java.lang.reflect.ParameterizedType;

import javax.persistence.AttributeConverter;

/**
 * Convertisseur Hibernate générique.
 */
public class GenericBaseEnumConverter<T extends Enum<T> & GenericPersistentEnum<P>, P>
        implements AttributeConverter<T, P> {

    private final Class<T> persistentClass;

    /**
     * Constructeur.
     */
    @SuppressWarnings("unchecked")
    public GenericBaseEnumConverter() {
        // show limitations and drawbacks og this reflection technique:
        // http://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass()
                .getGenericSuperclass();
        this.persistentClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    public P convertToDatabaseColumn(final T attribute) {
        return attribute == null ? null : attribute.getPk();
    }

    @Override
    public T convertToEntityAttribute(final P id) {
        return id == null ? null : GenericEnumConverterUtil.valueOf(this.persistentClass, id);
    }

}
