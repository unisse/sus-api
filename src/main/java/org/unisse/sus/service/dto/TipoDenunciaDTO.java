package org.unisse.sus.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.unisse.sus.domain.TipoDenuncia} entity.
 */
public class TipoDenunciaDTO implements Serializable {

    private Long id;

    @NotNull
    private String descricao;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TipoDenunciaDTO tipoDenunciaDTO = (TipoDenunciaDTO) o;
        if (tipoDenunciaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoDenunciaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoDenunciaDTO{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
