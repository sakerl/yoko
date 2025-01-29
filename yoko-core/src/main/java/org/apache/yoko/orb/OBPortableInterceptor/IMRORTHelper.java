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
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.VM_NONE;
import org.omg.CORBA.ValueMember;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.PortableInterceptor.AdapterNameHelper;
import org.omg.PortableInterceptor.ObjectReferenceTemplateHelper;
import org.omg.PortableInterceptor.ServerIdHelper;

import java.io.Serializable;

import static org.apache.yoko.util.MinorCodes.MinorIncompatibleObjectType;
import static org.apache.yoko.util.MinorCodes.MinorTypeMismatch;
import static org.apache.yoko.util.MinorCodes.describeBadOperation;
import static org.apache.yoko.util.MinorCodes.describeBadParam;
import static org.omg.CORBA.CompletionStatus.COMPLETED_NO;

//
// IDL:orb.yoko.apache.org/OBPortableInterceptor/IMRORT:1.0
//
final public class IMRORTHelper
{
    public static void
    insert(Any any, IMRORT val)
    {
        any.insert_Value(val, type());
    }

    public static IMRORT
    extract(Any any)
    {
        if(any.type().equivalent(type()))
        {
            Serializable _ob_v = any.extract_Value();
            if(_ob_v == null || _ob_v instanceof IMRORT)
                return (IMRORT)_ob_v;
        }


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
            ValueMember[] members = new ValueMember[3];

            members[0] = new ValueMember();
            members[0].name = "the_server_id";
            members[0].type = ServerIdHelper.type();
            members[0].access = PRIVATE_MEMBER.value;

            members[1] = new ValueMember();
            members[1].name = "the_adapter_name";
            members[1].type = AdapterNameHelper.type();
            members[1].access = PRIVATE_MEMBER.value;

            members[2] = new ValueMember();
            members[2].name = "the_real_template";
            members[2].type = ObjectReferenceTemplateHelper.type();
            members[2].access = PRIVATE_MEMBER.value;

            typeCode_ = orb.create_value_tc(id(), "IMRORT", VM_NONE.value, null, members);
        }

        return typeCode_;
    }

    public static String
    id()
    {
        return "IDL:orb.yoko.apache.org/OBPortableInterceptor/IMRORT:1.0";
    }

    public static IMRORT
    read(InputStream in)
    {
        if(!(in instanceof org.omg.CORBA_2_3.portable.InputStream)) {
            throw new BAD_PARAM(describeBadParam(MinorIncompatibleObjectType),
                MinorIncompatibleObjectType,
                COMPLETED_NO);
        }
        return (IMRORT)((org.omg.CORBA_2_3.portable.InputStream)in).read_value(id());
    }

    public static void
    write(OutputStream out, IMRORT val)
    {
        if(!(out instanceof org.omg.CORBA_2_3.portable.OutputStream)) {
            throw new BAD_PARAM(describeBadParam(MinorIncompatibleObjectType),
                MinorIncompatibleObjectType,
                COMPLETED_NO);
        }
        ((org.omg.CORBA_2_3.portable.OutputStream)out).write_value(val, id());
    }
}
