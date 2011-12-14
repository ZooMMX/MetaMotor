package models;

import models.Consulta.Consulta;
import models.motor.Motor;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.webserver.WebServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 12/12/11
 * Time: 08:53 PM
 */
public class MotorRemoteTest extends UnitTest {

    WebServer server;

    @Before
    public void setUp() {
        Fixtures.deleteDatabase();
        Fixtures.loadModels("data.yml");

      try {
         System.out.println("Attempting to start XML-RPC Server...");
         server = new WebServer(9001);
         XmlRpcServer xmlRpcServer = server.getXmlRpcServer();
         PropertyHandlerMapping phm = new PropertyHandlerMapping();

         phm.addHandler("Motor", XmlRpcServerMock.class);
         xmlRpcServer.setHandlerMapping(phm);

         server.start();
         System.out.println("Started successfully.");
         System.out.println("Accepting requests. (Halt program to stop.)");
       } catch (Exception exception) {
         System.err.println("JavaServer: " + exception);
       }
    }

    @After
    public void tearDown() {
        server.shutdown();
    }

    @Test
    public void testConsultar() throws Exception {
        Consulta consultaDelUsuario = Consulta.all().first();

        Motor motor = Motor.all().first();
        List<Resultado> resultados = motor.consultar(consultaDelUsuario);

        Assert.assertTrue(resultados != null);
        Assert.assertTrue(resultados.size() == 2);
        Assert.assertTrue(resultados.get(0).documento.equals("documento prueba 1"));
    }
}
