package back.logica.basededatos.iam;

import back.utiles.DBCoreService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static back.logica.basededatos.iam.Consultas.SP_PERMISOS_SELECT_PROPIOS;


@Service
public class SesionDB extends DBCoreService {

    public ArrayList<HashMap<String, Object>> obtenerPermisos(String uuid) throws SQLException {
        Object[] parametros = new Object[]{uuid};
        return obtenerElementos(SP_PERMISOS_SELECT_PROPIOS, parametros);
    }
}
