package com.kt.giga.home.b2b.service;

import com.kt.giga.home.b2b.domain.bssiot.sender.ContSttusChgRequest;
import com.kt.giga.home.b2b.domain.bssiot.sender.ContSttusChgResponse;
import com.kt.giga.home.b2b.domain.bssiot.sender.SvcActvaRequest;
import com.kt.giga.home.b2b.domain.bssiot.sender.SvcActvaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpUrlConnection;

import javax.jws.WebService;
import java.net.URI;
import java.util.Optional;

/**
 * Created by alsdnfl on 2017-02-06.
 */
@Slf4j(topic = "com.kt.giga.home.b2b.bssiot")
@Service
@WebService(endpointInterface = "com.kt.giga.home.b2b.service.IBssIotSenderWebService", name = "PS_SVC1000ProviderPort", targetNamespace = "http://www.kt.com/m2m/provider/svc1000Provider")
public class BssIotSenderWebService extends WebServiceGatewaySupport implements IBssIotSenderWebService {

    private int readTimeout       = 10000;
    private int connectionTimeout = 5000;

    @Autowired
    private DiscoveryClient discoveryClient;

    private WebServiceTemplate webServiceTemplate;

    public BssIotSenderWebService() {
    }

    @Autowired
    public BssIotSenderWebService(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public SvcActvaResponse svcActva(SvcActvaRequest svcActvaRequest) {

        String URL = getUrl();

        log.debug("bssUrl : {}", URL);
        log.info("Requesting to BssIoT : {}", svcActvaRequest);

        SvcActvaResponse svcActvaResponse = (SvcActvaResponse) webServiceTemplate.marshalSendAndReceive(URL, svcActvaRequest, message -> {
            ((SoapMessage) message).setSoapAction("svcActva");
            TransportContext  context    = TransportContextHolder.getTransportContext();
            HttpUrlConnection connection = ((HttpUrlConnection) context.getConnection());
            connection.getConnection().setConnectTimeout(connectionTimeout);
            connection.getConnection().setReadTimeout(readTimeout);
            connection.getConnection().setRequestProperty("Content-Type", "application/xml");
        });

        log.info("Response from BssIoT : {}", svcActvaResponse);
        return svcActvaResponse;
    }

    public ContSttusChgResponse contSttusChg(ContSttusChgRequest contSttusChgRequest) {

        String URL = getUrl();

        log.info("bssUrl : {}", URL);
        log.info("Requesting to BssIoT : {}", contSttusChgRequest);

        ContSttusChgResponse contSttusChgResponse = (ContSttusChgResponse) webServiceTemplate.marshalSendAndReceive(URL, contSttusChgRequest, message -> {
            ((SoapMessage) message).setSoapAction("contSttusChg");
            TransportContext  context    = TransportContextHolder.getTransportContext();
            HttpUrlConnection connection = ((HttpUrlConnection) context.getConnection());
            connection.getConnection().setConnectTimeout(connectionTimeout);
            connection.getConnection().setReadTimeout(readTimeout);
            connection.getConnection().setRequestProperty("Content-Type", "application/xml");
        });

        log.info("Response from BssIoT : {}", contSttusChgResponse);
        return contSttusChgResponse;
    }

    private String getUrl() {
        Optional<ServiceInstance> b2BGateway = discoveryClient.getInstances("B2BGateway").stream().findFirst();

        URI uri = null;
        if (b2BGateway.isPresent()) {
            uri = b2BGateway.get().getUri();
        }

        if (uri == null) {
            throw new RuntimeException("Gateway not available");
        }

        return uri.toString() + "/crm/services/svc1000ProviderWS";
    }
}
