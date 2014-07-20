package org.softdays.mandy.service.support;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.softdays.mandy.service.MapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperServiceImpl implements MapperService {

    @Autowired
    private Mapper mapper;

    @Override
    public <T> List<T> map(final Iterable<?> iterable,
	    final Class<T> destinationClass) {
	List<T> result = new ArrayList<T>();
	for (Object source : iterable) {
	    T dest = mapper.map(source, destinationClass);
	    result.add(dest);
	}

	return result;
    }

}
