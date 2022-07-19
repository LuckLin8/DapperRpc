package com.dapper.rpc.serializer;

/**
 * @author bw.lin
 */
public abstract class BaseSerializer {
    public abstract <T> byte[] serialize(T obj);

    public abstract <T> Object deserialize(byte[] bytes, Class<T> clazz);
}
