package sample;

public class ChatServerModel {

    private int portNumber;
    private String userNameOne;
    private String userNameTwo;


    public String getPortNumber() {
        return String.valueOf(portNumber);
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = Integer.parseInt(portNumber);
    }

    public String getUserNameOne() {
        return userNameOne;
    }

    public void setUserNameOne(String userNameOne) {
        this.userNameOne = userNameOne;
    }

    public String getUserNameTwo() {
        return userNameTwo;
    }

    public void setUserNameTwo(String userNameTwo) {
        this.userNameTwo = userNameTwo;
    }

    public ChatServerModel(){

    }

}
