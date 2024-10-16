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
package org.apache.yoko.orb.CORBA;

import org.apache.yoko.orb.BiDirPolicy.BidirectionalPolicy_impl;
import org.apache.yoko.orb.Messaging.MaxHopsPolicy_impl;
import org.apache.yoko.orb.Messaging.QueueOrderPolicy_impl;
import org.apache.yoko.orb.Messaging.RebindPolicy_impl;
import org.apache.yoko.orb.Messaging.RelativeRequestTimeoutPolicy_impl;
import org.apache.yoko.orb.Messaging.RelativeRoundtripTimeoutPolicy_impl;
import org.apache.yoko.orb.Messaging.ReplyEndTimePolicy_impl;
import org.apache.yoko.orb.Messaging.ReplyPriorityPolicy_impl;
import org.apache.yoko.orb.Messaging.ReplyStartTimePolicy_impl;
import org.apache.yoko.orb.Messaging.RequestEndTimePolicy_impl;
import org.apache.yoko.orb.Messaging.RequestPriorityPolicy_impl;
import org.apache.yoko.orb.Messaging.RequestStartTimePolicy_impl;
import org.apache.yoko.orb.Messaging.RoutingPolicy_impl;
import org.apache.yoko.orb.Messaging.SyncScopePolicy_impl;
import org.apache.yoko.orb.OB.CONNECTION_REUSE_POLICY_ID;
import org.apache.yoko.orb.OB.CONNECT_TIMEOUT_POLICY_ID;
import org.apache.yoko.orb.OB.ConnectTimeoutPolicy_impl;
import org.apache.yoko.orb.OB.ConnectionReusePolicy_impl;
import org.apache.yoko.orb.OB.INTERCEPTOR_POLICY_ID;
import org.apache.yoko.orb.OB.InterceptorPolicy_impl;
import org.apache.yoko.orb.OB.LOCATE_REQUEST_POLICY_ID;
import org.apache.yoko.orb.OB.LOCATION_TRANSPARENCY_POLICY_ID;
import org.apache.yoko.orb.OB.LocateRequestPolicy_impl;
import org.apache.yoko.orb.OB.LocationTransparencyPolicy_impl;
import org.apache.yoko.orb.OB.PROTOCOL_POLICY_ID;
import org.apache.yoko.orb.OB.ProtocolPolicy_impl;
import org.apache.yoko.orb.OB.REQUEST_TIMEOUT_POLICY_ID;
import org.apache.yoko.orb.OB.RETRY_ALWAYS;
import org.apache.yoko.orb.OB.RETRY_POLICY_ID;
import org.apache.yoko.orb.OB.RequestTimeoutPolicy_impl;
import org.apache.yoko.orb.OB.RetryAttributes;
import org.apache.yoko.orb.OB.RetryAttributesHelper;
import org.apache.yoko.orb.OB.RetryPolicy_impl;
import org.apache.yoko.orb.OB.TIMEOUT_POLICY_ID;
import org.apache.yoko.orb.OB.TimeoutPolicy_impl;
import org.apache.yoko.orb.OB.ZERO_PORT_POLICY_ID;
import org.apache.yoko.orb.OB.ZeroPortPolicy_impl;
import org.omg.BiDirPolicy.BIDIRECTIONAL_POLICY_TYPE;
import org.omg.BiDirPolicy.BidirectionalPolicyValueHelper;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.BAD_POLICY;
import org.omg.CORBA.BAD_POLICY_TYPE;
import org.omg.CORBA.BAD_POLICY_VALUE;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Policy;
import org.omg.CORBA.PolicyError;
import org.omg.Messaging.MAX_HOPS_POLICY_TYPE;
import org.omg.Messaging.ORDER_ANY;
import org.omg.Messaging.ORDER_DEADLINE;
import org.omg.Messaging.OrderingHelper;
import org.omg.Messaging.PriorityRange;
import org.omg.Messaging.PriorityRangeHelper;
import org.omg.Messaging.QUEUE_ORDER_POLICY_TYPE;
import org.omg.Messaging.REBIND_POLICY_TYPE;
import org.omg.Messaging.RELATIVE_REQ_TIMEOUT_POLICY_TYPE;
import org.omg.Messaging.RELATIVE_RT_TIMEOUT_POLICY_TYPE;
import org.omg.Messaging.REPLY_END_TIME_POLICY_TYPE;
import org.omg.Messaging.REPLY_PRIORITY_POLICY_TYPE;
import org.omg.Messaging.REPLY_START_TIME_POLICY_TYPE;
import org.omg.Messaging.REQUEST_END_TIME_POLICY_TYPE;
import org.omg.Messaging.REQUEST_PRIORITY_POLICY_TYPE;
import org.omg.Messaging.REQUEST_START_TIME_POLICY_TYPE;
import org.omg.Messaging.ROUTING_POLICY_TYPE;
import org.omg.Messaging.RoutingTypeRange;
import org.omg.Messaging.RoutingTypeRangeHelper;
import org.omg.Messaging.SYNC_SCOPE_POLICY_TYPE;
import org.omg.PortableInterceptor.PolicyFactory;
import org.omg.TimeBase.UtcTHelper;

