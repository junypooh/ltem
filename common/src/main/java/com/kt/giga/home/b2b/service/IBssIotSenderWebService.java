package com.kt.giga.home.b2b.service;

import com.kt.giga.home.b2b.domain.bssiot.sender.ContSttusChgRequest;
import com.kt.giga.home.b2b.domain.bssiot.sender.ContSttusChgResponse;
import com.kt.giga.home.b2b.domain.bssiot.sender.SvcActvaRequest;
import com.kt.giga.home.b2b.domain.bssiot.sender.SvcActvaResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.WebServiceClient;

/**
 * Created by alsdnfl on 2017-02-06.
 */
@WebServiceClient(targetNamespace = "http://www.kt.com/m2m/domain/svc1000Provider", name = "PS_SVC1000ProviderPort")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface IBssIotSenderWebService {

//    @WebMethod(action = "svcActva")
//    @WebResult(name = "SvcActvaResponse", targetNamespace = "http://www.kt.com/m2m/domain/svc1000Provider", partName = "body")
    SvcActvaResponse svcActva(@WebParam(partName = "body", name = "SvcActvaRequest", targetNamespace = "http://www.kt.com/m2m/domain/svc1000Provider") @XmlElement(required = true) SvcActvaRequest svcActvaRequest);


    @WebMethod(action = "contSttusChg")
    @WebResult(name = "ContSttusChgResponse", targetNamespace = "http://www.kt.com/m2m/domain/svc1000Provider", partName = "body")
    ContSttusChgResponse contSttusChg(@WebParam(partName = "body", name = "ContSttusChgRequest", targetNamespace = "http://www.kt.com/m2m/domain/svc1000Provider") @XmlElement(required = true) ContSttusChgRequest contSttusChgRequest);
}
