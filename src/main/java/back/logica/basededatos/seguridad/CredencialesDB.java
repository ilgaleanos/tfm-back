package back.logica.basededatos.seguridad;

import back.logica.entidades.Usuario;
import back.utiles.DBCoreService;
import back.utiles.SerializadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;

import static back.logica.basededatos.seguridad.Consultas.*;


@Service
public class CredencialesDB extends DBCoreService {


    SerializadorService serializadorService;

    @Autowired
    CredencialesDB(SerializadorService serializadorService) {
        this.serializadorService = serializadorService;
    }


    /**
     * @param telefono
     * @param correo
     * @param uuid
     * @param otp
     * @return
     * @throws SQLException
     */
    public int insertarOtpUsuario(String telefono, String correo, String uuid, String otp) throws SQLException {
        String[] parametros = new String[]{telefono, correo, uuid, otp};
        return ejecutarQuery(SP_USUARIOS_INSERT_OTP, parametros);
    }


    /**
     * Consulta que busca un único usuario por email
     *
     * @return Usuario encontrado por el email
     * @throws SQLException error de la base de datos
     */
    public Usuario obtenerUsuarioIdentificadorUuidOtp(String uuid, String otp) throws SQLException {
        String[] parametros = new String[]{uuid, otp};
        HashMap<String, Object> usuarioMap = obtenerElemento(SP_USUARIOS_SELECT_X_UUID_OTP, parametros);
        if (usuarioMap == null || usuarioMap.size() == 0) {
            return null;
        }
        byte[] usuarioBytes = serializadorService.serialize(usuarioMap);
        return serializadorService.deserialize(Usuario.class, usuarioBytes);
    }


    public int eliminarToken(String uuid) throws SQLException {
        String[] parametros = new String[]{uuid};
        return ejecutarQuery(SP_TOKEN_DELETE, parametros);
    }
}
