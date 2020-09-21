package sample;

public class ChatServerModel {

    String ipAddress;
    int portNumber;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPortNumber() {
        return String.valueOf(portNumber);
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = Integer.parseInt(portNumber);
    }

    public ChatServerModel(){

    }

}
