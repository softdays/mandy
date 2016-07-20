package org.softdays.commons.jpa.converter;

/**
 * Interface permettant d'indiquer que l'énumération peut être persistée via une valeur entière
 * arbitraire (i.e. différente de la valeur ordinale de l'énuméré).
 */
public interface IntegerPersistentEnum extends GenericPersistentEnum<Integer> {

}
