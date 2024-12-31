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
package org.apache.yoko.orb.OB.CorbalocProtocolPackage;

import org.apache.yoko.util.MinorCodes;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

import static org.apache.yoko.util.MinorCodes.MinorTypeMismatch;
import static org.apache.yoko.util.MinorCodes.describeBadOperation;
import static org.omg.CORBA.CompletionStatus.COMPLETED_NO;
import static org.omg.CORBA.TCKind.tk_octet;

//
// IDL:orb.yoko.apache.org/OB/CorbalocProtocol/ObjectKey:1.0
//
final public class ObjectKeyHelper
{
    public static void
    insert(Any any, byte[] val)
    {
        OutputStream out = any.create_output_stream();
        write(out, val);
        any.read_value(out.create_input_stream(), type());
    }

    public static byte[]
    extract(Any any)
    {
        if(any.type().equivalent(type()))
            return read(any.create_input_stream());
        else

            throw new BAD_OPERATION(
                describeBadOperation(MinorTypeMismatch),
                MinorTypeMismatch, COMPLETED_NO);
    }

    private static TypeCode typeCode_;

    public static TypeCode
    type()
    {
        if(typeCode_ == null)
        {
            ORB orb = ORB.init();
            typeCode_ = orb.create_alias_tc(id(), "ObjectKey", orb.create_sequence_tc(0, orb.get_primitive_tc(tk_octet)));
        }

        return typeCode_;
    }

    public static String
    id()
    {
        return "IDL:orb.yoko.apache.org/OB/CorbalocProtocol/ObjectKey:1.0";
    }

    public static byte[]
    read(InputStream in)
    {
        byte[] _ob_v;
        int len0 = in.read_ulong();
        _ob_v = new byte[len0];
        in.read_octet_array(_ob_v, 0, len0);
        return _ob_v;
    }

    public static void
    write(OutputStream out, byte[] val)
    {
        int len0 = val.length;
        out.write_ulong(len0);
        out.write_octet_array(val, 0, len0);
    }
}
