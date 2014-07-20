package org.softdays.mandy.service;

import java.util.List;

public interface MapperService {

    <T> List<T> map(Iterable<?> uneListe, Class<T> destinationClass);

}