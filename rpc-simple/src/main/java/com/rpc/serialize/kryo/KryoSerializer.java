package com.rpc.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.rpc.remoting.dto.RpcRequest;
import com.rpc.remoting.dto.RpcResponse;
import com.rpc.serialize.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.serializer.SerializerException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: KryoSerializer.java
 * @Description: Kryo序列化
 * @CreateTime: 2022/7/27 14:13:00
 **/
public class KryoSerializer implements Serializer {

    Logger logger = LoggerFactory.getLogger(KryoSerializer.class);

    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(()->{
       Kryo kryo = new Kryo();
       kryo.register(RpcRequest.class);
       kryo.register(RpcResponse.class);
       kryo.register(java.lang.Class[].class);
       kryo.register(java.lang.Class.class);
       kryo.register(java.lang.Object[].class);
       kryo.register(java.lang.Object.class);
       kryo.register(java.lang.Integer.class);
       kryo.register(java.lang.String.class);
       return kryo;
    });

    @Override
    public byte[] serializer(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)){
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            throw new SerializerException("SerializerException Failed");
        }
    }

    @Override
    public <T> T deserializer(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)){
            Kryo kryo = kryoThreadLocal.get();
            // byte -> Object: 从 byte 数组中反序列化出对象
            Object object = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return clazz.cast(object);
        } catch (Exception e) {
            throw new SerializerException("Deserializer Failed");
        }
    }
}
