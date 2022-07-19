package com.dapper.rpc.serializer.protostuff;

import com.dapper.rpc.serializer.BaseSerializer;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bw.lin
 */
public class ProtostuffSerializer extends BaseSerializer {

    private final Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    /**
     * 无需构造器进行实例化对象
     */
    private final Objenesis objenesis = new ObjenesisStd(true);



    @SuppressWarnings("unchecked")
    private <T> Schema<T> getSchema(Class<T> cls) {
        // for thread-safe
        return (Schema<T>) cachedSchema.computeIfAbsent(cls, RuntimeSchema::createFrom);
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> byte[] serialize(T obj) {
        var buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        Class<T> cls = (Class<T>) obj.getClass();
        try {
            Schema<T> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    /**
     * //var message = schema.newMessage();
     * 或者直接采用schema.newMessage也可以实例化对象
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    @Override
    public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
        try {
            var message = objenesis.newInstance(clazz);
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(bytes, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
