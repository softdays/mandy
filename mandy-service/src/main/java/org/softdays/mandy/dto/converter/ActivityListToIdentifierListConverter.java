package org.softdays.mandy.dto.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;
import org.dozer.CustomConverter;
import org.softdays.mandy.core.model.Activity;

import edu.emory.mathcs.backport.java.util.Collections;

public class ActivityListToIdentifierListConverter implements CustomConverter {

	@SuppressWarnings("unchecked")
	@Override
	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
		
		if (sourceFieldValue == null) {
			return null;
		}		
		
		if (ClassUtils.getAllInterfaces(sourceClass).contains(List.class)) {
			
			List<?> values = (List<?>) sourceFieldValue;
			if (values.isEmpty()) {
				return Collections.emptyList();
			} else {
				Object firstItem = values.get(0);
				if (firstItem instanceof Activity) {
					return this.getActivityIds((List<Activity>) values);
				} else if (firstItem instanceof Long) {
					return this.createActivityFromIds((List<Long>) values);
				} else {
					throw new IllegalArgumentException("non suported type for conversion");
				}
			}
		} 
		// abnormal
		throw new IllegalArgumentException("sourceFieldValue is not a list");
	}

	private Object getActivityIds(List<Activity> values) {
		List<Long> results = new ArrayList<>(values.size());
		for (Activity activity : values) {
			results.add(activity.getId());
		}
		return results;
	}
	
	private Object createActivityFromIds(List<Long> values) {
		List<Activity> results = new ArrayList<>(values.size());
		for (Long activityId : values) {
			results.add(new Activity(activityId));
		}
		return results;
	}

}
