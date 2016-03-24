import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;

import java.io.File;

/**
 * Created by Moises on 24/03/2016.
 */
public class Administracio {

    public static void main(String[] args) throws XMLDBException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        addCol("plantes.xml");
    }

    private static String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
    private static String driver = "org.exist.xmldb.DatabaseImpl";

    public Administracio() { }

    private static void addCol(String fl) throws XMLDBException, ClassNotFoundException, IllegalAccessException, InstantiationException{
        File f = new File(fl);

        // initialize database driver
        Class cl = Class.forName(driver);
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");

        // crear el manegador
        DatabaseManager.registerDatabase(database);

        //crear la collecion
        Collection parent = DatabaseManager.getCollection(URI+"/db","admin","admin");
        CollectionManagementService c = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");

        c.createCollection("Villaverde");

        Collection col = DatabaseManager.getCollection(URI + "/db/Villaverde", "admin", "admin");
        //afegir el recurs que farem servir
        Resource res = col.createResource(fl,"XMLResource");
        res.setContent(f);
        col.storeResource(res);

    }




}
