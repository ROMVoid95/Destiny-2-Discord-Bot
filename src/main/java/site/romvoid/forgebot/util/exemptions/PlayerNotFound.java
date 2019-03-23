package site.romvoid.forgebot.util.exemptions;

@SuppressWarnings("serial")
public class PlayerNotFound extends IndexOutOfBoundsException {

    public PlayerNotFound(String messageString) {
        super(messageString);
    }
    
    

}