final public class ORBPolicyFactory_impl extends LocalObject
        implements PolicyFactory {
    // ------------------------------------------------------------------
    // Standard IDL to Java Mapping
    // ------------------------------------------------------------------

    public Policy create_policy(int type, Any any)
            throws PolicyError {
        try {
            switch (type) {
            case CONNECTION_REUSE_POLICY_ID.value: {
                boolean b = any.extract_boolean();
                return new ConnectionReusePolicy_impl(b);
            }
            case ZERO_PORT_POLICY_ID.value: {
                boolean b = any.extract_boolean();
                return new ZeroPortPolicy_impl(b);
            }

            case PROTOCOL_POLICY_ID.value: {
                String[] seq = org.apache.yoko.orb.OCI.PluginIdSeqHelper
                        .extract(any);
                return new ProtocolPolicy_impl(seq);
            }

            case RETRY_POLICY_ID.value: {
                try {
                    short v = any.extract_short();
                    if (v > RETRY_ALWAYS.value)
                        throw new PolicyError(
                                BAD_POLICY_VALUE.value);
                    return new RetryPolicy_impl(v, 0, 1,
                            false);
                } catch (BAD_OPERATION ex) {
                }
                RetryAttributes attr = RetryAttributesHelper
                        .extract(any);
                return new RetryPolicy_impl(attr.mode,
                        attr.interval, attr.max, attr.remote);
            }

            case TIMEOUT_POLICY_ID.value: {
                int t = any.extract_ulong();
                return new TimeoutPolicy_impl(t);
            }

            case LOCATION_TRANSPARENCY_POLICY_ID.value: {
                short v = any.extract_short();
                return new LocationTransparencyPolicy_impl(
                        v);
            }

            case REQUEST_START_TIME_POLICY_TYPE.value: {
                org.omg.TimeBase.UtcT v = UtcTHelper
                        .extract(any);
                return new RequestStartTimePolicy_impl(
                        v);
            }

            case REQUEST_END_TIME_POLICY_TYPE.value: {
                org.omg.TimeBase.UtcT v = UtcTHelper
                        .extract(any);
                return new RequestEndTimePolicy_impl(
                        v);
            }

            case REPLY_START_TIME_POLICY_TYPE.value: {
                org.omg.TimeBase.UtcT v = UtcTHelper
                        .extract(any);
                return new ReplyStartTimePolicy_impl(
                        v);
            }

            case REPLY_END_TIME_POLICY_TYPE.value: {
                org.omg.TimeBase.UtcT v = UtcTHelper
                        .extract(any);
                return new ReplyEndTimePolicy_impl(
                        v);
            }

            case RELATIVE_REQ_TIMEOUT_POLICY_TYPE.value: {
                long v = any.extract_long();
                return new RelativeRequestTimeoutPolicy_impl(
                        v);
            }

            case RELATIVE_RT_TIMEOUT_POLICY_TYPE.value: {
                long v = any.extract_long();
                return new RelativeRoundtripTimeoutPolicy_impl(
                        v);
            }

            case REBIND_POLICY_TYPE.value: {
                short v = any.extract_short();
                return new RebindPolicy_impl(v);
            }

            case SYNC_SCOPE_POLICY_TYPE.value: {
                short v = any.extract_short();
                return new SyncScopePolicy_impl(v);
            }

            case INTERCEPTOR_POLICY_ID.value: {
                boolean v = any.extract_boolean();
                return new InterceptorPolicy_impl(v);
            }

            case CONNECT_TIMEOUT_POLICY_ID.value: {
                int t = any.extract_ulong();
                return new ConnectTimeoutPolicy_impl(t);
            }

            case REQUEST_TIMEOUT_POLICY_ID.value: {
                int t = any.extract_ulong();
                return new RequestTimeoutPolicy_impl(t);
            }

            case LOCATE_REQUEST_POLICY_ID.value: {
                boolean b = any.extract_boolean();
                return new LocateRequestPolicy_impl(b);
            }

            case BIDIRECTIONAL_POLICY_TYPE.value: {
                short v = BidirectionalPolicyValueHelper
                        .extract(any);
                return new BidirectionalPolicy_impl(
                        v);
            }
            case REQUEST_PRIORITY_POLICY_TYPE.value: {
                org.omg.Messaging.PriorityRange v = PriorityRangeHelper
                        .extract(any);
                if (v.min > v.max)
                    throw new PolicyError(
                            BAD_POLICY_VALUE.value);
                return new RequestPriorityPolicy_impl(
                        v);
            }
            case REPLY_PRIORITY_POLICY_TYPE.value: {
                PriorityRange v = PriorityRangeHelper
                        .extract(any);
                if (v.min > v.max)
                    throw new PolicyError(
                            BAD_POLICY_VALUE.value);
                return new ReplyPriorityPolicy_impl(
                        v);
            }
            case ROUTING_POLICY_TYPE.value: {
                RoutingTypeRange v = RoutingTypeRangeHelper
                        .extract(any);
                if (v.min > v.max)
                    throw new PolicyError(
                            BAD_POLICY_VALUE.value);
                return new RoutingPolicy_impl(v);
            }
            case MAX_HOPS_POLICY_TYPE.value: {
                short v = any.extract_ushort();
                return new MaxHopsPolicy_impl(v);
            }
            case QUEUE_ORDER_POLICY_TYPE.value: {
                short v = OrderingHelper.extract(any);
                if (v < ORDER_ANY.value
                        || v > ORDER_DEADLINE.value)
                    throw new PolicyError(
                            BAD_POLICY_VALUE.value);
                return new QueueOrderPolicy_impl(
                        v);
            }

            } // end of switch
        } catch (BAD_OPERATION ex) {
            //
            // Any extraction failure
            //
            throw new PolicyError(
                    BAD_POLICY_TYPE.value);
        }

        throw new PolicyError(BAD_POLICY.value);
    }
}
