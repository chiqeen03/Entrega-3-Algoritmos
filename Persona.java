/**
 * Created by mnlgu on 28/11/2017.
 */
public class Persona {

    private Key[] llaves;

    public Persona(){
        RSA aux = new RSA();
        //llaves = new Key[2];
        this.llaves = aux.getKeys();
    }

    public String getPublic(){
        return ("("+llaves[0].getE() + ", " + llaves[0].getN()+")");
    }

    public String getSecret(){
        return ("("+llaves[1].getE() + ", " + llaves[1].getN()+")");
    }

    public Key getPublicKey(){
        return llaves[0];
    }
    public Key getSecretKey(){
        return llaves[1];
    }
}
