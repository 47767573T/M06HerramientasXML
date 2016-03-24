import net.xqj.exist.ExistXQDataSource;

import javax.xml.xquery.*;

/**
 * Created by Moises on 24/03/2016.
 */
public class Consultas {

    public static String FICHERO = "plantes.xml";
    public static String IP = "172.31.101.225";
    public static String PUERTO = "8080";

    public static XQConnection xconn = null;


    /**
     * Realiza la consulta de la pregunta 1 y la imprime por pantalla
     * @throws XQException
     */
    public static void query1() throws XQException {

        String linea = "";
        String resultado = "";

        //Nombre de la planta que tiene el maximo stock
        String query = "doc(\"" + FICHERO + "\")/CATALOG/PLANT[AVAILABILITY = max(/CATALOG/PLANT/AVAILABILITY)]/COMMON/node()";

        XQPreparedExpression xqpe = xconn.prepareExpression(query);
        XQResultSequence xqrs = xqpe.executeQuery();

        while (xqrs.next()){

            linea = xqrs.getItemAsString(null);
            resultado += linea;
        }
        String resultadoMejorado = resultado.replaceAll("text ", "");
        System.out.println("Resultado de pregunta 1: "+resultadoMejorado);
    }

    /**
     * Realiza la consulta de la pregunta 2 y la imprime por pantalla
     * @throws XQException
     */
    public static void query2() throws XQException {
        String linea = "";
        String resultado = "";

        //Sumatorio de todas las plantas existentes
        String query = "sum(doc(\"" + FICHERO + "\")/CATALOG/PLANT/AVAILABILITY)";

        XQPreparedExpression xqpe = xconn.prepareExpression(query);
        XQResultSequence xqrs = xqpe.executeQuery();

        while (xqrs.next()){

            linea = xqrs.getItemAsString(null);
            resultado += linea;
        }
        System.out.println("Resultado de pregunta 2: "+resultado);
    }

    public static void query3(){

    }

    /**
     * Inicia Connexion con la base de datos
     * @throws XQException
     */
    public static void iniciarConexion () throws XQException {

        XQDataSource xqs = new ExistXQDataSource();
        xqs.setProperty("serverName", IP);
        xqs.setProperty("port", PUERTO);

        xconn = xqs.getConnection();
    }

    /**
     * Metodo para cerrar la conexion con la base de datos
     */
    public static void cerrarConexion () {
        try {
            xconn.close();
        } catch (XQException e) {
            System.out.println("Error al cerrar conexion: "+e);
        }
    }

}
