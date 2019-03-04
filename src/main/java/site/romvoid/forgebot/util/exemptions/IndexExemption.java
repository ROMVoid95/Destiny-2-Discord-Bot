package site.romvoid.forgebot.util.exemptions;

public class IndexExemption extends IndexOutOfBoundsException {

    private static final long serialVersionUID = 1L;
    
    public IndexExemption() {
        
        super();
    }
 
    public IndexExemption(String messageString) {
        
        super(messageString);
    }
    
    

}
