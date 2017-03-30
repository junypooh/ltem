package com.kt.giga.home.b2b.web.configuration;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;

import java.io.*;

/**
 * com.kt.giga.home.b2b.web.configuration
 * <p>
 * Created by cecil on 2017. 2. 6..
 */
public class ObjectStreamSerializer implements StreamSerializer<Object> {
    public int getTypeId() {
        return 2;
    }

    public void write(ObjectDataOutput objectDataOutput, Object object)
            throws IOException {
        ObjectOutputStream out = new ObjectOutputStream((OutputStream) objectDataOutput);
        out.writeObject(object);
        out.flush();
    }

    public Object read(ObjectDataInput objectDataInput) throws IOException {
        ObjectInputStream in = new ObjectInputStream((InputStream) objectDataInput);
        try {
            return in.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    public void destroy() {
    }

}