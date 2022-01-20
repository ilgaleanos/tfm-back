package back.logica.entidades;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.regex.Pattern;

@Component
@CompiledJson
public class Usuario {

    private int id;
    private String uuid;
    private String correo = "";
    private String nombre = "";
    private String apellido = "";
    private int estado;
    private String ultimo_ingreso;
    private String creacion;
    private String token = "";
    private String otp = "";
    private int vida_horas;


    // requerido para serializacion
    public Usuario() {
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    /* =================================================================================================================
     *   GETTERS Y SETTERS
     =================================================================================================================*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        Objects.requireNonNull(correo);
        this.correo = correo.toLowerCase();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCreacion() {
        return creacion;
    }

    public void setCreacion(String creacion) {
        this.creacion = creacion;
    }

    public String getUltimo_ingreso() {
        return ultimo_ingreso;
    }

    public void setUltimo_ingreso(String ultimo_ingreso) {
        this.ultimo_ingreso = ultimo_ingreso;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public int getVida_horas() {
        return vida_horas;
    }

    public void setVida_horas(int vida_horas) {
        this.vida_horas = vida_horas;
    }
}
