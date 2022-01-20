package back.logica.basededatos.iam;

import back.utiles.DBCoreService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static back.logica.basededatos.iam.Consultas.SP_PERMISOS_SELECT;


@Service
public class IamDB extends DBCoreService {

    public ArrayList<HashMap<String, Object>> obtenerPermisos(int usuario_id, int plataforma_id) throws SQLException {
        Object[] parametros = new Object[]{usuario_id, plataforma_id};
        return obtenerElementos(SP_PERMISOS_SELECT, parametros);
    }
}
