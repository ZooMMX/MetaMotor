package models;

import models.motor.Motor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.Logger;
import play.db.jpa.GenericModel;
import play.test.Fixtures;
import play.test.UnitTest;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Proyecto Omoikane: SmartPOS 2.0
 * User: octavioruizcastillo
 * Date: 13/12/11
 * Time: 22:19
 */
public class ResultadoTest extends UnitTest {

    @Before
    public void setUp() {
        Fixtures.deleteDatabase();
        Fixtures.loadModels("data.yml");
    }

    @Test
    public void testMezclar() throws Exception {
        List<Resultado> mezclados = new ArrayList<Resultado>();

        Hashtable<Motor, List<Resultado>> resultadosConMotor = new Hashtable<Motor, List<Resultado>>();
        GenericModel.JPAQuery query = Resultado.all();

        List<Resultado> resultados1 = query.from(0).fetch(2);
        List<Resultado> resultados2 = query.from(2).fetch(2);
        Assert.assertTrue(resultados1 != null && resultados2 != null);

        List<Motor> motores = Motor.all().fetch();
        resultadosConMotor.put(motores.get(0), resultados1);
        resultadosConMotor.put(motores.get(1), resultados2);

        Assert.assertTrue( resultados1.size() + resultados2.size() == 4 );
        mezclados = Resultado.mezclar(resultadosConMotor);

        Assert.assertTrue( mezclados.size() == 4 );
        Logger.info(mezclados.get(0).id+"<."+mezclados.get(0).documento);
        Logger.info("."+query.from(3).<Resultado>first().getId());


        Assert.assertTrue(mezclados.get(0).id == query.from(3).<Resultado>first().getId());
        Assert.assertTrue(mezclados.get(1).id == query.from(0).<Resultado>first().getId());
        Assert.assertTrue(mezclados.get(2).id == query.from(2).<Resultado>first().getId());
        Assert.assertTrue(mezclados.get(3).id == query.from(1).<Resultado>first().getId());
    }
}
