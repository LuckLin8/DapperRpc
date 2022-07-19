package com.dapper.rpc.serializer.protobuf;

import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProtoBufTest {

    @Test
    public void protobufClient() throws IOException {
        Socket socket = new Socket("127.0.0.1",3030);

        PersonFactory.Person.Builder person = PersonFactory.Person.newBuilder();

        PersonFactory.Addr.Builder addr = PersonFactory.Addr.newBuilder();
        addr.setContry("china").setCity("shenzhen");

        person.setId(1).setAge(12).setName("ccf");
        person.setAddr(addr);

        byte[] messageBody = person.build().toByteArray();

        int headerLen = 1;
        byte[] message = new byte[headerLen+messageBody.length];
        message[0] = (byte)messageBody.length;
        System.arraycopy(messageBody, 0,  message, 1, messageBody.length);
        System.out.println("msg len:"+message.length);
        socket.getOutputStream().write(message);
    }

    @Test
    public void protobufServer() throws IOException {
        ServerSocket serverSock = new ServerSocket(3030);
        while(true){
            Socket sock = serverSock.accept();
            byte[] msg = new byte[256];
            sock.getInputStream().read(msg);
            int msgBodyLen = msg[0];
            System.out.println("msg body len:"+msgBodyLen);
            byte[] msgbody = new byte[msgBodyLen];
            System.arraycopy(msg, 1, msgbody, 0, msgBodyLen);

            PersonFactory.Person person = PersonFactory.Person.parseFrom(msgbody);

            System.out.println("Receive:");
            System.out.println(person);
        }

    }
}
