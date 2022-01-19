package back.logica.flujos.iam;

import back.logica.basededatos.iam.SesionDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


@Component
public class SesionFlujo {

    private final static Logger logger = LoggerFactory.getLogger(SesionFlujo.class);
    private final SesionDB sesionDB;

    @Autowired
    public SesionFlujo(
            SesionDB sesionDB
    ) {
        this.sesionDB = sesionDB;
    }

    public ArrayList<HashMap<String, Object>> obtenerPermisos(String uuid) throws SQLException {
        return sesionDB.obtenerPermisos(uuid);
    }
}