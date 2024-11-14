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

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.Streamable;

public final class ParameterDesc {
    public Streamable param; // The parameter

    public TypeCode tc; // The typecode of the parameter

    public int mode; // CORBA::ParameterMode

    public ParameterDesc(Streamable param,
            TypeCode tc, int mode) {
        this.param = param;
        this.tc = tc;
        this.mode = mode;
    }
}
