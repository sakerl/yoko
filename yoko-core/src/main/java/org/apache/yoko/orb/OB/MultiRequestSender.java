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

import static org.apache.yoko.util.MinorCodes.MinorRequestNotSent;
import static org.apache.yoko.util.MinorCodes.describeBadInvOrder;
import static org.omg.CORBA.CompletionStatus.COMPLETED_NO;

import java.util.Vector;

import org.apache.yoko.util.MinorCodes;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.Request;
import org.omg.CORBA.WrongTransaction;

//
// The MultiRequestSender class. ORB::send_multiple_requests() and all
// related operations delegate to this class.
//
public class MultiRequestSender {
    // org.apache.yoko.orb.CORBA.Request needs access to private members
    public Vector deferredRequests_ = new Vector();

    // OBORB_impl creates MultiRequestSender
    public MultiRequestSender() {
    }

    // ----------------------------------------------------------------------
    // Convenience functions for use by org.apache.yoko.orb.CORBA.Request
    // ----------------------------------------------------------------------

    public synchronized boolean findDeferredRequest(
            Request request) {
        for (int index = 0; index < deferredRequests_.size(); index++)
            if (deferredRequests_.elementAt(index) == request)
                return true;

        return false;
    }

    public synchronized void addDeferredRequest(Request request) {
        deferredRequests_.addElement(request);
    }

    public synchronized void removeDeferredRequest(Request request) {
        int index;
        for (index = 0; index < deferredRequests_.size(); index++)
            if (deferredRequests_.elementAt(index) == request)
                break;

        if (index < deferredRequests_.size())
            deferredRequests_.removeElementAt(index);
    }

    // ----------------------------------------------------------------------
    // Public member implementations
    // ----------------------------------------------------------------------

    public void sendMultipleRequestsOneway(Request[] requests) {
        //
        // Send all requests oneway
        //
        for (int i = 0; i < requests.length; i++)
            requests[i].send_oneway();
    }

    public void sendMultipleRequestsDeferred(Request[] requests) {
        //
        // Send all requests deferred
        //
        for (int i = 0; i < requests.length; i++)
            requests[i].send_deferred();
    }

    public synchronized boolean pollNextResponse() {
        if (deferredRequests_.size() == 0)
            throw new BAD_INV_ORDER(describeBadInvOrder(MinorRequestNotSent),
                    MinorRequestNotSent,
                    COMPLETED_NO);

        //
        // Poll all deferred requests
        //
        boolean polled = false;
        for (int i = 0; i < deferredRequests_.size(); i++) {
            Request req = (Request) deferredRequests_
                    .elementAt(i);
            if (req.poll_response())
                polled = true;
        }

        return polled;
    }

    public synchronized Request getNextResponse()
            throws WrongTransaction {
        Request request = null;

        //
        // Try to find a deferred request that has completed already
        //
        for (int i = 0; i < deferredRequests_.size(); i++) {
            request = (Request) deferredRequests_.elementAt(i);
            if (((org.apache.yoko.orb.CORBA.Request) request)._OB_completed()) {
                deferredRequests_.removeElementAt(i);
                return request;
            }
        }

        //
        // No completed deferred request. Let's simply get the response of
        // the first request.
        //
        if (deferredRequests_.size() > 0) {
            request = (Request) deferredRequests_.elementAt(0);
            request.get_response();
            return request;
        }

        throw new BAD_INV_ORDER(describeBadInvOrder(MinorRequestNotSent),
                MinorRequestNotSent,
                COMPLETED_NO);
    }
}
