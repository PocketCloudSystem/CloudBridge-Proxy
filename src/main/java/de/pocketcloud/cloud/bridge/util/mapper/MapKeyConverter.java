package de.pocketcloud.cloud.bridge.util.mapper;

public interface MapKeyConverter<T, R> {

    R toValue(T obj);
    T fromValue(R value);
}