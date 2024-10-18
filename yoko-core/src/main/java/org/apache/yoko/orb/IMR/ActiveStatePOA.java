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
package org.apache.yoko.orb.IMR;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.PortableInterceptor.ObjectReferenceTemplate;
import org.omg.PortableInterceptor.ObjectReferenceTemplateHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;

//
// IDL:orb.yoko.apache.org/IMR/ActiveState:1.0
//
public abstract class ActiveStatePOA
    extends Servant
    implements InvokeHandler,
               ActiveStateOperations
{
    static final String[] _ob_ids_ =
    {
        "IDL:orb.yoko.apache.org/IMR/ActiveState:1.0",
    };

    public ActiveState
    _this()
    {
        return ActiveStateHelper.narrow(super._this_object());
    }

    public ActiveState
    _this(ORB orb)
    {
        return ActiveStateHelper.narrow(super._this_object(orb));
    }

    public String[]
    _all_interfaces(POA poa, byte[] objectId)
    {
        return _ob_ids_;
    }

    public OutputStream
    _invoke(String opName,
            InputStream in,
            ResponseHandler handler)
    {
        final String[] _ob_names =
        {
            "poa_create",
            "poa_status_update",
            "set_status"
        };

        int _ob_left = 0;
        int _ob_right = _ob_names.length;
        int _ob_index = -1;

        while(_ob_left < _ob_right)
        {
            int _ob_m = (_ob_left + _ob_right) / 2;
            int _ob_res = _ob_names[_ob_m].compareTo(opName);
            if(_ob_res == 0)
            {
                _ob_index = _ob_m;
                break;
            }
            else if(_ob_res > 0)
                _ob_right = _ob_m;
            else
                _ob_left = _ob_m + 1;
        }

        if(_ob_index == -1 && opName.charAt(0) == '_')
        {
            _ob_left = 0;
            _ob_right = _ob_names.length;
            String _ob_ami_op =
                opName.substring(1);

            while(_ob_left < _ob_right)
            {
                int _ob_m = (_ob_left + _ob_right) / 2;
                int _ob_res = _ob_names[_ob_m].compareTo(_ob_ami_op);
                if(_ob_res == 0)
                {
                    _ob_index = _ob_m;
                    break;
                }
                else if(_ob_res > 0)
                    _ob_right = _ob_m;
                else
                    _ob_left = _ob_m + 1;
            }
        }

        switch(_ob_index)
        {
        case 0: // poa_create
            return _OB_op_poa_create(in, handler);

        case 1: // poa_status_update
            return _OB_op_poa_status_update(in, handler);

        case 2: // set_status
            return _OB_op_set_status(in, handler);
        }

        throw new BAD_OPERATION();
    }

    private OutputStream
    _OB_op_poa_create(InputStream in,
                      ResponseHandler handler)
    {
        OutputStream out = null;
        try
        {
            POAStatus _ob_a0 = POAStatusHelper.read(in);
            ObjectReferenceTemplate _ob_a1 = ObjectReferenceTemplateHelper.read(in);
            ObjectReferenceTemplate _ob_r = poa_create(_ob_a0, _ob_a1);
            out = handler.createReply();
            ObjectReferenceTemplateHelper.write(out, _ob_r);
        }
        catch(_NoSuchPOA _ob_ex)
        {
            out = handler.createExceptionReply();
            _NoSuchPOAHelper.write(out, _ob_ex);
        }
        return out;
    }

    private OutputStream
    _OB_op_poa_status_update(InputStream in,
                             ResponseHandler handler)
    {
        OutputStream out = null;
        String[][] _ob_a0 = POANameSeqHelper.read(in);
        POAStatus _ob_a1 = POAStatusHelper.read(in);
        poa_status_update(_ob_a0, _ob_a1);
        out = handler.createReply();
        return out;
    }

    private OutputStream
    _OB_op_set_status(InputStream in,
                      ResponseHandler handler)
    {
        OutputStream out = null;
        String _ob_a0 = in.read_string();
        ServerStatus _ob_a1 = ServerStatusHelper.read(in);
        set_status(_ob_a0, _ob_a1);
        out = handler.createReply();
        return out;
    }
}
