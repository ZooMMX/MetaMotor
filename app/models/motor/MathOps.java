package models.motor;

import models.Consulta.Consulta;
import models.Frecuencia;
import models.Termino;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MathOps implements Serializable {
    static List<Motor> calcularRm(Consulta consulta, Long resultadosEsperados, List<Motor> motores) {
        MathContext mc = MathContext.DECIMAL64;

        List<Termino> terminos = new ArrayList<Termino>();
        for (Frecuencia frecuencia : consulta.frecuencias) {
            terminos.add(frecuencia.termino);
        }
        BigDecimal cv2Sum = new BigDecimal(0d, mc).setScale(6, BigDecimal.ROUND_HALF_UP);
        BigDecimal cvv = new BigDecimal(0d, mc).setScale(6, BigDecimal.ROUND_HALF_UP);
        HashMap<String, HashMap<Motor, Long>> completa = new HashMap<String, HashMap<Motor, Long>>();
        for (Motor motor : motores) {

            for (Termino termino : terminos) {
                Long totalFrecuencias = (Long) Frecuencia.find("SELECT SUM(f.frecuencia) FROM Frecuencia as f WHERE termino = ? AND fuente.class = Motor", termino).query.getSingleResult();
                if (totalFrecuencias == null) {
                    continue;
                }
                if (completa.get(termino.termino) == null) {
                    completa.put(termino.termino, new HashMap<Motor, Long>());
                }
                Frecuencia frecDFmj = Frecuencia.findOrCreate(motor, termino); // Inicialización perezosa
                completa.get(termino.termino).put(motor, frecDFmj.frecuencia);
            }
        }
        for (Motor motor : motores) {
            for (String termino : completa.keySet()) {
                Long frec = completa.get(termino).get(motor);
                BigDecimal a = new BigDecimal(frec.doubleValue() / motor.nDocs.doubleValue(), mc).setScale(6, BigDecimal.ROUND_HALF_UP);
                BigDecimal b = new BigDecimal(0d, mc).setScale(6, BigDecimal.ROUND_HALF_UP);
                BigDecimal c = new BigDecimal(0d, mc).setScale(6, BigDecimal.ROUND_HALF_UP);
                for (Map.Entry<Motor, Long> motorFrec : completa.get(termino).entrySet()) {
                    if (motorFrec.getKey() == motor) {
                        continue;
                    }
                    b = b.add(new BigDecimal(motorFrec.getValue().doubleValue()), mc);
                    c = c.add(new BigDecimal(motor.nDocs.doubleValue()), mc);
                }
                BigDecimal d = b.divide(c, BigDecimal.ROUND_HALF_UP);
                BigDecimal cv = a.divide(a.add(d), BigDecimal.ROUND_HALF_UP);
                BigDecimal cv2 = (cv.subtract(promedio(completa.get(termino)))).pow(2);
                cv2Sum = cv2Sum.add(cv2);
            }
        }
        cvv = cvv.add(cv2Sum);
        BigDecimal motoresSize = new BigDecimal(motores.size(), mc).setScale(6, BigDecimal.ROUND_HALF_UP);
        cvv = cvv.divide(motoresSize, BigDecimal.ROUND_HALF_UP);

        for (Motor motor : motores) {
            BigDecimal rm = new BigDecimal(0d, mc).setScale(6, BigDecimal.ROUND_HALF_UP);
            for (String termino : completa.keySet()) {
                rm = rm.add(new BigDecimal(completa.get(termino).get(motor).doubleValue()).multiply(cvv));
            }
            motor.setRm(rm);
        }

        return motores;
    }

    static BigDecimal promedio(HashMap<Motor, Long> motorLongHashMap) {
        MathContext mc = MathContext.DECIMAL64;
        BigDecimal resultado = new BigDecimal(0d, mc).setScale(6, BigDecimal.ROUND_HALF_UP);
        BigDecimal numMotores = new BigDecimal(motorLongHashMap.size(), mc).setScale(6, BigDecimal.ROUND_HALF_UP);
        for (Long aLong : motorLongHashMap.values()) {
            BigDecimal frecuencia = new BigDecimal(aLong.doubleValue(), mc).setScale(6, BigDecimal.ROUND_HALF_UP);
            resultado = resultado.add(frecuencia);
        }
        return resultado.divide(numMotores);
    }
}