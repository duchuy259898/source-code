
package connectispotapi;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "ConnectIspotApi", targetNamespace = "urn:ConnectIspotApi", wsdlLocation = "http://api-test.sosia.jp/hpb/ConnectIspotApi.php?wsdl")
public class ConnectIspotApi
    extends Service
{

    private final static URL CONNECTISPOTAPI_WSDL_LOCATION;
    private final static WebServiceException CONNECTISPOTAPI_EXCEPTION;
    private final static QName CONNECTISPOTAPI_QNAME = new QName("urn:ConnectIspotApi", "ConnectIspotApi");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://api-test.sosia.jp/hpb/ConnectIspotApi.php?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        CONNECTISPOTAPI_WSDL_LOCATION = url;
        CONNECTISPOTAPI_EXCEPTION = e;
    }

    public ConnectIspotApi() {
        super(__getWsdlLocation(), CONNECTISPOTAPI_QNAME);
    }

    public ConnectIspotApi(WebServiceFeature... features) {
        super(__getWsdlLocation(), CONNECTISPOTAPI_QNAME, features);
    }

    public ConnectIspotApi(URL wsdlLocation) {
        super(wsdlLocation, CONNECTISPOTAPI_QNAME);
    }

    public ConnectIspotApi(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, CONNECTISPOTAPI_QNAME, features);
    }

    public ConnectIspotApi(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ConnectIspotApi(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns ConnectIspotApiPortType
     */
    @WebEndpoint(name = "ConnectIspotApiPort")
    public ConnectIspotApiPortType getConnectIspotApiPort() {
        return super.getPort(new QName("urn:ConnectIspotApi", "ConnectIspotApiPort"), ConnectIspotApiPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ConnectIspotApiPortType
     */
    @WebEndpoint(name = "ConnectIspotApiPort")
    public ConnectIspotApiPortType getConnectIspotApiPort(WebServiceFeature... features) {
        return super.getPort(new QName("urn:ConnectIspotApi", "ConnectIspotApiPort"), ConnectIspotApiPortType.class, features);
    }

    private static URL __getWsdlLocation() {
        if (CONNECTISPOTAPI_EXCEPTION!= null) {
            throw CONNECTISPOTAPI_EXCEPTION;
        }
        return CONNECTISPOTAPI_WSDL_LOCATION;
    }

}
