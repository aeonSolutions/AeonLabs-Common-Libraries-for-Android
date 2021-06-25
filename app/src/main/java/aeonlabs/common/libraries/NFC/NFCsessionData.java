package aeonlabs.common.libraries.NFC;


public class NFCsessionData {
    static public String ReadSmartCardAuthenticationString="";
    static public String ReadSmartCardDataString="";
    static public String ReadSmartCardFullMessageString="";
    static public String ReadSmartCardID="";
    static public Integer NFCState=-1;
    static public String smartCardEncryptionKey="5pRQVsgHK1VvqJBb";
    static public Integer smartCardMemorySize=504; // memory size in bytes

    public static void clearNFCdata(){
        ReadSmartCardAuthenticationString="";
        ReadSmartCardDataString="";
        ReadSmartCardFullMessageString="";
        ReadSmartCardID="";
        NFCState=-1;
    }
}
