package br.com.alura.www.screenmatch.service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;


public class ConsumoApi {

    private final HttpClient client;

    public ConsumoApi() throws KeyStoreException, IOException, CertificateException,
            NoSuchAlgorithmException, KeyManagementException {
        // Carregar o certificado SSL do servidor para um TrustStore
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        // Carregar o certificado do servidor
        X509Certificate cert = (X509Certificate) cf.generateCertificate(
                getClass().getResourceAsStream("/caminho/do/certificado.crt"));
        // Adicionar o certificado ao truststore
        trustStore.setCertificateEntry("server", cert);

        // Criar o TrustManagerFactory com o truststore
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        // Criar um SSLContext com o TrustManagerFactory
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());

        client = null;
    }

    public String obterDados(String endereco) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new IOException("Erro ao obter dados da API: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erro ao obter dados da API", e);
        }
    }
}