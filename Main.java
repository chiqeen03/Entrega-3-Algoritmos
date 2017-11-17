/**
 * Created by mnlgu on 10/11/2017.
 */
public class Main {
    public static void main (String[] args){
        RSA eu = new RSA();

        //System.out.println(eu.euclides(11, 7));

        /*int[] aux = eu.extendedEuclid(13, 60);
        for(int i=0; i<aux.length; i++){
            System.out.println(aux[i]);
        }*/

        /*
        int phiN = 20;
        int randomPrimeRelative = eu.randomPrimeRelative(phiN);
        System.out.println("random prime relative of " + phiN + " is: " + randomPrimeRelative);
        System.out.println("euclides of " + phiN + " with " + randomPrimeRelative + " is: " + eu.euclid(randomPrimeRelative, phiN));
        */

        //System.out.println(eu.generateRandomPrimeNumber());

        //System.out.println(Long.MAX_VALUE);
        //System.out.println(Integer.toBinaryString(2048));

        //System.out.println(/*"res: " + */eu.modularPower(2, 6, 5));

        //System.out.println("Res: "+eu.encryptBase26("TeC"));

        //System.out.println("Res: " + eu.decryptBase26(21166));
    }
}
