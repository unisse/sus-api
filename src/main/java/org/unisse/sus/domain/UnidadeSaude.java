package org.unisse.sus.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A UnidadeSaude.
 */
@Entity
@Table(name = "unidade_saude")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UnidadeSaude implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @NotNull
    @Column(name = "horario", nullable = false)
    private String horario;

    @NotNull
    @Column(name = "latitude", precision = 21, scale = 2, nullable = false)
    private BigDecimal latitude;

    @NotNull
    @Column(name = "longitude", precision = 21, scale = 2, nullable = false)
    private BigDecimal longitude;

    @NotNull
    @Column(name = "endereco", nullable = false)
    private String endereco;

    @NotNull
    @Column(name = "bairro", nullable = false)
    private String bairro;

    @NotNull
    @Column(name = "cidade", nullable = false)
    private String cidade;

    @NotNull
    @Column(name = "uf", nullable = false)
    private String uf;

    @NotNull
    @Column(name = "cep", nullable = false)
    private String cep;

    @NotNull
    @Column(name = "referencia", nullable = false)
    private String referencia;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("unidadeSaudes")
    private TipoUnidadeSaude tipo;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("unidadeSaudes")
    private SituacaoUnidadeSaude situacao;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public UnidadeSaude nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public UnidadeSaude descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getHorario() {
        return horario;
    }

    public UnidadeSaude horario(String horario) {
        this.horario = horario;
        return this;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public UnidadeSaude latitude(BigDecimal latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public UnidadeSaude longitude(BigDecimal longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getEndereco() {
        return endereco;
    }

    public UnidadeSaude endereco(String endereco) {
        this.endereco = endereco;
        return this;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public UnidadeSaude bairro(String bairro) {
        this.bairro = bairro;
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public UnidadeSaude cidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public UnidadeSaude uf(String uf) {
        this.uf = uf;
        return this;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public UnidadeSaude cep(String cep) {
        this.cep = cep;
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getReferencia() {
        return referencia;
    }

    public UnidadeSaude referencia(String referencia) {
        this.referencia = referencia;
        return this;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public TipoUnidadeSaude getTipo() {
        return tipo;
    }

    public UnidadeSaude tipo(TipoUnidadeSaude tipoUnidadeSaude) {
        this.tipo = tipoUnidadeSaude;
        return this;
    }

    public void setTipo(TipoUnidadeSaude tipoUnidadeSaude) {
        this.tipo = tipoUnidadeSaude;
    }

    public SituacaoUnidadeSaude getSituacao() {
        return situacao;
    }

    public UnidadeSaude situacao(SituacaoUnidadeSaude situacaoUnidadeSaude) {
        this.situacao = situacaoUnidadeSaude;
        return this;
    }

    public void setSituacao(SituacaoUnidadeSaude situacaoUnidadeSaude) {
        this.situacao = situacaoUnidadeSaude;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnidadeSaude)) {
            return false;
        }
        return id != null && id.equals(((UnidadeSaude) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UnidadeSaude{" +
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
            "}";
    }
}
