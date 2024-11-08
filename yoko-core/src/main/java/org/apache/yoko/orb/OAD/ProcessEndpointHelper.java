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
package org.apache.yoko.orb.OAD;

import static org.apache.yoko.util.MinorCodes.MinorIncompatibleObjectType;
import static org.apache.yoko.util.MinorCodes.MinorTypeMismatch;
import static org.apache.yoko.util.MinorCodes.describeBadParam;
import static org.omg.CORBA.CompletionStatus.COMPLETED_NO;

import org.apache.yoko.util.MinorCodes;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;

//
// IDL:orb.yoko.apache.org/OAD/ProcessEndpoint:1.0
//
final public class ProcessEndpointHelper
{
    public static void
    insert(Any any, ProcessEndpoint val)
    {
        any.insert_Object(val, type());
    }

    public static ProcessEndpoint
    extract(Any any)
    {
        if(any.type().equivalent(type()))
            return narrow(any.extract_Object());

        throw new BAD_OPERATION(
            MinorCodes
                    .describeBadOperation(MinorTypeMismatch),
            MinorTypeMismatch, COMPLETED_NO);
    }

    private static TypeCode typeCode_;

    public static TypeCode
    type()
    {
        if(typeCode_ == null)
        {
            ORB orb = ORB.init();
            typeCode_ = orb.create_interface_tc(id(), "ProcessEndpoint");
        }

        return typeCode_;
    }

    public static String
    id()
    {
        return "IDL:orb.yoko.apache.org/OAD/ProcessEndpoint:1.0";
    }

    public static ProcessEndpoint
    read(InputStream in)
    {
        org.omg.CORBA.Object _ob_v = in.read_Object();

        try
        {
            return (ProcessEndpoint)_ob_v;
        }
        catch(ClassCastException ex)
        {
        }

        ObjectImpl _ob_impl;
        _ob_impl = (ObjectImpl)_ob_v;
        _ProcessEndpointStub _ob_stub = new _ProcessEndpointStub();
        _ob_stub._set_delegate(_ob_impl._get_delegate());
        return _ob_stub;
    }

    public static void
    write(OutputStream out, ProcessEndpoint val)
    {
        out.write_Object(val);
    }

    public static ProcessEndpoint
    narrow(org.omg.CORBA.Object val)
    {
        if(val != null)
        {
            try
            {
                return (ProcessEndpoint)val;
            }
            catch(ClassCastException ex)
            {
            }

            if(val._is_a(id()))
            {
                ObjectImpl _ob_impl;
                _ProcessEndpointStub _ob_stub = new _ProcessEndpointStub();
                _ob_impl = (ObjectImpl)val;
                _ob_stub._set_delegate(_ob_impl._get_delegate());
                return _ob_stub;
            }

            throw new BAD_PARAM(describeBadParam(MinorIncompatibleObjectType),
                MinorIncompatibleObjectType,
                COMPLETED_NO);
        }

        return null;
    }

    public static ProcessEndpoint
    unchecked_narrow(org.omg.CORBA.Object val)
    {
        if(val != null)
        {
            try
            {
                return (ProcessEndpoint)val;
            }
            catch(ClassCastException ex)
            {
            }

            ObjectImpl _ob_impl;
            _ProcessEndpointStub _ob_stub = new _ProcessEndpointStub();
            _ob_impl = (ObjectImpl)val;
            _ob_stub._set_delegate(_ob_impl._get_delegate());
            return _ob_stub;
        }

        return null;
    }
}
