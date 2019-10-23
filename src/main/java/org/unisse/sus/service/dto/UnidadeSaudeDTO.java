package org.unisse.sus.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link org.unisse.sus.domain.UnidadeSaude} entity.
 */
public class UnidadeSaudeDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private String descricao;

    @NotNull
    private String horario;

    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;

    @NotNull
    private String endereco;

    @NotNull
    private String bairro;

    @NotNull
    private String cidade;

    @NotNull
    private String uf;

    @NotNull
    private String cep;

    @NotNull
    private String referencia;


    private Long tipoId;

    private Long situacaoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Long getTipoId() {
        return tipoId;
    }

    public void setTipoId(Long tipoUnidadeSaudeId) {
        this.tipoId = tipoUnidadeSaudeId;
    }

    public Long getSituacaoId() {
        return situacaoId;
    }

    public void setSituacaoId(Long situacaoUnidadeSaudeId) {
        this.situacaoId = situacaoUnidadeSaudeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UnidadeSaudeDTO unidadeSaudeDTO = (UnidadeSaudeDTO) o;
        if (unidadeSaudeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), unidadeSaudeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UnidadeSaudeDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", horario='" + getHorario() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", endereco='" + getEndereco() + "'" +
            ", bairro='" + getBairro() + "'" +
            ", cidade='" + getCidade() + "'" +
            ", uf='" + getUf() + "'" +
            ", cep='" + getCep() + "'" +
            ", referencia='" + getReferencia() + "'" +
            ", tipo=" + getTipoId() +
            ", situacao=" + getSituacaoId() +
            "}";
    }
}
