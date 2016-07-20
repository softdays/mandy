package org.softdays.commons.jpa.converter;

/**
 * Helps to retrieve Enum from id.
 * 
 * @author repatriarche
 * 
 */
public final class EnumConverterUtil {

    private EnumConverterUtil() {
        // private constructor
    }

    /**
     * Retourne la valeur d'une enum en fonction de son identifiant.
     *
     * @param persistentClass
     *            La classe de l'énumération
     * @param id
     *            L'identifiant recherché
     * @param <S>
     *            Type de l'énumeration persistante
     * 
     * @return La valeur de l'énumération en fonction de l'identifiant passé en paramètre
     */
    public static <S extends Enum<S> & GenericPersistentEnum<T>, T> S valueOf(
            final Class<S> persistentClass, final T pk) {

        for (S e : persistentClass.getEnumConstants()) {
            if (e.getPk().equals(pk)) {
                return e;
            }
        }

        throw new IllegalArgumentException(
                "No enum const " + persistentClass.getName() + " for pk \'" + pk + '\'');
    }

}
