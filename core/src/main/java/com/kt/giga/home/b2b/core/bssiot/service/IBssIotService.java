package com.kt.giga.home.b2b.core.bssiot.service;

import com.kt.giga.home.b2b.domain.bssiot.receiver.*;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by 민우 on 2016-12-06.
 */
@SOAPBinding(parameterStyle=SOAPBinding.ParameterStyle.BARE)
@WebService(name="PS_SVCResProviderPort",targetNamespace="http://www.kt.com/m2m/domain/svcResProvider")
public interface IBssIotService {

    @WebMethod(action = "bizContInfoRst")
    @WebResult(name = "BizContInfoRstResponse", targetNamespace ="http://www.kt.com/m2m/domain/svcResProvider" , partName = "body")
    BizContractResponse bizContInfoRst(@WebParam(name = "BizContInfoRstRequest", targetNamespace = "http://www.kt.com/m2m/domain/svcResProvider", partName = "body") @XmlElement(required = true) @Valid BizContractRequest bizContractRequest);

    @WebMethod(action = "contSttusChgRst")
    @WebResult(name = "ContSttusChgRstResponse", targetNamespace ="http://www.kt.com/m2m/domain/svcResProvider" ,partName = "body")
    ContSttusChgRstResponse contSttusChgRst(@WebParam(name = "ContSttusChgRstRequest", targetNamespace = "http://www.kt.com/m2m/domain/svcResProvider", partName = "body") @XmlElement(required = true) ContSttusChgRstRequest contSttusChgRstRequest);

    @WebMethod(action = "preOpenRst")
    @WebResult(name = "PreOpenRstResponse", targetNamespace ="http://www.kt.com/m2m/domain/svcResProvider" , partName = "body")
    PreOpenRstResponse preOpenRst(@WebParam(name = "PreOpenRstRequest", targetNamespace ="http://www.kt.com/m2m/domain/svcResProvider", partName = "body") @XmlElement(required = true) PreOpenRstRequest preOpenRstRequest);

    @WebMethod(action = "useQntRst")
    @WebResult(name = "UseQntRstResponse", targetNamespace ="http://www.kt.com/m2m/domain/svcResProvider" , partName = "body")
    UseQntRstResponse useQntRst(@WebParam(name = "UseQntRstRequest", targetNamespace ="http://www.kt.com/m2m/domain/svcResProvider", partName = "body") @XmlElement(required = true) UseQntRstRequest useQntRstRequest);

}
