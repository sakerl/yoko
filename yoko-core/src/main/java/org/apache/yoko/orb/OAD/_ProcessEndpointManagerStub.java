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
package org.apache.yoko.orb.OAD;

import org.apache.yoko.orb.IMR.ProcessIDHelper;
import org.omg.CORBA.UNKNOWN;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.CORBA.portable.ServantObject;

//
// IDL:orb.yoko.apache.org/OAD/ProcessEndpointManager:1.0
//
public class _ProcessEndpointManagerStub extends ObjectImpl
                                         implements ProcessEndpointManager
{
    private static final String[] _ob_ids_ =
    {
        "IDL:orb.yoko.apache.org/OAD/ProcessEndpointManager:1.0",
    };

    public String[]
    _ids()
    {
        return _ob_ids_;
    }

    final public static Class _ob_opsClass = ProcessEndpointManagerOperations.class;

    //
    // IDL:orb.yoko.apache.org/OAD/ProcessEndpointManager/establish_link:1.0
    //
    public void
    establish_link(String _ob_a0,
                   String _ob_a1,
                   int _ob_a2,
                   ProcessEndpoint _ob_a3)
        throws AlreadyLinked
    {
        while(true)
        {
            if(!this._is_local())
            {
                OutputStream out = null;
                InputStream in = null;
                try
                {
                    out = _request("establish_link", true);
                    out.write_string(_ob_a0);
                    out.write_string(_ob_a1);
                    ProcessIDHelper.write(out, _ob_a2);
                    ProcessEndpointHelper.write(out, _ob_a3);
                    in = _invoke(out);
                    return;
                }
                catch(RemarshalException _ob_ex)
                {
                    continue;
                }
                catch(ApplicationException _ob_aex)
                {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();

                    if(_ob_id.equals(AlreadyLinkedHelper.id()))
                        throw AlreadyLinkedHelper.read(in);
                    throw (UNKNOWN)new 
                        UNKNOWN("Unexpected User Exception: " + _ob_id).initCause(_ob_aex);
                }
                finally
                {
                    _releaseReply(in);
                }
            }
            else
            {
                ServantObject _ob_so = _servant_preinvoke("establish_link", _ob_opsClass);
                if(_ob_so == null)
                    continue;
                ProcessEndpointManagerOperations _ob_self = (ProcessEndpointManagerOperations)_ob_so.servant;
                try
                {
                    _ob_self.establish_link(_ob_a0, _ob_a1, _ob_a2, _ob_a3);
                    return;
                }
                finally
                {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }
}
