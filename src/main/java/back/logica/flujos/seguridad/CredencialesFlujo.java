package back.logica.flujos.seguridad;

import back.logica.basededatos.seguridad.CredencialesDB;
import back.logica.entidades.Usuario;
import back.utiles.Captcha;
import back.utiles.CorreoSMTPService;
import back.utiles.SeguridadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;


@Service
public class CredencialesFlujo {

    private final static Logger logger = LoggerFactory.getLogger(CredencialesFlujo.class);
    private final SeguridadService seguridadService;
    private final CredencialesDB credencialesDB;
    private final Captcha recaptcha;
    private final CorreoSMTPService correoSMTPService;

    @Autowired
    public CredencialesFlujo(
            SeguridadService seguridadService,
            CredencialesDB credencialesDB,
            Captcha recaptcha,
            CorreoSMTPService correoSMTPService
    ) {
        this.recaptcha = recaptcha;
        this.seguridadService = seguridadService;
        this.credencialesDB = credencialesDB;
        this.correoSMTPService = correoSMTPService;
    }


    /**
     * @param telefono: posible ingreso por telefono
     * @param correo:   posible ingreso por correo
     * @param captcha:  cadena para verificar si es una petición válida
     * @return token generado o nulo en caso de fallo de autenticación
     */
    public String generarOTP(String telefono, String correo, String captcha) throws SQLException, AddressException {
        if (recaptcha.verificarInvalido(captcha, 0.9)) {
            return null;
        }
        String otp = seguridadService.generateOTPSeguro(6);
        String uuid = UUID.randomUUID().toString();
        int esCorreo = credencialesDB.insertarOtpUsuario(telefono, correo, uuid, otp);
        if (esCorreo == 999) {
            return null;
        }

        correoSMTPService.enviarEmailNoreply(
                "OTP para Sun Eyes",
                "Tu OTP de ingreso es : <b>" + otp + "</b>",
                new Address[]{new InternetAddress(correo)},
                new Address[]{}
        );

        return uuid;
    }


    /**
     * Método para validar el ingreso de un usuario
     *
     * @param resp
     * @param uuid    :    campo por el cual se buscara en la base de datos
     * @param otp     :     otp a validar
     * @param captcha : cadena para verificar si es una petición válida
     * @return token generado o nulo en caso de fallo de autenticación
     */
    public Usuario ingresar(HttpServletResponse resp, String uuid, String otp, String captcha) throws SQLException {
        if (recaptcha.verificarInvalido(captcha, 0.9)) {
            return null;
        }

        Usuario usuario = credencialesDB.obtenerUsuarioIdentificadorUuidOtp(uuid, otp);
        if (usuario == null) {
            return null;
        }

        usuario.setUuid(UUID.randomUUID().toString());
        usuario.setToken(seguridadService.generarToken(resp, usuario, Calendar.HOUR));

        return usuario;
    }


    public int eliminarToken(HttpServletRequest req, HttpServletResponse resp) {
        return seguridadService.eliminarSesion(req, resp);
    }
}