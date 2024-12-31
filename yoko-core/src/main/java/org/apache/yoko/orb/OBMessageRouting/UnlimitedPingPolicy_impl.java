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
package org.apache.yoko.orb.OBMessageRouting;

import org.omg.CORBA.Policy;
import org.omg.MessageRouting.UNLIMITED_PING_POLICY_TYPE;
import org.omg.MessageRouting.UnlimitedPing;

public class UnlimitedPingPolicy_impl extends
        UnlimitedPing {
    public UnlimitedPingPolicy_impl() {
    }

    public UnlimitedPingPolicy_impl(short maxBackoffs, float backoffFactor,
            int bis) {
        max_backoffs = maxBackoffs;
        backoff_factor = backoffFactor;
        base_interval_seconds = bis;
    }

    public int policy_type() {
        return UNLIMITED_PING_POLICY_TYPE.value;
    }

    public Policy copy() {
        return null;
    }

    public void destroy() {
    }
}
