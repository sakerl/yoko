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
package org.apache.yoko.orb.OBCORBA;

import org.apache.yoko.util.Assert;
import org.omg.CORBA.DIIPollable;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.NO_RESPONSE;
import org.omg.CORBA.Pollable;
import org.omg.CORBA.PollableSet;
import org.omg.CORBA.PollableSetPackage.NoPossiblePollable;
import org.omg.CORBA.PollableSetPackage.UnknownPollable;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.TIMEOUT;

import java.util.LinkedList;
import java.util.ListIterator;

import static java.lang.System.currentTimeMillis;

public class PollableSet_impl extends LocalObject implements
        PollableSet {
    //
    // List of pollable objects in this set
    //
    protected LinkedList pollableList_ = new LinkedList();

    //
    // Constructor
    //
    public PollableSet_impl() {
    }

    //
    // IDL:omg.org/CORBA/PollableSet/create_dii_pollable:1.0
    //
    public DIIPollable create_dii_pollable() {
        throw new NO_IMPLEMENT();
    }

    //
    // IDL:omg.org/CORBA/PollableSet/add_pollable:1.0
    //
    public void add_pollable(Pollable potential) {
        Assert.ensure(potential != null);
        pollableList_.addLast(potential);
    }

    //
    // IDL:omg.org/CORBA/PollableSet/get_ready_pollable:1.0
    //
    public Pollable get_ready_pollable(int timeout)
            throws NoPossiblePollable,
            SystemException {
        if (pollableList_.size() == 0)
            throw new NoPossiblePollable();

        //
        // try to return a pollable item in the timeout specified
        //
        while (true) {
            //
            // starting time of query
            //
            long start_time = currentTimeMillis();

            //
            // are there any pollables ready?
            //
            ListIterator iter = pollableList_.listIterator(0);
            while (iter.hasNext()) {
                Pollable pollable = (Pollable) iter
                        .next();

                if (pollable.is_ready(0)) {
                    iter.remove();
                    return pollable;
                }
            }

            //
            // none are ready yet so we need to block on the
            // OrbAsyncHandler until a new response is received or throw
            // a NO_RESPONSE if there is no timeout specified
            //
            if (timeout == 0)
                throw new NO_RESPONSE();

            //
            // Yield for now to give another thread a timeslice
            //
            Thread.yield();

            //
            // just return if timeout is INFINITE
            //
            if (timeout == -1)
                continue;

            //
            // the ending time of the query
            //
            long end_time = currentTimeMillis();

            //
            // subtract difference in time from the timeout value
            //
            long diff_time = end_time - start_time;
            if (diff_time > timeout)
                timeout = 0;
            else
                timeout -= diff_time;

            //
            // check if all the time has now expired
            //
            if (timeout == 0)
                throw new TIMEOUT();
        }
    }

    //
    // IDL:omg.org/CORBA/PollableSet/remove:1.0
    //
    public void remove(Pollable potential)
            throws UnknownPollable {
        Assert.ensure(potential != null);

        //
        // iterate the list, looking for a match
        //
        ListIterator iter = pollableList_.listIterator(0);
        while (iter.hasNext()) {
            if (potential == iter.next()) {
                iter.remove();
                return;
            }
        }

        //
        // never found the item
        //
        throw new UnknownPollable();
    }

    //
    // IDL:omg.org/CORBA/PollableSet/number_left:1.0
    //
    public short number_left() {
        return (short) pollableList_.size();
    }
}
