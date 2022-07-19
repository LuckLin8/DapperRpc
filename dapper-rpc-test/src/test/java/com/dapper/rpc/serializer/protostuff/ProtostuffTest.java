package com.dapper.rpc.serializer.protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ProtostuffTest {
    static RuntimeSchema<Po> poSchema = RuntimeSchema.createFrom(Po.class);

    private static byte[] decode(Po po){
        return ProtostuffIOUtil.toByteArray(po, poSchema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }

    private static Po ecode(byte[] bytes){
        Po po = poSchema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, po, poSchema);
        return po;
    }

    @Test
    public void serializer() {
        InnerPo innerPo = new InnerPo(1, "InnerPo1");
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        Po po = new Po(1, "Fong", "备注", 24, new int[]{1,2,3,4},innerPo,list);
        System.out.println(po);
        byte[] bytes = decode(po);
        System.out.println(bytes.length);
        Po newPo = ecode(bytes);
        System.out.println(newPo);
    }
}
