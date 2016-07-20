package org.softdays.mandy.core.model;

import org.softdays.commons.jpa.converter.GenericPersistentEnum;

/**
 * Describes activity types.
 * 
 * This enum has been redesigned in version 1.3 to allow finer granularity of
 * imputation.
 * 
 * The new {@link ActivityCategory} enum replaces the previous role of
 * ActivityType.
 * 
 * @author repatriarche
 * 
 * @since 1.0.0
 * 
 * @version 1.3.0
 *
 */
public enum ActivityType implements GenericPersistentEnum<Character> {

    ANA('A', "Analysis/Design"),

    MOD('M', "Modeling"),

    FIX('F', "Fix/Defect"),

    EVO('E', "Evolution"),

    INT('I', "Configuration/Integration"),

    REU('C', "Coordination/Meeting"),

    UNS('U', "Unspecified");

    private Character pk;

    private String description;

    /**
     * Instantiates a new activity type.
     * 
     * @param desc
     *            the desc
     */
    ActivityType(final Character pk, final String desc) {
        this.pk = pk;
        this.description = desc;
    }

    /**
     * /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name();
    }

    /**
     * Gets the description.
     * 
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    @Override
    public Character getPk() {
        return this.pk;
    }

    public static ActivityType fromCode(final Character code) {
        if (code == null) {
            return null;
        }
        switch (code) {
            case 'A':
                return ActivityType.ANA;

            case 'M':
                return ActivityType.MOD;

            case 'F':
                return ActivityType.FIX;

            case 'E':
                return ActivityType.EVO;

            case 'I':
                return ActivityType.INT;

            case 'C':
                return ActivityType.REU;

            case 'U':
                return ActivityType.UNS;

            default:
                throw new IllegalArgumentException("value not supported");
        }
    }

}
