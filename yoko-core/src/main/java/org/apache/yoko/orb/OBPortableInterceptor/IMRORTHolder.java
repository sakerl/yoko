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

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

//
// IDL:orb.yoko.apache.org/OBPortableInterceptor/IMRORT:1.0
//
final public class IMRORTHolder implements Streamable
{
    public IMRORT value;

    public
    IMRORTHolder()
    {
    }

    public
    IMRORTHolder(IMRORT initial)
    {
        value = initial;
    }

    public void
    _read(InputStream in)
    {
        value = IMRORTHelper.read(in);
    }

    public void
    _write(OutputStream out)
    {
        IMRORTHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode
    _type()
    {
        return IMRORTHelper.type();
    }
}
