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
package org.apache.yoko.orb.OB;

import static org.apache.yoko.util.MinorCodes.MinorTypeMismatch;
import static org.apache.yoko.util.MinorCodes.describeBadOperation;
import static org.omg.CORBA.CompletionStatus.COMPLETED_NO;
import static org.omg.CORBA.TCKind.tk_boolean;
import static org.omg.CORBA.TCKind.tk_short;
import static org.omg.CORBA.TCKind.tk_ulong;

import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

//
// IDL:orb.yoko.apache.org/OB/RetryAttributes:1.0
//
public final class RetryAttributesHelper
{
    public static void
    insert(Any any, RetryAttributes val)
    {
        OutputStream out = any.create_output_stream();
        write(out, val);
        any.read_value(out.create_input_stream(), type());
    }

    public static RetryAttributes
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
            StructMember[] members = new StructMember[4];

            members[0] = new StructMember();
            members[0].name = "mode";
            members[0].type = orb.get_primitive_tc(tk_short);

            members[1] = new StructMember();
            members[1].name = "interval";
            members[1].type = orb.get_primitive_tc(tk_ulong);

            members[2] = new StructMember();
            members[2].name = "max";
            members[2].type = orb.get_primitive_tc(tk_ulong);

            members[3] = new StructMember();
            members[3].name = "remote";
            members[3].type = orb.get_primitive_tc(tk_boolean);

            typeCode_ = orb.create_struct_tc(id(), "RetryAttributes", members);
        }

        return typeCode_;
    }

    public static String
    id()
    {
        return "IDL:orb.yoko.apache.org/OB/RetryAttributes:1.0";
    }

    public static RetryAttributes
    read(InputStream in)
    {
        RetryAttributes _ob_v = new RetryAttributes();
        _ob_v.mode = in.read_short();
        _ob_v.interval = in.read_ulong();
        _ob_v.max = in.read_ulong();
        _ob_v.remote = in.read_boolean();
        return _ob_v;
    }

    public static void
    write(OutputStream out, RetryAttributes val)
    {
        out.write_short(val.mode);
        out.write_ulong(val.interval);
        out.write_ulong(val.max);
        out.write_boolean(val.remote);
    }
}
