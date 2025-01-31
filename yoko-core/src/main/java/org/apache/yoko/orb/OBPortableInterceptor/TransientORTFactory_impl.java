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

import org.apache.yoko.orb.OB.ORBInstance;
import org.omg.CORBA.portable.ValueFactory;
import org.omg.CORBA_2_3.portable.InputStream;

import java.io.Serializable;

//
// The Transient ObjectReferenceTemplate
//
final public class TransientORTFactory_impl implements
        ValueFactory {
    private ORBInstance orbInstance_;

    // ------------------------------------------------------------------
    // Public member functions
    // ------------------------------------------------------------------

    public TransientORTFactory_impl(
            ORBInstance orbInstance) {
        orbInstance_ = orbInstance;
    }

    public Serializable read_value(
            InputStream in) {
        return in.read_value(new TransientORT_impl(orbInstance_));
    }
}
