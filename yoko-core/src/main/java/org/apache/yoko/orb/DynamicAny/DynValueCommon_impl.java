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
package org.apache.yoko.orb.DynamicAny;

import org.apache.yoko.orb.OB.ORBInstance;
import org.omg.CORBA.TypeCode;
import org.omg.DynamicAny.DynAnyFactory;
import org.omg.DynamicAny.DynValueCommon;

abstract class DynValueCommon_impl extends DynAny_impl implements
        DynValueCommon {
    private boolean isNull_;

    DynValueCommon_impl(DynAnyFactory factory,
            ORBInstance orbInstance,
            TypeCode type) {
        super(factory, orbInstance, type);
    }

    // ------------------------------------------------------------------
    // Private and protected member implementations
    // ------------------------------------------------------------------

    protected abstract void createComponents();

    protected abstract void destroyComponents();

    // ------------------------------------------------------------------
    // Standard IDL to Java Mapping
    // ------------------------------------------------------------------

    public synchronized boolean is_null() {
        return isNull_;
    }

    public synchronized void set_to_null() {
        if (!isNull_) {
            isNull_ = true;
            destroyComponents();
        }
    }

    public synchronized void set_to_value() {
        if (isNull_) {
            isNull_ = false;
            createComponents();
        }
    }
}
