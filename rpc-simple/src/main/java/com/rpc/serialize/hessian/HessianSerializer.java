package com.rpc.serialize.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.rpc.serialize.Serializer;
import org.yaml.snakeyaml.serializer.SerializerException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: HessianSerializer.java
 * @Description: Hessian 序列化
 * @CreateTime: 2022/7/27 16:20:00
 **/
public class HessianSerializer implements Serializer {
    @Override
    public byte[] serializer(Object obj) {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())   {
            HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
            hessianOutput.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new SerializerException("Serialization Failed");
        }
    }

    @Override
    public <T> T deserializer(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)){
            HessianInput hessianInput = new HessianInput(byteArrayInputStream);
            Object object = hessianInput.readObject();
            return clazz.cast(object);

        } catch (Exception e) {
            throw new SerializerException("Deserialization Failed");
        }
    }
}
