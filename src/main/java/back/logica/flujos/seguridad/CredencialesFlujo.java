package back.logica.flujos.seguridad;

import back.logica.basededatos.seguridad.UsuarioDB;
import back.logica.entidades.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;


@Component
public class CredencialesFlujo {

    private final static Logger logger = LoggerFactory.getLogger(CredencialesFlujo.class);
    private final SeguridadService seguridadService;
    private final UsuarioDB usuarioDB;
    private final Captcha recaptcha;

    @Autowired
    public CredencialesFlujo(
            SeguridadService seguridadService,
            UsuarioDB usuarioDB,
            Captcha recaptcha
    ) {
        this.recaptcha = recaptcha;
        this.seguridadService = seguridadService;
        this.usuarioDB = usuarioDB;
    }


    /**
     * @param telefono: posible ingreso por telefono
     * @param correo:   posible ingreso por correo
     * @param captcha:  cadena para verificar si es una petición válida
     * @return token generado o nulo en caso de fallo de autenticación
     */
    public String generarOTP(String telefono, String correo, String captcha) throws SQLException {
        if (recaptcha.verificarInvalido(captcha)) {
            return null;
        }
        String otp = seguridadService.generateOTPSeguro(7);
        String uuid = UUID.randomUUID().toString();
        int esCorreo = usuarioDB.insertarOtpUsuario(telefono, correo, uuid, otp);
        if (esCorreo == 999) {
            return null;
        }

        if (esCorreo == 1) {
            // TODO: enviar por el correo
        } else {
            // TODO: enviar por el telefono
        }
        logger.info("UUID: " + uuid + " OTP: " + otp);
        return uuid;
    }


    /**
     * Método para validar el ingreso de un usuario
     *
     * @param uuid:    campo por el cual se buscara en la base de datos
     * @param otp:     otp a validar
     * @param captcha: cadena para verificar si es una petición válida
     * @return token generado o nulo en caso de fallo de autenticación
     */
    public Usuario ingresar(String uuid, String otp, String captcha) throws SQLException {
        if (recaptcha.verificarInvalido(captcha)) {
            return null;
        }

        Usuario usuario = usuarioDB.obtenerUsuarioIdentificadorUuidOtp(uuid, otp);
        if (usuario == null) {
            return null;
        }

        usuario.setUuid(UUID.randomUUID().toString());
        usuario.setToken(seguridadService.generarToken(usuario, Calendar.HOUR));

        return usuario;
    }
}