/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.netty.server;

import com.mycompany.entities.MessageSent;
import com.mycompany.utils.MessageUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ServerDecodeMsgHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.isReadable()) {
            if (in.readableBytes() < 50) {
                return;
            }
            MessageSent msg = new MessageSent();
            in.markReaderIndex();
            int lengthPush = in.readInt();
            int length = in.readableBytes();
            if (lengthPush > 0) {
                if (length < lengthPush) {
                    in.resetReaderIndex();
                    return;
                }

                byte[] bytes = new byte[length];
                in.readBytes(bytes);
                String totalMessage = new String(bytes, StandardCharsets.UTF_8);
                msg = MessageUtils.decodeJson(totalMessage);
            }

            out.add(msg);
        }
    }

}
