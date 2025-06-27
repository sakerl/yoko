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
package org.apache.yoko.orb.rofl;

import org.apache.yoko.util.rofl.Rofl;
import org.apache.yoko.util.rofl.RoflThreadLocal;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.ORB;
import org.omg.IOP.ServiceContext;
import org.omg.PortableInterceptor.ForwardRequest;
import org.omg.PortableInterceptor.InvalidSlot;
import org.omg.PortableInterceptor.ServerRequestInfo;
import org.omg.PortableInterceptor.ServerRequestInterceptor;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.apache.yoko.util.MinorCodes.MinorInvalidServiceContextId;
import static org.apache.yoko.util.rofl.Rofl.RemoteOrb.IBM;

public class RoflServerInterceptor extends LocalObject implements ServerRequestInterceptor {
    private static final String NAME = RoflServerInterceptor.class.getName();
    private final int slotId;

    public RoflServerInterceptor(int slotId) {
        this.slotId = slotId;
    }
    public void receive_request_service_contexts(ServerRequestInfo ri) throws ForwardRequest {
        RoflThreadLocal.reset();
        saveRoflToSlot(readRofl(ri), ri);
    }
    public void receive_request(ServerRequestInfo ri) {}
    public void send_reply(ServerRequestInfo ri) { RoflThreadLocal.push(loadRoflFromSlot(ri)); }
    public void send_exception(ServerRequestInfo ri) { RoflThreadLocal.push(loadRoflFromSlot(ri)); }
    public void send_other(ServerRequestInfo ri) { RoflThreadLocal.push(loadRoflFromSlot(ri)); }
    public String name() { return NAME; }
    public void destroy() {}
    private void readObject(ObjectInputStream ios) throws IOException { throw new NotSerializableException(NAME);}
    private void writeObject(ObjectOutputStream oos) throws IOException { throw new NotSerializableException(NAME); }

    private void saveRoflToSlot(Rofl rofl, ServerRequestInfo ri) {
        Any any = ORB.init().create_any();
        any.insert_Value(rofl);
        try {
            ri.set_slot(slotId, any);
        } catch (InvalidSlot e) {
            throw (INTERNAL)(new INTERNAL(e.getMessage())).initCause(e);
        }
    }

    private Rofl loadRoflFromSlot(ServerRequestInfo ri) {
        final Rofl rofl;
        try {
            Any any = ri.get_slot(slotId);
            rofl = (Rofl) any.extract_Value();
        } catch (InvalidSlot e) {
            throw (INTERNAL)(new INTERNAL(e.getMessage())).initCause(e);
        }
        return rofl;
    }

    private static Rofl readRofl(ServerRequestInfo ri) {
        Rofl rofl = Rofl.NONE;
        try {
            ServiceContext sc = ri.get_request_service_context(IBM.serviceContextId);
            rofl = IBM.createRofl(sc.context_data);

        } catch (BAD_PARAM e) {
            if (e.minor != MinorInvalidServiceContextId) throw e;
        }
        return rofl;
    }
}
