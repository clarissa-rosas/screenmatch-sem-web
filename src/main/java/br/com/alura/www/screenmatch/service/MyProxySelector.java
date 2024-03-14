package br.com.alura.www.screenmatch.service;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.SocketAddress;
import java.io.IOException;
import java.util.List;


public class MyProxySelector extends ProxySelector {

    private final String proxyHost;
    private final int proxyPort;

    public MyProxySelector(String proxyHost, int proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    @Override
    public List<Proxy> select(URI uri) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        return List.of(proxy);
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        throw new RuntimeException("Failed to connect to proxy: " + sa, ioe);
    }

    public static void register(String proxyHost, int proxyPort) {
        ProxySelector.setDefault(new MyProxySelector(proxyHost, proxyPort));
    }
}