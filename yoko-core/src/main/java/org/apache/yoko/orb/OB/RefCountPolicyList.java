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

import org.omg.BiDirPolicy.BIDIRECTIONAL_POLICY_TYPE;
import org.omg.BiDirPolicy.BidirectionalPolicy;
import org.omg.BiDirPolicy.NORMAL;
import org.omg.CORBA.Policy;
import org.omg.Messaging.MAX_HOPS_POLICY_TYPE;
import org.omg.Messaging.MaxHopsPolicy;
import org.omg.Messaging.ORDER_TEMPORAL;
import org.omg.Messaging.PriorityRange;
import org.omg.Messaging.QUEUE_ORDER_POLICY_TYPE;
import org.omg.Messaging.QueueOrderPolicy;
import org.omg.Messaging.REBIND_POLICY_TYPE;
import org.omg.Messaging.RELATIVE_REQ_TIMEOUT_POLICY_TYPE;
import org.omg.Messaging.RELATIVE_RT_TIMEOUT_POLICY_TYPE;
import org.omg.Messaging.REPLY_END_TIME_POLICY_TYPE;
import org.omg.Messaging.REPLY_PRIORITY_POLICY_TYPE;
import org.omg.Messaging.REPLY_START_TIME_POLICY_TYPE;
import org.omg.Messaging.REQUEST_END_TIME_POLICY_TYPE;
import org.omg.Messaging.REQUEST_PRIORITY_POLICY_TYPE;
import org.omg.Messaging.REQUEST_START_TIME_POLICY_TYPE;
import org.omg.Messaging.ROUTE_NONE;
import org.omg.Messaging.ROUTING_POLICY_TYPE;
import org.omg.Messaging.RebindPolicy;
import org.omg.Messaging.RelativeRequestTimeoutPolicy;
import org.omg.Messaging.RelativeRoundtripTimeoutPolicy;
import org.omg.Messaging.ReplyEndTimePolicy;
import org.omg.Messaging.ReplyPriorityPolicy;
import org.omg.Messaging.ReplyStartTimePolicy;
import org.omg.Messaging.RequestEndTimePolicy;
import org.omg.Messaging.RequestPriorityPolicy;
import org.omg.Messaging.RequestStartTimePolicy;
import org.omg.Messaging.RoutingPolicy;
import org.omg.Messaging.RoutingTypeRange;
import org.omg.Messaging.SYNC_NONE;
import org.omg.Messaging.SYNC_SCOPE_POLICY_TYPE;
import org.omg.Messaging.SyncScopePolicy;
import org.omg.Messaging.TRANSPARENT;
import org.omg.TimeBase.UtcT;

public final class RefCountPolicyList {
    //
    // The immutable PolicyList
    //
    public Policy[] value;

    //
    // The immutable value of the retry policy
    //
    public RetryAttributes retry;

    //
    // The immutable value of the connect timeout policy
    //
    public int connectTimeout;

    //
    // The immutable value of the request timeout policy
    //
    public int requestTimeout;

    public int replyTimeout;

    //
    // The immutable value of the request start time policy
    //
    public UtcT requestStartTime;

    //
    // The immutable value of the request end time policy
    //
    public UtcT requestEndTime;

    //
    // The immutable value of the reply start time policy
    //
    public UtcT replyStartTime;

    //
    // The immutable value of the reply end time policy
    //
    public UtcT replyEndTime;

    //
    // The immutable value of the relative request timeout policy
    //
    public long relativeRequestTimeout;

    //
    // The immutable value of the relative round trip timeout policy
    //
    public long relativeRoundTripTimeout;

    //
    // The immutable value of the rebind mode policy
    //
    public short rebindMode;

    //
    // The immutable value of the sync scope policy
    //
    public short syncScope;

    //
    // The immutable value of the location transparency policy
    //
    public short locationTransparency;

    //
    // the immutable value of the bidir policy
    //
    public short biDirMode;

    //
    // The immutable value of the InterceptorPolicy, or true if there
    // is no such policy
    //
    public boolean interceptor;

    //
    // The immutable value of the LocateRequestPolicy, or false if there
    // is no such policy
    //
    public boolean locateRequest;

    //
    // the immutable value of the request priority policy
    //
    public PriorityRange requestPriority;

    //
    // the immutable value of the reply priority policy
    //
    public PriorityRange replyPriority;

