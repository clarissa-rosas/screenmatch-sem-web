package br.com.alura.www.screenmatch.principal;

import br.com.alura.www.screenmatch.model.DadosEpisodio;
import br.com.alura.www.screenmatch.model.DadosSerie;
import br.com.alura.www.screenmatch.model.DadosTemporada;
import br.com.alura.www.screenmatch.model.Episodio;
import br.com.alura.www.screenmatch.service.ConsumoApi;
import br.com.alura.www.screenmatch.service.ConverteDados;
import br.com.alura.www.screenmatch.service.MyProxySelector;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=c8d89cdb";

    public Principal() {
        MyProxySelector.register("xx.xx.xxx.xx", xxxx);
    }

    public void exibeMenu() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

//        for(int i = 0; i < dados.totalTemporadas(); i++){
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for(int j = 0; j< episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 5 melhores episódios:");
        dadosEpisodios
                .stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("A partir de que ano você quer ver os episódios?");
        var anoBusca = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(anoBusca, 1, 1);

        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancamentoEpisodio() != null && e.getDataLancamentoEpisodio().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getNumeroTemporada() +
                                "Número do episódio: " + e.getNumeroEpisodio() +
                                "Data de lançamento: " + e.getDataLancamentoEpisodio().format(formatadorData)
                        ));

        System.out.println("Digite um trecho do título do episódio a ser buscado");
        var trechoBuscado = leitura.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTituloEpisodio().toUpperCase().contains(trechoBuscado.toUpperCase()))
                .findFirst();
        if (episodioBuscado.isPresent()) {
            System.out.println("Episódio encontrado!"
                    + "\nTítulo: " + episodioBuscado.get().getTituloEpisodio()
                    + "\nTemporada: " + episodioBuscado.get().getNumeroTemporada()
                    + "\nEpisódio: " + episodioBuscado.get().getNumeroEpisodio());
        } else {
            System.out.println("Episódio não encontrado!");
        }

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacaoEpisodio() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getNumeroTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacaoEpisodio)));
        System.out.println("Avaliações por temporada");
        avaliacoesPorTemporada.forEach
                ((temporada, mediaAvaliacoes) -> System.out.println("Temporada " + temporada + ": " + mediaAvaliacoes));

        Optional<Integer> melhorTemporada = avaliacoesPorTemporada.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

        Optional<Integer> piorTemporada = avaliacoesPorTemporada.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacaoEpisodio() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacaoEpisodio));
        System.out.println("Média das avaliações: " + est.getAverage() +
                "\nMelhor temporada: " + (melhorTemporada.isPresent() ? melhorTemporada.get() : "N/A") +
                "\nPior temporada: " + (piorTemporada.isPresent() ? piorTemporada.get() : "N/A") +
                "\nMelhor episódio: " + est.getMax() +
                "\nPior episódio: " + est.getMin()  +
                "\nTotal de episódios: " + est.getCount());

//        List<String> episodiosFiltrados = episodios.stream()
//                .filter(e -> e.getTituloEpisodio().toUpperCase().contains(trechoBuscado.toUpperCase()))
//                .collect(Collectors.toList());
//                }
//
//        // Exibindo os episódios filtrados
//        episodiosFiltrados.forEach(System.out::println);
    }
}
