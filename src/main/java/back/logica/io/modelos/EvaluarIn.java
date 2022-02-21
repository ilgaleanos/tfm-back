package back.logica.io.modelos;

import back.utiles.Env;
import com.dslplatform.json.CompiledJson;

@CompiledJson
public class EvaluarIn {

    private String key = Env.AUTH_API_KEY;
    private double pib;
    private double participaciones;
    private double regalias;
    private double censo_electoral;
    private double censo_habitacional;
    private double homicidios;

    // requerido para serializacion
    public EvaluarIn() {
    }

    public boolean esInValido() {
        return pib == 0
                || participaciones == 0
                || regalias == 0
                || censo_electoral == 0
                || censo_habitacional == 0
                || homicidios == 0;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = Env.AUTH_API_KEY;
    }

    public double getPib() {
        return pib;
    }

    public void setPib(double pib) {
        this.pib = pib;
    }

    public double getParticipaciones() {
        return participaciones;
    }

    public void setParticipaciones(double participaciones) {
        this.participaciones = participaciones;
    }

    public double getRegalias() {
        return regalias;
    }

    public void setRegalias(double regalias) {
        this.regalias = regalias;
    }

    public double getCenso_electoral() {
        return censo_electoral;
    }

    public void setCenso_electoral(double censo_electoral) {
        this.censo_electoral = censo_electoral;
    }

    public double getCenso_habitacional() {
        return censo_habitacional;
    }

    public void setCenso_habitacional(double censo_habitacional) {
        this.censo_habitacional = censo_habitacional;
    }

    public double getHomicidios() {
        return homicidios;
    }

    public void setHomicidios(double homicidios) {
        this.homicidios = homicidios;
    }
}
