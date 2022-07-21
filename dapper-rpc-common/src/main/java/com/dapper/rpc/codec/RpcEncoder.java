package com.dapper.rpc.codec;

import com.dapper.rpc.serializer.BaseSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bw.lin
 */
public class RpcEncoder extends MessageToByteEncoder<RpcRequest> {
    private static final Logger logger = LoggerFactory.getLogger(RpcEncoder.class);
    private final Class<?> genericClass;
    private final BaseSerializer serializer;

    public RpcEncoder(Class<?> genericClass, BaseSerializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, RpcRequest in, ByteBuf out) {
        if (genericClass.isInstance(in)) {
            try {
                byte[] data = serializer.serialize(in);
                out.writeInt(data.length);
                out.writeBytes(data);
            } catch (Exception ex) {
                logger.error("Encode error: " + ex);
            }
        }
    }
}