    //
    // the immutable value of the routing policy
    //
    public RoutingTypeRange routingRange;

    //
    // the immutable value of the max hops policy
    //
    public short maxHops;

    //
    // the immutable value of the queue order policy
    //
    public short queueOrder;

    // ----------------------------------------------------------------------
    // RefCountPolicyList private and protected members
    // ----------------------------------------------------------------------

    private static RetryAttributes getRetry(Policy[] policies) {
        RetryAttributes attributes = new RetryAttributes();
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == RETRY_POLICY_ID.value) {
                RetryPolicy policy = (RetryPolicy) policies[i];

                attributes.mode = policy.retry_mode();
                attributes.interval = policy.retry_interval();
                attributes.max = policy.retry_max();
                attributes.remote = policy.retry_remote();

                return attributes;
            }
        }
        attributes.mode = RETRY_STRICT.value;
        attributes.interval = 0;
        attributes.max = 1;
        attributes.remote = false;

        return attributes;
    }

    private static int getConnectTimeout(Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == CONNECT_TIMEOUT_POLICY_ID.value) {
                ConnectTimeoutPolicy policy = (ConnectTimeoutPolicy) policies[i];
                return policy.value();
            }
        }

        //
        // Fall back to TimeoutPolicy
        //
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == TIMEOUT_POLICY_ID.value) {
                TimeoutPolicy policy = (TimeoutPolicy) policies[i];
                return policy.value();
            }
        }

        return -1;
    }

    //
    // TODO: This needs to be replaced with the new messaging timeout
    // policies below.
    //
    private static int getRequestTimeout(Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == REQUEST_TIMEOUT_POLICY_ID.value) {
                RequestTimeoutPolicy policy = (RequestTimeoutPolicy) policies[i];
                return policy.value();
            }
        }

        //
        // Fall back to TimeoutPolicy
        //
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == TIMEOUT_POLICY_ID.value) {
                TimeoutPolicy policy = (TimeoutPolicy) policies[i];
                return policy.value();
            }
        }

        return -1;
    }

    private static int getReplyTimeout(Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == REPLY_TIMEOUT_POLICY_ID.value) {
                ReplyTimeoutPolicy policy = (ReplyTimeoutPolicy) policies[i];
                return policy.value();
            }
        }

        //
        // Fall back to TimeoutPolicy
        //
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == TIMEOUT_POLICY_ID.value) {
                TimeoutPolicy policy = (TimeoutPolicy) policies[i];
                return policy.value();
            }
        }

        return -1;
    }

    private UtcT getRequestStartTime(
            Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == REQUEST_START_TIME_POLICY_TYPE.value) {
                RequestStartTimePolicy policy = (RequestStartTimePolicy) policies[i];
                return policy.start_time();
            }
        }

        return TimeHelper.utcMin();
    }

    private UtcT getRequestEndTime(
            Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == REQUEST_END_TIME_POLICY_TYPE.value) {
                RequestEndTimePolicy policy = (RequestEndTimePolicy) policies[i];
                return policy.end_time();
            }
        }

        return TimeHelper.utcMin();
    }

    private UtcT getReplyStartTime(
            Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == REPLY_START_TIME_POLICY_TYPE.value) {
                ReplyStartTimePolicy policy = (ReplyStartTimePolicy) policies[i];
                return policy.start_time();
            }
        }

        return TimeHelper.utcMin();
    }

    private UtcT getReplyEndTime(
            org.omg.CORBA.Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == REPLY_END_TIME_POLICY_TYPE.value) {
                ReplyEndTimePolicy policy = (ReplyEndTimePolicy) policies[i];
                return policy.end_time();
            }
        }

        return TimeHelper.utcMin();
    }

    private long getRelativeRequestTimeout(Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == RELATIVE_REQ_TIMEOUT_POLICY_TYPE.value) {
                RelativeRequestTimeoutPolicy policy = (RelativeRequestTimeoutPolicy) policies[i];
                return policy.relative_expiry();
            }
        }

        return 0;
    }

    private long getRelativeRoundTripTimeout(Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == RELATIVE_RT_TIMEOUT_POLICY_TYPE.value) {
                RelativeRoundtripTimeoutPolicy policy = (RelativeRoundtripTimeoutPolicy) policies[i];
                return policy.relative_expiry();
            }
        }

        return 0;
    }

    private static short getRebindMode(Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == REBIND_POLICY_TYPE.value) {
                RebindPolicy policy = (RebindPolicy) policies[i];
                return policy.rebind_mode();
            }
        }

        return TRANSPARENT.value;
    }

    private static short getSyncScope(Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == SYNC_SCOPE_POLICY_TYPE.value) {
                SyncScopePolicy policy = (SyncScopePolicy) policies[i];
                return policy.synchronization();
            }
        }

        return SYNC_NONE.value;
    }

    private static short getLocationTransparency(Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == LOCATION_TRANSPARENCY_POLICY_ID.value) {
                LocationTransparencyPolicy policy = (LocationTransparencyPolicy) policies[i];
                return policy.value();
            }
        }

        return LOCATION_TRANSPARENCY_RELAXED.value;
    }

    private static short getBiDirMode(Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == BIDIRECTIONAL_POLICY_TYPE.value) {
                BidirectionalPolicy policy = (BidirectionalPolicy) policies[i];

                return policy.value();
            }
        }

        return NORMAL.value;
    }

    private static boolean getInterceptor(Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == INTERCEPTOR_POLICY_ID.value) {
                InterceptorPolicy policy = (InterceptorPolicy) policies[i];
                return policy.value();
            }
        }

        return true;
    }

    private static boolean getLocateRequest(Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == LOCATE_REQUEST_POLICY_ID.value) {
                LocateRequestPolicy policy = (LocateRequestPolicy) policies[i];
                return policy.value();
            }
        }

        return false;
    }

    public static PriorityRange getRequestPriority(
            Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == REQUEST_PRIORITY_POLICY_TYPE.value) {
                RequestPriorityPolicy policy = (RequestPriorityPolicy) policies[i];
                return policy.priority_range();
            }
        }

        PriorityRange range = new PriorityRange();
        range.min = 0;
        range.max = 0;

        return range;
    }

    public static PriorityRange getReplyPriority(
            Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == REPLY_PRIORITY_POLICY_TYPE.value) {
                ReplyPriorityPolicy policy = (ReplyPriorityPolicy) policies[i];
                return policy.priority_range();
            }
        }

        PriorityRange range = new PriorityRange();
        range.min = 0;
        range.max = 0;

        return range;
    }

    public static RoutingTypeRange getRoutingRange(
            Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == ROUTING_POLICY_TYPE.value) {
                RoutingPolicy policy = (RoutingPolicy) policies[i];
                return policy.routing_range();
            }
        }

        RoutingTypeRange range = new RoutingTypeRange();
        range.min = ROUTE_NONE.value;
        range.max = ROUTE_NONE.value;

        return range;
    }

    public static short getMaxHops(Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == MAX_HOPS_POLICY_TYPE.value) {
                MaxHopsPolicy policy = (MaxHopsPolicy) policies[i];
                return policy.max_hops();
            }
        }

        return Short.MAX_VALUE;
    }

    public static short getQueueOrder(Policy[] policies) {
        for (int i = 0; i < policies.length; i++) {
            if (policies[i].policy_type() == QUEUE_ORDER_POLICY_TYPE.value) {
                QueueOrderPolicy policy = (QueueOrderPolicy) policies[i];
                return policy.allowed_orders();
            }
        }

        return ORDER_TEMPORAL.value;
    }

    // ----------------------------------------------------------------------
    // RefCountPolicyList public members
    // ----------------------------------------------------------------------

    public RefCountPolicyList(Policy[] v) {
        value = v;
        retry = getRetry(v);
        connectTimeout = getConnectTimeout(v);
        requestTimeout = getRequestTimeout(v);
        requestStartTime = getRequestStartTime(v);
        requestEndTime = getRequestEndTime(v);
        replyTimeout = getReplyTimeout(v);
        replyStartTime = getReplyStartTime(v);
        replyEndTime = getReplyEndTime(v);
        relativeRequestTimeout = getRelativeRequestTimeout(v);
        relativeRoundTripTimeout = getRelativeRoundTripTimeout(v);
        rebindMode = getRebindMode(v);
        syncScope = getSyncScope(v);
        locationTransparency = getLocationTransparency(v);
        biDirMode = getBiDirMode(v);
        interceptor = getInterceptor(v);
        locateRequest = getLocateRequest(v);
        requestPriority = getRequestPriority(v);
        replyPriority = getReplyPriority(v);
        routingRange = getRoutingRange(v);
        maxHops = getMaxHops(v);
        queueOrder = getQueueOrder(v);
    }
}
