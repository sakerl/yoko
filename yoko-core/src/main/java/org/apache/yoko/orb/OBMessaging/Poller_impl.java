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
package org.apache.yoko.orb.OBMessaging;

import org.apache.yoko.orb.OB.Downcall;
import org.apache.yoko.orb.OB.ORBInstance;
import org.apache.yoko.orb.OB.OrbAsyncHandler;
import org.apache.yoko.orb.OBCORBA.PollableSet_impl;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.Pollable;
import org.omg.CORBA.PollableSet;
import org.omg.Messaging.Poller;
import org.omg.Messaging.ReplyHandler;

import static org.apache.yoko.util.Assert.ensure;

public class Poller_impl implements Pollable,
        Poller {
    //
    // ORBInstance this poller is bound to
    //
    protected ORBInstance orbInstance_ = null;

    //
    // operation target
    //
    protected org.omg.CORBA.Object objectTarget_ = null;

    //
    // Operation name
    //
    protected String operationName_ = null;

    //
    // Associated ReplyHandler
    //
    protected ReplyHandler replyHandler_ = null;

    // ----------------------------------------------------------------
    // Standard IDL to Java mapping
    // ----------------------------------------------------------------
    public String[] _truncatable_ids() {
        throw new NO_IMPLEMENT();
    }

    // ----------------------------------------------------------------
    // From org.omg.CORBA.Pollable
    // ----------------------------------------------------------------

    //
    // IDL:omg.org/CORBA/Pollable/is_ready:1.0
    //
    public boolean is_ready(int timeout) {
        ensure(orbInstance_ != null);

        OrbAsyncHandler handler = orbInstance_
                .getAsyncHandler();
        return handler.is_ready(this, timeout);
    }

    //
    // IDL:omg.org/CORBA/Pollable/create_pollable_set:1.0
    //
    public PollableSet create_pollable_set() {
        ensure(orbInstance_ != null);

        return new PollableSet_impl();
    }

    // ----------------------------------------------------------------
    // From org.omg.Messaging.Poller
    // ----------------------------------------------------------------

    //
    // IDL:omg.org/Messaging/Poller/operation_target:1.0
    //
    public org.omg.CORBA.Object operation_target() {
        return objectTarget_;
    }

    //
    // IDL:omg.org/Messaging/Poller/operation_name:1.0
    //
    public String operation_name() {
        return operationName_;
    }

    //
    // IDL:omg.org/Messaging/Poller/associated_handler:1.0
    //
    public ReplyHandler associated_handler() {
        return replyHandler_;
    }

    public void associated_handler(ReplyHandler handler) {
        replyHandler_ = handler;
    }

    //
    // IDL:omg.org/Messasging/Poller/is_from_poller:1.0
    //
    public boolean is_from_poller() {
        return false;
    }

    // ----------------------------------------------------------------
    // Proprietary methods used by the ORB
    // ----------------------------------------------------------------

    //
    // set the internal ORBInstance handle
    // 
    public void _OB_ORBInstance(ORBInstance orb) {
        orbInstance_ = orb;
    }

    //
    // set the object target reference in this poller
    //
    public void _OB_Object(org.omg.CORBA.Object obj) {
        objectTarget_ = obj;
    }

    //
    // get the response of this request
    //
    public Downcall _OB_poll_response() {
        OrbAsyncHandler handler = orbInstance_
                .getAsyncHandler();
        return handler.poll_response(this);
    }
}
