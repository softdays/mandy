package org.softdays.commons.jpa.converter;

/**
 * Helps to retrieve Enum from id.
 * 
 * @author repatriarche
 * 
 */
public final class GenericEnumConverterUtil {

    private GenericEnumConverterUtil() {
        // private constructor
    }

    /**
     * Retourne la valeur d'une enum en fonction de son identifiant.
     *
     * @param persistentClass
     *            La classe de l'énumération
     * @param id
     *            L'identifiant recherché
     * @param <T>
     *            Type de l'énumeration persistante
     * @return La valeur de l'énumération en fonction de l'identifiant passé en paramètre
     */
    public static <P, T extends Enum<T> & GenericPersistentEnum<P>> T valueOf(
            final Class<T> persistentClass, final P id) {

        for (T e : persistentClass.getEnumConstants()) {
            if (e.getPk().equals(id)) {
                return e;
            }
        }

        throw new IllegalArgumentException(
                "No enum const " + persistentClass.getName() + " for id \'" + id + '\'');
    }

}
