package back.logica.io.seguridad;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CredencialesIn {


    /*==================================================================================================================
     * Generar OTP
     *================================================================================================================*/

    @CompiledJson
    public static class GenerarOTP {

        @JsonAttribute(ignore = true)
        private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        @JsonAttribute(ignore = true)
        private static final Pattern VALID_PHONE_ADDRESS_REGEX =
                Pattern.compile("^\\+?[0-9]{1,3}\\s?[0-9\\s]{2,20}$", Pattern.CASE_INSENSITIVE);
        private String correo;
        private String telefono;
        private String captcha;


        // requerido para serialización
        public GenerarOTP() {
        }


        public boolean esInValido() {
            if (captcha == null || (correo == null && telefono == null)) {
                return true;
            }
            if (captcha.isEmpty()) {
                return true;
            }
            if (correo != null) {
                Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(correo);
                return !matcher.find();
            }
            Matcher matcher = VALID_PHONE_ADDRESS_REGEX.matcher(telefono);
            return !matcher.find();
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo.toLowerCase().strip();
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono.strip();
        }

        public String getCaptcha() {
            return captcha;
        }

        public void setCaptcha(String captcha) {
            this.captcha = captcha;
        }
    }



    /*==================================================================================================================
     * GenerarToken
     *================================================================================================================*/

    @CompiledJson
    public static class GenerarToken {

        private String uuid;
        private String otp;
        private String captcha;

        // requerido para serialización
        public GenerarToken() {
        }


        public boolean esInValido() {
            return uuid == null || uuid.isEmpty()
                    || otp == null || otp.length() != 7
                    || captcha == null || captcha.isEmpty();
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getCaptcha() {
            return captcha;
        }

        public void setCaptcha(String captcha) {
            this.captcha = captcha;
        }
    }
}
