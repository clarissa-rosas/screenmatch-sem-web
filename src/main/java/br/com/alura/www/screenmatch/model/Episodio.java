package br.com.alura.www.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {
    private Integer numeroTemporada;
    private String tituloEpisodio;
    private Integer numeroEpisodio;
    private Double avaliacaoEpisodio;
    private LocalDate dataLancamentoEpisodio;

    public Integer getNumeroTemporada() {
        return numeroTemporada;
    }

    public void setNumeroTemporada(Integer numeroTemporada) {
        this.numeroTemporada = numeroTemporada;
    }

    public String getTituloEpisodio() {
        return tituloEpisodio;
    }

    public void setTituloEpisodio(String tituloEpisodio) {
        this.tituloEpisodio = tituloEpisodio;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public Double getAvaliacaoEpisodio() {
        return avaliacaoEpisodio;
    }

    public void setAvaliacaoEpisodio(Double avaliacaoEpisodio) {
        this.avaliacaoEpisodio = avaliacaoEpisodio;
    }

    public LocalDate getDataLancamentoEpisodio() {
        return dataLancamentoEpisodio;
    }

    public void setDataLancamentoEpisodio(LocalDate dataLancamentoEpisodio) {
        this.dataLancamentoEpisodio = dataLancamentoEpisodio;
    }

    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
        this.numeroTemporada = numeroTemporada;
        this.tituloEpisodio = dadosEpisodio.titulo();
        this.numeroEpisodio = dadosEpisodio.numero();

        try {
            this.avaliacaoEpisodio = Double.valueOf(dadosEpisodio.avaliacao());
        } catch (NumberFormatException ex) {
            this.avaliacaoEpisodio = 0.0;
        }

        try {
            this.dataLancamentoEpisodio = LocalDate.parse(dadosEpisodio.dataLancamento());
        } catch (DateTimeParseException ex) {
            this.dataLancamentoEpisodio = null;
        }
    }

    @Override
    public String toString() {
        return "numeroTemporada=" + numeroTemporada +
                ", tituloEpisodio='" + tituloEpisodio + '\'' +
                ", numeroEpisodio=" + numeroEpisodio +
                ", avaliacaoEpisodio=" + avaliacaoEpisodio +
                ", dataLancamentoEpisodio=" + dataLancamentoEpisodio;
    }
}
