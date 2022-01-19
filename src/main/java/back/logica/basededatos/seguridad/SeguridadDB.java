package back.logica.basededatos.seguridad;

import back.logica.entidades.Permiso;
import back.logica.entidades.PermisoAlcance;
import back.logica.entidades.Usuario;
import back.utiles.DBCoreService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

import static back.logica.basededatos.seguridad.Consultas.*;

@Service
public class SeguridadDB extends DBCoreService {

    /**
     * Verificamos la existencia del token en la base de datos
     *
     * @param uuid    clave de ingreso temporal del usuario
     * @param permiso al que quiere acceder el usuario
     * @param alcance al que dispone
     */
    public int existeToken(final String uuid, Permiso permiso, PermisoAlcance alcance) throws SQLException {
        Object[] parametros = new Object[]{uuid, permiso.getValue(), alcance.getValue()};
        return ejecutarQuery(SP_TOKEN_EXIST, parametros);
    }


    public void fijarToken(Usuario usuario) throws SQLException {
        Object[] params = new Object[]{usuario.getId(), usuario.getVida_horas(), usuario.getUuid()};
        ejecutarQuery(SP_TOKEN_INSERT, params);
    }


    public void borrarToken(String uuid) {
        new Thread(() -> {
            try {
                ejecutarQuery(SP_TOKEN_DELETE, new String[]{uuid});
            } catch (SQLException err) {
                err.printStackTrace();
            }
        }).start();
    }
}
