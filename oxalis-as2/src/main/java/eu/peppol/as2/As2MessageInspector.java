package eu.peppol.as2;

import javax.security.auth.x500.X500Principal;

/**
 * @author steinar
 *         Date: 08.10.13
 *         Time: 11:09
 */
public class As2MessageInspector {



    public static SignedMimeMessageInspector validate(As2Message as2Message) throws InvalidAs2MessageException {


        SignedMimeMessageInspector SignedMimeMessageInspector = new SignedMimeMessageInspector(as2Message.getMimeMessage());

        compareAs2FromHeaderWithCertificateCommonName(as2Message, SignedMimeMessageInspector);

        // TODO: compare the value of the AS2-To: header with the CN attribute of our own certificate for equality

        return SignedMimeMessageInspector;
    }


    /** Compares the value of the "AS2-From" header with the value of the CN= attribute of the inbound certificate. */
    private static void compareAs2FromHeaderWithCertificateCommonName(As2Message as2Message, SignedMimeMessageInspector SignedMimeMessageInspector) throws InvalidAs2MessageException {

        // Retrieves the CN=AP_......, O=X......, C=.... from the certificate
        X500Principal x500Principal = SignedMimeMessageInspector.getSignersX509Certificate().getSubjectX500Principal();

        // Verifies that the value of AS2-From header equals the value of the CN attribute from the signers certificate
        As2SystemIdentifier as2SystemIdentifierFromCertificate = new As2SystemIdentifier(x500Principal);
        if (!as2SystemIdentifierFromCertificate.equals(as2Message.getAs2From())) {
            throw new InvalidAs2MessageException("The signers CN '" + as2SystemIdentifierFromCertificate.toString() + "'does not compare to the AS2-From header '" + as2Message.getAs2From().toString()+"'");
        }
    }


}