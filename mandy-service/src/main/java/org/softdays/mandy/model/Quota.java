package org.softdays.mandy.model;


public enum Quota {
    NONE(0f), QUARTER(0.25f), HALF(0.5f), THREE_QUARTERS(0.75f), WHOLE(1f);

    private float value;

    private Quota(float value) {
	this.value = value;
    }

    public Float floatValue() {
	return value;
    }

    public static Quota valueOf(float value) {
	Quota result = null;
	for (Quota quota : Quota.values()) {
	    if (quota.value == value) {
		result = quota;
		break;
	    }
	}
	return result;
    }
}
