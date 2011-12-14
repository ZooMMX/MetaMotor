package models;

import models.Consulta.Consulta;
import models.Resultado;
import models.motor.Motor;
import org.junit.Assert;
import play.Logger;
import play.db.jpa.JPABase;

import java.util.Hashtable;
import java.util.List;

/**
 * Proyecto Omoikane: SmartPOS 2.0
 * User: octavioruizcastillo
 * Date: 14/12/11
 * Time: 02:07
 */
public class MetaMotor {
    public List<Resultado> buscar(String consultaTxt, Integer nDocs) throws Error {
        long resultadosEsperados = nDocs.longValue();
        try {
            // 1
            Consulta consultaDelUsuario = Consulta.crearConsulta(consultaTxt);
            Assert.assertNotNull("Consulta: "+consultaDelUsuario.consulta+", sin palabras clave", consultaDelUsuario.frecuencias);

            //2
            List<Motor> motores;
            Assert.assertTrue("No hay motores de búsqueda registrados", Motor.<JPABase>findAll().size() > 0);
            motores = Motor.seleccionarMotores(consultaDelUsuario);
            Assert.assertTrue("Ningún motor es candidado de búsqueda", motores.size() > 0);

            //3
            motores = Motor.selectorDocumentos(consultaDelUsuario, resultadosEsperados, motores);

            //4 Éste es todo el dispatcher
            Hashtable<Motor, List<Resultado>> resultados = new Hashtable<Motor, List<Resultado>>();
            for (Motor motor : motores) {
                //5
                List<Resultado> resultadosMotor = null;
                try {
                    resultadosMotor = motor.consultar(consultaDelUsuario);
                    resultados.put(motor, resultadosMotor);
                } catch (Exception e) { Assert.fail("Error de conexión a motores"); }
            }

            //6
            List<Resultado> resultadosMezclados = Resultado.mezclar(resultados);
            //Refinamos número total de resultados
            Integer subListFinal = (int) resultadosEsperados > resultadosMezclados.size() ? resultadosMezclados.size() : (int) resultadosEsperados;
            resultadosMezclados = resultadosMezclados.subList(0, subListFinal);

            return resultadosMezclados;
        } catch (Error err) {
            throw err;
        }
    }
}
