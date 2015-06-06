package org.softdays.mandy.dto.converter;

import org.dozer.DozerConverter;
import org.softdays.mandy.core.PersistentEnum;
import org.softdays.mandy.core.converter.EnumConverterUtil;

public class PersistentEnumToCharConverter extends DozerConverter<PersistentEnum, Character> {

    public PersistentEnumToCharConverter() {
        super(PersistentEnum.class, Character.class);
    }

    @Override
    public Character convertTo(final PersistentEnum source, final Character destination) {
        return source.getId();
    }

    @Override
    @SuppressWarnings("unchecked")
    public PersistentEnum convertFrom(final Character source, final PersistentEnum destination) {
        return EnumConverterUtil.valueOf(determineClass(), source);
    }

    @SuppressWarnings("rawtypes")
    private Class determineClass() {
        try {
            return Class.forName(this.getParameter());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("No class found for <" + this.getParameter() + ">");
        }
    }

}
