
package com.kt.giga.home.b2b.domain.bssiot.sender;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.activenetwork.iam.ws.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.activenetwork.iam.ws.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CommonHeader }
     * 
     */
    public CommonHeader createCommonHeader() {
        return new CommonHeader();
    }

    /**
     * Create an instance of {@link SvcActvaRequest }
     * 
     */
    public SvcActvaRequest createSvcActvaRequest() {
        return new SvcActvaRequest();
    }

    /**
     * Create an instance of {@link SvcActvaReqInfo }
     * 
     */
    public SvcActvaReqInfo createSvcActvaReqInfo() {
        return new SvcActvaReqInfo();
    }

    /**
     * Create an instance of {@link SvcActvaResponse }
     * 
     */
    public SvcActvaResponse createSvcActvaResponse() {
        return new SvcActvaResponse();
    }

    /**
     * Create an instance of {@link ContSttusChgRequest }
     * 
     */
    public ContSttusChgRequest createContSttusChgRequest() {
        return new ContSttusChgRequest();
    }

    /**
     * Create an instance of {@link ContSttusChgReqInfo }
     * 
     */
    public ContSttusChgReqInfo createContSttusChgReqInfo() {
        return new ContSttusChgReqInfo();
    }

    /**
     * Create an instance of {@link ContSttusChgResponse }
     * 
     */
    public ContSttusChgResponse createContSttusChgResponse() {
        return new ContSttusChgResponse();
    }


}
