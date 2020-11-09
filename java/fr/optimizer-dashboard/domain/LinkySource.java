package fr.ifpen.synergreen.domain;

import fr.ifpen.synergreen.domain.enumeration.ParameterType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A LinkySource.
 */
@Entity
@DiscriminatorValue("LINKY")
public class LinkySource extends MeasurementSource implements Serializable {
    public LinkySource(String name, String url, String login, String passwd) {
        // Example : should be in mapper probably
       /* parameters.add(new Parameter(name, LinkyParameter.LINKY_URL.name(), LinkyParameter.LINKY_URL.getType(), url, ""));
        parameters.add(new Parameter(name, LinkyParameter.LINKY_LOGIN.name(), LinkyParameter.LINKY_LOGIN.getType(), login, ""));
        parameters.add(new Parameter(name, LinkyParameter.LINKY_PASSWD.name(), LinkyParameter.LINKY_PASSWD.getType(), passwd, ""));*/
    }

    public MeasuredData getPower(ZonedDateTime t) {
        // Here put the code to connect to linky with credentials coming from parameters and retrieve value
        // String url = getParameter(LinkyParameter.LINKY_URL.name()).getParameterValue();
        // ...
        return null; // return linky_session.getValue(t)
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinkySource linkySource = (LinkySource) o;
        if (linkySource.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), linkySource.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LinkySource{" +
            "id=" + getId() +
            "}";
    }

    enum LinkyParameter { // Maybe thre is another way but need to express the list of parameter for front
        LINKY_NAME("URL", ParameterType.STRING),
        LINKY_URL("URL to Linky", ParameterType.STRING),
        LINKY_LOGIN("Linky client ID", ParameterType.STRING),
        LINKY_PASSWD("Linky passwd", ParameterType.STRING);

        private String label;
        private ParameterType type;

        LinkyParameter(String label, ParameterType type) {
            this.label = label;
            this.type = type;
        }

        public String getLabel() {
            return label;
        }

        public ParameterType getType() {
            return type;
        }
    }
}
