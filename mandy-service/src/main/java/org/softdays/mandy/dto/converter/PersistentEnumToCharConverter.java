package org.softdays.mandy.dto.converter;

import org.dozer.DozerConverter;
import org.softdays.commons.jpa.converter.EnumConverterUtil;
import org.softdays.commons.jpa.converter.GenericPersistentEnum;

@SuppressWarnings("rawtypes")
public class PersistentEnumToCharConverter
        extends DozerConverter<GenericPersistentEnum, Character> {

    public PersistentEnumToCharConverter() {
        super(GenericPersistentEnum.class, Character.class);
    }

    @Override
    public Character convertTo(final GenericPersistentEnum source, final Character destination) {
        return (Character) source.getPk();
    }

    @Override
    @SuppressWarnings("unchecked")
    public GenericPersistentEnum convertFrom(final Character source,
            final GenericPersistentEnum destination) {
        return (GenericPersistentEnum) EnumConverterUtil.valueOf(determineClass(), source);
    }

    private Class determineClass() {
        try {
            return Class.forName(this.getParameter());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("No class found for <" + this.getParameter() + ">");
        }
    }

}
