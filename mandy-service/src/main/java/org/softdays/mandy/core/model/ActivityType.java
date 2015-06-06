package org.softdays.mandy.core.model;

import org.softdays.mandy.core.PersistentEnum;

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
public enum ActivityType implements PersistentEnum {

    ANA('A', "Analysis/Design"),

    MOD('M', "Modeling"),

    FIX('F', "Fix/Defect"),

    EVO('E', "Evolution"),

    INT('I', "Configuration/Integration"),

    REU('C', "Coordination/Meeting"),

    UNS('U', "Unspecified");

    private Character id;

    private String description;

    /**
     * Instantiates a new activity type.
     * 
     * @param desc
     *            the desc
     */
    ActivityType(final Character id, final String desc) {
        this.id = id;
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
    public Character getId() {
        return id;
    }

}
