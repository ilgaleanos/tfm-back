package back.logica.basededatos.seguridad;

import back.utiles.DBCoreService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

import static back.logica.basededatos.seguridad.Consultas.SP_TOKEN_DELETE;


@Service
public class LogoutDB extends DBCoreService {

    public int eliminar(String uuid) throws SQLException {
        String[] parametros = new String[]{uuid};
        return ejecutarQuery(SP_TOKEN_DELETE, parametros);
    }
}