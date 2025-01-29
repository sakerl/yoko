/*
 * Copyright 2025 IBM Corporation and others.
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
package org.apache.yoko.orb.OBPortableInterceptor;

import org.apache.yoko.util.MinorCodes;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.ORB;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.omg.CORBA.VM_NONE;
import org.omg.CORBA.ValueMember;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.IOP.IORHelper;
import org.omg.PortableInterceptor.AdapterNameHelper;
import org.omg.PortableInterceptor.ORBIdHelper;
import org.omg.PortableInterceptor.ServerIdHelper;

import java.io.Serializable;

import static org.apache.yoko.util.MinorCodes.MinorIncompatibleObjectType;
import static org.apache.yoko.util.MinorCodes.MinorTypeMismatch;
import static org.apache.yoko.util.MinorCodes.describeBadOperation;
import static org.apache.yoko.util.MinorCodes.describeBadParam;
import static org.omg.CORBA.CompletionStatus.COMPLETED_NO;
import static org.omg.CORBA.TCKind.tk_ulong;

//
// IDL:orb.yoko.apache.org/OBPortableInterceptor/TransientORT:1.0
//
final public class TransientORTHelper
{
    public static void
    insert(Any any, TransientORT val)
    {
        any.insert_Value(val, type());
    }

    public static TransientORT
    extract(Any any)
    {
        if(any.type().equivalent(type()))
        {
            Serializable _ob_v = any.extract_Value();
            if(_ob_v == null || _ob_v instanceof TransientORT)
                return (TransientORT)_ob_v;
        }


        throw new BAD_OPERATION(
            describeBadOperation(MinorTypeMismatch),
            MinorTypeMismatch, COMPLETED_NO);
    }

    private static org.omg.CORBA.TypeCode typeCode_;

    public static org.omg.CORBA.TypeCode
    type()
    {
        if(typeCode_ == null)
        {
            ORB orb = ORB.init();
            ValueMember[] members = new ValueMember[5];

            members[0] = new ValueMember();
            members[0].name = "the_server_id";
            members[0].type = ServerIdHelper.type();
            members[0].access = PRIVATE_MEMBER.value;

            members[1] = new ValueMember();
            members[1].name = "the_orb_id";
            members[1].type = ORBIdHelper.type();
            members[1].access = PRIVATE_MEMBER.value;

            members[2] = new ValueMember();
            members[2].name = "the_adapter_name";
            members[2].type = AdapterNameHelper.type();
            members[2].access = PRIVATE_MEMBER.value;

            members[3] = new ValueMember();
            members[3].name = "the_create_time";
            members[3].type = orb.get_primitive_tc(tk_ulong);
            members[3].access = PRIVATE_MEMBER.value;

            members[4] = new ValueMember();
            members[4].name = "the_ior_template";
            members[4].type = IORHelper.type();
            members[4].access = PRIVATE_MEMBER.value;

            typeCode_ = orb.create_value_tc(id(), "TransientORT", VM_NONE.value, null, members);
        }

        return typeCode_;
    }

    public static String
    id()
    {
        return "IDL:orb.yoko.apache.org/OBPortableInterceptor/TransientORT:1.0";
    }

    public static TransientORT
    read(InputStream in)
    {
        if(!(in instanceof org.omg.CORBA_2_3.portable.InputStream)) {
            throw new BAD_PARAM(describeBadParam(MinorIncompatibleObjectType),
                MinorIncompatibleObjectType,
                COMPLETED_NO);
        }
        return (TransientORT)((org.omg.CORBA_2_3.portable.InputStream)in).read_value(id());
    }

    public static void
    write(OutputStream out, TransientORT val)
    {
        if(!(out instanceof org.omg.CORBA_2_3.portable.OutputStream)) {
            throw new BAD_PARAM(describeBadParam(MinorIncompatibleObjectType),
                MinorIncompatibleObjectType,
                COMPLETED_NO);
        }
        ((org.omg.CORBA_2_3.portable.OutputStream)out).write_value(val, id());
    }
}
