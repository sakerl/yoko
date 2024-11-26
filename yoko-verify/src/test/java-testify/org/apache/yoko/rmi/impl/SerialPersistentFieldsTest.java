/*
 * Copyright 2024 IBM Corporation and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an \"AS IS\" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.apache.yoko.rmi.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectInputStream.GetField;
import java.io.ObjectOutputStream;
import java.io.ObjectOutputStream.PutField;
import java.io.ObjectStreamField;
import java.io.Serializable;

import org.apache.yoko.orb.CORBA.InputStream;
import org.apache.yoko.orb.CORBA.OutputStream;
import org.junit.jupiter.api.Test;
import org.omg.CORBA.ORB;

import testify.iiop.annotation.ConfigureOrb;

@ConfigureOrb
public class SerialPersistentFieldsTest {
    static class PrimitiveData implements Serializable {
        private static final ObjectStreamField[] serialPersistentFields = {
                new ObjectStreamField("z", boolean.class),
                new ObjectStreamField("b", byte.class),
                new ObjectStreamField("c", char.class),
                new ObjectStreamField("s", short.class),
                new ObjectStreamField("i", int.class),
                new ObjectStreamField("f", float.class),
                new ObjectStreamField("j", long.class),
                new ObjectStreamField("d", double.class),
        };
        static final boolean Z = true;
        static final byte B = -127;
        static final char C = 'C';
        static final short S = 0x0F00;
        static final int I = 0xCAFEBABE;
        static final float F = 3.14F;
        static final long J = 0xFEED_FACE_DEAD_BEEFL;
        static final double D = 6.28D;

        private void writeObject(ObjectOutputStream out) throws IOException {
            System.out.println("### writeObject() called");
            PutField fields = out.putFields();
            fields.put("z", Z);
            fields.put("b", B);
            fields.put("c", C);
            fields.put("s", S);
            fields.put("i", I);
            fields.put("f", F);
            fields.put("j", J);
            fields.put("d", D);
            out.writeFields();
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            GetField fields = in.readFields();
            assertEquals(Z, fields.get("z", false));
            assertEquals(B, fields.get("b", 0));
            assertEquals(C, fields.get("c", (char)0));
            assertEquals(S, fields.get("s", (short)0));
            assertEquals(I, fields.get("i", 0));
            assertEquals(F, fields.get("f", 0F));
            assertEquals(J, fields.get("j", 0L));
            assertEquals(D, fields.get("d", 0D));
        }
    }

    @Test
    public void testPrimitiveData(ORB orb) throws IOException {
        System.out.println("###Hello### " + orb.getClass().getName());
        PrimitiveData obj = new PrimitiveData();
        OutputStream out = (OutputStream)orb.create_output_stream();
        out.write_value(obj);
        System.out.println(out.getBufferReader().dumpAllData());
        InputStream in = out.create_input_stream();
    }
}
