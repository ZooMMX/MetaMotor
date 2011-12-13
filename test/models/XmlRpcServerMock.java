package models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: PNMB
 * Date: 12/12/11
 * Time: 08:57 PM
 */
public class XmlRpcServerMock {
    public List<String> consultar(String consulta) {
        ArrayList<String> resultados = new ArrayList<String>();
        resultados.add("Resultado de la búsqueda 1");
        resultados.add("Resultado de la búsqueda 2");
        return resultados;
    }
}
