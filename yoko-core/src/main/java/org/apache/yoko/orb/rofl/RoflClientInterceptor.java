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
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.LocalObject;
import org.omg.IOP.TaggedComponent;
import org.omg.PortableInterceptor.ClientRequestInfo;
import org.omg.PortableInterceptor.ClientRequestInterceptor;
import org.omg.PortableInterceptor.ForwardRequest;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.apache.yoko.util.MinorCodes.MinorInvalidComponentId;
import static org.apache.yoko.util.rofl.Rofl.RemoteOrb.IBM;

public class RoflClientInterceptor extends LocalObject implements ClientRequestInterceptor {
    private static final String NAME = RoflClientInterceptor.class.getName();

    @Override
    public void send_request(ClientRequestInfo ri) throws ForwardRequest {
        Rofl rofl = Rofl.NONE;
        try {
            TaggedComponent tc = ri.get_effective_component(IBM.tagComponentId);
            rofl = IBM.createRofl(tc.component_data);
        } catch (BAD_PARAM e) {
            if (e.minor != MinorInvalidComponentId) {
                throw e;
            }
        }
        RoflThreadLocal.push(rofl);
    }

    public void send_poll(ClientRequestInfo ri) {}
    public void receive_reply(ClientRequestInfo ri) {
        RoflThreadLocal.pop();
    }
    public void receive_exception(ClientRequestInfo ri) {
        RoflThreadLocal.pop();
    }
    public void receive_other(ClientRequestInfo ri) {
        RoflThreadLocal.pop();
    }
    public String name() {
        return NAME;
    }
    public void destroy() {}
    private void readObject(ObjectInputStream ios) throws IOException { throw new NotSerializableException(NAME); }
    private void writeObject(ObjectOutputStream oos) throws IOException { throw new NotSerializableException(NAME); }
}
