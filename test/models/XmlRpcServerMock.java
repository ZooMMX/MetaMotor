package models;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 12/12/11
 * Time: 08:57 PM
 */
public class XmlRpcServerMock {
    public List<AbstractMap<String, Object>> consultar(String consulta) {
        ArrayList<AbstractMap<String, Object>> resultados = new ArrayList<AbstractMap<String, Object>>();
        AbstractMap<String, Object> map1 = new HashMap<String, Object>();
        AbstractMap<String, Object> map2 = new HashMap<String, Object>();

        map1.put("documento" , "documento prueba 1");
        map1.put("relevancia", 0.88d);

        map2.put("documento" , "documento prueba 2");
        map2.put("relevancia", 0.32d);

        resultados.add(map1);
        resultados.add(map2);
        return resultados;
    }
}
