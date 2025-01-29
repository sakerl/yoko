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

//
// IDL:orb.yoko.apache.org/OBPortableInterceptor/PersistentORT:1.0
//

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.StreamableValue;
import org.omg.IOP.IOR;
import org.omg.IOP.IORHelper;
import org.omg.PortableInterceptor.AdapterNameHelper;
import org.omg.PortableInterceptor.ORBIdHelper;
import org.omg.PortableInterceptor.ServerIdHelper;

/***/

public abstract class PersistentORT implements StreamableValue,
                                               ObjectReferenceTemplate
{
    //
    // IDL:orb.yoko.apache.org/OBPortableInterceptor/PersistentORT/the_server_id:1.0
    //
    /***/

    protected String the_server_id;

    //
    // IDL:orb.yoko.apache.org/OBPortableInterceptor/PersistentORT/the_orb_id:1.0
    //
    /***/

    protected String the_orb_id;

    //
    // IDL:orb.yoko.apache.org/OBPortableInterceptor/PersistentORT/the_adapter_name:1.0
    //
    /***/

    protected String[] the_adapter_name;

    //
    // IDL:orb.yoko.apache.org/OBPortableInterceptor/PersistentORT/the_ior_template:1.0
    //
    /***/

    protected IOR the_ior_template;

    private static String[] _OB_truncatableIds_ =
    {
        PersistentORTHelper.id()
    };

    public String[]
    _truncatable_ids()
    {
        return _OB_truncatableIds_;
    }

    public void
    _read(InputStream in)
    {
        the_server_id = ServerIdHelper.read(in);
        the_orb_id = ORBIdHelper.read(in);
        the_adapter_name = AdapterNameHelper.read(in);
        the_ior_template = IORHelper.read(in);
    }

    public void
    _write(OutputStream out)
    {
        ServerIdHelper.write(out, the_server_id);
        ORBIdHelper.write(out, the_orb_id);
        AdapterNameHelper.write(out, the_adapter_name);
        IORHelper.write(out, the_ior_template);
    }

    public TypeCode
    _type()
    {
        return PersistentORTHelper.type();
    }
}
