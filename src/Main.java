

import net.xqj.exist.ExistXQDataSource;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;

import javax.xml.xquery.*;
import java.io.File;

/**
 * Created by Moises on 24/03/2016.
 */
public class Main {

    private static String IP = "localhost";
    public static String PUERTO = "8080";
    private static String URI = "xmldb:exist://"+IP+":"+PUERTO+"/exist/xmlrpc";
    private static String driver = "org.exist.xmldb.DatabaseImpl";
    public static String FICHERO = "plantes.xml";

    public static XQConnection xconn = null;

    public static void main(String[] args) throws XMLDBException, ClassNotFoundException, InstantiationException, IllegalAccessException, XQException {

        System.out.println("Iniciando conexión");
        iniciarConexion();

        System.out.println("Crear coleccion");
        addCol ("Villaverde");                  //Creamos la coleccion

        System.out.println("Añadir recurso de plantas");
        addRes (FICHERO, "Villaverde");         //Añadimos el recurso.

        System.out.println("Realizar query. Resultado:");
        toQuery();


    }

    public static void iniciarConexion () throws XQException {

        XQDataSource xqs = new ExistXQDataSource();
        xqs.setProperty("serverName", IP);
        xqs.setProperty("port", PUERTO);

        xconn = xqs.getConnection();

        System.out.println("\t.Iniciada conexión en "+IP+":"+PUERTO);
    }


    private static void addCol(String nombreDB) throws XMLDBException, ClassNotFoundException, IllegalAccessException, InstantiationException{


        // initialize database driver
        Class cl = Class.forName(driver);
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");
        System.out.println("\t.configuradas propiedades");

        // crear el Manager
        DatabaseManager.registerDatabase(database);

        //crear la collection
        Collection parent = DatabaseManager.getCollection(URI+"/db","admin","admin");
        CollectionManagementService c = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");
        c.createCollection(nombreDB);

        System.out.println("\t.creada coleccion");

    }

    private static void addRes (String fl, String nombreDB) throws XMLDBException {
        File fichero = new File(fl);

        Collection col = DatabaseManager.getCollection(URI + "/db/"+nombreDB, "admin", "admin");
        System.out.println("\t.consiguiendo coleccion");

        Resource res = col.createResource(fl,"XMLResource");
        res.setContent(fichero);
        col.storeResource(res);
        System.out.println("\t.añadido recurso");
    }

    public static void toQuery() throws XQException {

        String linea;
        String resultado = "";

        String query = "collection(\"" + FICHERO + "\")/CATALOG/PLANTS";

        XQPreparedExpression xqpe = xconn.prepareExpression(query);
        XQResultSequence xqrs = xqpe.executeQuery();

        while (xqrs.next()){
            linea = xqrs.getItemAsString(null);
            resultado += linea;
        }
        System.out.println(resultado);
    }
}
