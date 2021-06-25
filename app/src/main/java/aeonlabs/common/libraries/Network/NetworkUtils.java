package aeonlabs.common.libraries.Network;

import java.net.InetAddress;

public class NetworkUtils {
    public static Boolean checkDomainIsAlive(String address){
        return checkDomainIsAlive(address, false);
    }

    private static Boolean checkDomainIsAlive(String address, Boolean loopback){
        try {
            InetAddress ia = InetAddress.getByName(address);
            if(loopback==false) {
                return checkDomainIsAlive(null, true);
            }else{
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
    }
}
