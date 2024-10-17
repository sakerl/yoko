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
package org.apache.yoko.orb.csi.gssup;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.Security.RequiresSupportsHelper;

public class GSSUPPolicyValueHelper {

    private static TypeCode _type = ORB
            .init()
            .create_struct_tc(
                    GSSUPPolicyValueHelper.id(),
                    "GSSUPPolicyValue",
                    new StructMember[]{
                            new StructMember(
                                    "mode",
                                    ORB
                                            .init()
                                            .create_enum_tc(
                                            RequiresSupportsHelper
                                                    .id(),
                                            "RequiresSupports",
                                            new String[]{
                                                    "SecRequires",
                                                    "SecSupports"}),
                                    null),
                            new StructMember("domain",
                                                           ORB.init()
                                                                   .create_string_tc(0), null)});

    public GSSUPPolicyValueHelper() {
    }

    public static void insert(Any any, GSSUPPolicyValue s) {
        any.type(type());
        write(any.create_output_stream(), s);
    }

    public static GSSUPPolicyValue extract(Any any) {
        return read(any.create_input_stream());
    }

    public static TypeCode type() {
        return _type;
    }

    public String get_id() {
        return id();
    }

    public org.omg.CORBA.TypeCode get_type() {
        return type();
    }

    public void write_Object(OutputStream out,
                             Object obj)
    {
        throw new RuntimeException(" not implemented");
    }

    public Object read_Object(InputStream in) {
        throw new RuntimeException(" not implemented");
    }

    public static String id() {
        return "IDL:org/apache/yoko/orb/csi/gssup/GSSUPPolicyValue:1.0";
    }

    public static GSSUPPolicyValue read(
            InputStream in)
    {
        GSSUPPolicyValue result = new GSSUPPolicyValue();
        result.mode = RequiresSupportsHelper.read(in);
        result.domain = in.read_string();
        return result;
    }

    public static void write(OutputStream out,
                             GSSUPPolicyValue s)
    {
        RequiresSupportsHelper.write(out, s.mode);
        out.write_string(s.domain);
    }
}
