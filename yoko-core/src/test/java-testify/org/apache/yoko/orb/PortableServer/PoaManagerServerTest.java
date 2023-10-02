package org.apache.yoko.orb.PortableServer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAManager;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import test.poa.PMSTestThread;
import test.poa.PMSTestThread.Result;
import test.poa.POAManagerProxy;
import test.poa.POAManagerProxyHelper;
import test.poa.POAManagerProxyPOA;
import test.poa.TestDSI_impl;
import test.poa.TestHelper;
import test.poa.TestInfo;
import test.poa.TestPOAManagerCommon;
import test.poa.TestServer;
import test.poa.TestServerHelper;
import test.poa.TestServer_impl;
import test.poa.Test_impl;
import testify.bus.Bus;
import testify.iiop.annotation.ConfigureServer;
import testify.iiop.annotation.ConfigureServer.BeforeServer;

import java.io.FileOutputStream;
import java.io.PrintWriter;

import static org.apache.yoko.orb.PortableServer.PolicyValue.NO_IMPLICIT_ACTIVATION;
import static org.apache.yoko.orb.PortableServer.PolicyValue.PERSISTENT;
import static org.apache.yoko.orb.PortableServer.PolicyValue.RETAIN;
import static org.apache.yoko.orb.PortableServer.PolicyValue.UNIQUE_ID;
import static org.apache.yoko.orb.PortableServer.PolicyValue.USER_ID;
import static org.apache.yoko.orb.PortableServer.PolicyValue.create_POA;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@ConfigureServer
public class PoaManagerServerTest {

    @BeforeServer
    public static void setup(ORB orb, POA root) throws Exception {
        POAManager mgr = root.the_POAManager();

        // Create POA w/ RETAIN. This POA should use a seperate POAManager.
        POA retain = create_POA("retain", root, null, PERSISTENT, USER_ID, RETAIN, NO_IMPLICIT_ACTIVATION, UNIQUE_ID);

        POAManager retainManager = retain.the_POAManager();

        POAManagerProxy_impl proxyImpl = new POAManagerProxy_impl(retainManager);
        POAManagerProxy proxy = proxyImpl._this(orb);

        Test_impl testImpl = new Test_impl(orb, retain);
        byte[] oid = "test".getBytes();
        retain.activate_object_with_id(oid, testImpl);


        test.poa.Test test = testImpl._this();

        TestDSI_impl testDSIImpl = new TestDSI_impl(orb, retain);
        byte[] oidDSI = "testDSI".getBytes();
        retain.activate_object_with_id(oidDSI, testDSIImpl);
        org.omg.CORBA.Object objDSI = retain.create_reference_with_id(oidDSI, "IDL:Test:1.0");
        test.poa.Test testDSI = TestHelper.narrow(objDSI);

        // Create server
//            TestInfo[] info = new TestInfo[2];
//            info[0] = new TestInfo();
//            info[1] = new TestInfo();
//            info[0].obj = test;
//            info[0].except_id = "";
//            info[1].obj = testDSI;
//            info[1].except_id = "";
//            TestServer_impl serverImpl = new TestServer_impl(orb, info);
//            TestServer server = serverImpl._this(orb);
        TestInfo[] info = new TestInfo[]{new TestInfo(), new TestInfo()};
        info[0].obj = test;
        info[0].except_id = "";
        info[1].obj = testDSI;
        info[1].except_id = "";
        TestServer_impl serverImpl = new TestServer_impl(orb, info);
        TestServer server = serverImpl._this(orb);

        // If JTC is available spawn a thread to find out whether a method invocation on test is blocked until POAManager::activate() is called.
        PMSTestThread t = new PMSTestThread(test);
        t.start();
        t.waitForStart();

        // Run implementation. This should cause the blocked call in the thread to release.
        mgr.activate();
        retainManager.activate();
        t.waitForEnd();

        assertSame(t.result, Result.SUCCESS);

        new TestPOAManagerCommon(proxy, info);

        // Don't write references until we're ready to run
        // Save reference
        String refFile = "Test.ref";
        FileOutputStream file = new FileOutputStream(refFile);
        PrintWriter out = new PrintWriter(file);
        out.println(orb.object_to_string(server));
        out.flush();
        file.close();

        // Save reference
        String refFileMgr = "POAManagerProxy.ref";
        FileOutputStream file1 = new FileOutputStream(refFileMgr);
        PrintWriter out1 = new PrintWriter(file1);
        out1.println(orb.object_to_string(proxy));
        out1.flush();
        file1.close();
    }

    @BeforeAll
    public static void setupClient(ORB orb, POA root, Bus bus) throws Exception {
        org.omg.CORBA.Object obj = orb.string_to_object("relfile:/Test.ref");
        TestServer server = TestServerHelper.narrow(obj);

        obj = orb.string_to_object("relfile:/POAManagerProxy.ref");
        POAManagerProxy poaMgrProxy = POAManagerProxyHelper.narrow(obj);
        poaMgrProxy.activate();

        TestInfo[] info = server.get_info();
        new TestPOAManagerCommon(poaMgrProxy, info);

        server.deactivate();
    }

    @Test
     void test1(ORB orb) {

    }

    static final class POAManagerProxy_impl extends LocalObject implements POAManagerProxy {
        private final POAManager mgr;

        public POAManagerProxy_impl(POAManager manager) { mgr = manager; }

        // Mapping for PortableServer::POAManager
        public void activate()
                throws test.poa.POAManagerProxyPackage.AdapterInactive {
            try {
                mgr.activate();
            } catch (AdapterInactive ex) {
                throw new test.poa.POAManagerProxyPackage.AdapterInactive();
            }
        }

        public void hold_requests(boolean a)
                throws test.poa.POAManagerProxyPackage.AdapterInactive {
            try {
                mgr.hold_requests(a);
            } catch (AdapterInactive ex) {
                throw new test.poa.POAManagerProxyPackage.AdapterInactive();
            }
        }

        public void discard_requests(boolean a)
                throws test.poa.POAManagerProxyPackage.AdapterInactive {
            try {
                mgr.discard_requests(a);
            } catch (AdapterInactive ex) {
                throw new test.poa.POAManagerProxyPackage.AdapterInactive();
            }
        }

        public void deactivate(boolean a, boolean b)
                throws test.poa.POAManagerProxyPackage.AdapterInactive {
            try {
                mgr.deactivate(a, b);
            } catch (AdapterInactive ex) {
                throw new test.poa.POAManagerProxyPackage.AdapterInactive();
            }
        }

        public test.poa.POAManagerProxyPackage.State get_state() {
            return test.poa.POAManagerProxyPackage.State.from_int(mgr.get_state().value());
        }
    }

}
