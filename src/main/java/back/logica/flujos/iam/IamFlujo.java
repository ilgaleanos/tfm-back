package back.logica.flujos.iam;

import back.logica.basededatos.iam.IamDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


@Service
public class IamFlujo {

    private final static Logger logger = LoggerFactory.getLogger(IamFlujo.class);
    private final IamDB iamDB;

    @Autowired
    public IamFlujo(
            IamDB iamDB
    ) {
        this.iamDB = iamDB;
    }

    public ArrayList<HashMap<String, Object>> obtenerPermisos(int usuario_id, int plataforma_id) throws SQLException {
        return iamDB.obtenerPermisos(usuario_id, plataforma_id);
    }
}