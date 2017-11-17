import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by mnlgu on 10/11/2017.
 */
public class RSA {

    //Euclid's
    //----------------------------------------------------------------------------
    private int euclid(int a, int b){
        if(a > b){
            //euclid
            if (b == 0)
                return a;
            else
                return euclid(b, a%b);
        }
        else{
            //exchange
            return euclid(b, a);
        }
    }

    private int[] extendedEuclid(int a, int b){
        int[] d_x_y_; //this is (d', x', y')
        int[] dxy = new int[3]; //this is (d, x, y)

        if (b == 0){
            // return (a, 1, 0)
            dxy[0] = a;
            dxy[1] = 1;
            dxy[2] = 0;
            return dxy;
        }
        else{
            // (d', x', y') = EE(b, a%b)
            d_x_y_ = extendedEuclid(b, a%b);
            //d = d'
            dxy[0] = d_x_y_[0];
            //x = y'
            dxy[1] = d_x_y_[2];
            //y = x'-[a/b]*y'
            dxy[2] = (int)(d_x_y_[1] - (Math.floor(a/b)) * d_x_y_[2]);

            return dxy;
        }
    }
    //----------------------------------------------------------------------------

    //modular equation linear solver
    //----------------------------------------------------------------------------
    private void modularLinearEquationSolver(int a, int b, int n){
        int[] aux = extendedEuclid(a, n);
        int d = aux[0];
        int x_ = aux[1];
        int y_ = aux[2];

        int x0 = 0;

        if ((d%b) == 0){
            x0 = x_ * ( (b/d) % n );
            for (int i=0; i<d-1; i++)
                System.out.println((x0 + i*(n/d)) % n);
        }
        else
            System.out.println("No possible answer");
    }
    //----------------------------------------------------------------------------

    //generates a random number
    //----------------------------------------------------------------------------
    private int generateRandom(){
        int minimum = 999;
        int maximum = 9999;

        int randomNum = ThreadLocalRandom.current().nextInt(minimum, maximum + 1);
        return randomNum;
    }
    //----------------------------------------------------------------------------

    //generates a random prime relative of phiN
    //----------------------------------------------------------------------------
    private int randomPrimeRelative(int phiN){

        boolean finish = false;

        int randomNum;

        do {
            //generates a random number
            randomNum = generateRandom();
            //if randomNum is odd and it is prime relative to phiN then we finish
            if(((randomNum%2) == 1) && (euclid(randomNum, phiN) == 1)){
                finish = true;
            }
        } while (!finish);

        return randomNum;
    }
    //----------------------------------------------------------------------------

    //prime number generator
    //----------------------------------------------------------------------------
    //generates a random prime number
    private int generateRandomPrimeNumber(){

        int randomNum;

        do {
            randomNum = generateRandom();
        }while (!isPrime(randomNum));

        return randomNum;
    }

    //checks if a number is prime
    private boolean isPrime(int inputNum){
        if (inputNum <= 3 || inputNum % 2 == 0)
            return inputNum == 2 || inputNum == 3; //this returns false if number is <=1 & true if number = 2 or 3
        int divisor = 3;
        while ((divisor <= Math.sqrt(inputNum)) && (inputNum % divisor != 0))
            divisor += 2; //iterates through all possible divisors
        return inputNum % divisor != 0; //returns true/false
    }
    //----------------------------------------------------------------------------

    //equivalence class
    //----------------------------------------------------------------------------
    //generates an equivalence class
    private int equivalenceClass(int a, int n, int k){
        int res = a + (k*n);
        return res;
    }

    //generates a positive equivalence class
    private int positiveEquivalenceClass (int a, int n){
        boolean neg = false;
        int aux;
        int k = 1;
        do {
            aux = equivalenceClass(a, n, k);
            if(aux <= 0){
                neg = true;
                k++;
            }
        }while (neg);
        return aux;
    }
    //----------------------------------------------------------------------------

    //getKeys
    //----------------------------------------------------------------------------
    public Key[] getKeys(){
        Key[] setOfKeys = new Key[2];

        //se escoje al azar p y q
        int p = generateRandomPrimeNumber();
        int q = generateRandomPrimeNumber();
        //n = pq
        int n = p*q;
        //phiN = (p-1)(q-1)
        int phiN = (p-1)*(q-1);
        //se escoje un numero impar entero y primo relativo de phiN
        int e = randomPrimeRelative(phiN);
        //calcular d tal que ed=1(mod phiN)
        //es lo mismo que el valor x de extendedEuclid
        int d = extendedEuclid(e, phiN)[1];
        //si d es negativo, se saca su clase de equivalencia positiva
        if(d < 0)
            d = positiveEquivalenceClass(d, phiN);

        Key publicKey = new Key(e, n);
        Key secretKey = new Key(d, n);

        setOfKeys[0] = publicKey;
        setOfKeys[1] = secretKey;

        return setOfKeys;
    }

    public Key[] getKeysWithDefinedValues(int p, int q, int e){
        Key[] setOfKeys = new Key[2];

        //n = pq
        int n = p*q;
        //phiN = (p-1)(q-1)
        int phiN = (p-1)*(q-1);
        //es lo mismo que el valor x de extendedEuclid
        int d = extendedEuclid(e, phiN)[1];
        //si d es negativo, se saca su clase de equivalencia positiva
        if(d < 0)
            d = positiveEquivalenceClass(d, phiN);

        Key publicKey = new Key(e, n);
        Key secretKey = new Key(d, n);

        setOfKeys[0] = publicKey;
        setOfKeys[1] = secretKey;

        return setOfKeys;
    }
    //----------------------------------------------------------------------------

    //RSA and Decryption
    //----------------------------------------------------------------------------
    //receives A's public key
    public long encryptInt(long toEncrypt, Key key){
        long encrypted = modularPower(toEncrypt, key.getE(), key.getN());
        return encrypted;
    }

    //receives A´s secret key
    public long decryptInt(long toDecrypt, Key key){
        long decrypted = modularPower(toDecrypt, key.getE(), key.getN());
        return decrypted;
    }

    //m^e%n
    //el valor a encriptar no puede ser mayor que el valor de n
    public long modularPower(long a, int e, int n){
        long d = 1;
        String eBinary = Integer.toBinaryString(e);
        int k = (eBinary.length()-1);

        for(int i = k; i >= 0; i--){
            //System.out.println("i: " + (i));
            d = (d*d)%n;
            //System.out.println("binario: "+ eBinary.charAt(k-i));
            if(eBinary.charAt(k-i) == ('1')){
                d = d*a % n;
                //System.out.println("si entre");
            }
        }
        return d;
    }
    //----------------------------------------------------------------------------

    //Base 26
    //----------------------------------------------------------------------------
    public long encryptBase26(String toConvert){
        String aux = toConvert;
        long result = 0;

        int mult = 0;

        int len = aux.length()-1;
        for(int i = len; i >= 0; i--){
            switch (aux.charAt(len-i)){
                case 'a':case 'A':
                    mult = 0;
                    break;
                case 'b':case 'B':
                    mult = 1;
                    break;
                case 'c':case 'C':
                    mult = 2;
                    break;
                case 'd':case 'D':
                    mult = 3;
                    break;
                case 'e':case 'E':
                    mult = 4;
                    break;
                case 'f':case 'F':
                    mult = 5;
                    break;
                case 'g':case 'G':
                    mult = 6;
                    break;
                case 'h':case 'H':
                    mult = 7;
                    break;
                case 'i':case 'I':
                    mult = 8;
                    break;
                case 'j':case 'J':
                    mult = 9;
                    break;
                case 'k':case 'K':
                    mult = 10;
                    break;
                case 'l':case 'L':
                    mult = 11;
                    break;
                case 'm':case 'M':
                    mult = 12;
                    break;
                case 'n':case 'N':case 'ñ':case 'Ñ':
                    mult = 13;
                    break;
                case 'o':case 'O':
                    mult = 14;
                    break;
                case 'p':case 'P':
                    mult = 15;
                    break;
                case 'q':case 'Q':
                    mult = 16;
                    break;
                case 'r':case 'R':
                    mult = 17;
                    break;
                case 's':case 'S':
                    mult = 18;
                    break;
                case 't':case 'T':
                    mult = 19;
                    break;
                case 'u':case 'U':
                    mult = 20;
                    break;
                case 'v':case 'V':
                    mult = 21;
                    break;
                case 'w':case 'W':
                    mult = 22;
                    break;
                case 'x':case 'X':
                    mult = 23;
                    break;
                case 'y':case 'Y':
                    mult = 24;
                    break;
                case 'z':case 'Z':
                    mult = 25;
                    break;
            }
            System.out.println("letra: " + mult + "*26^" + i);
            result += mult*(Math.pow(26, i));
        }
        return result;
    }

    public String decryptBase26(long toConvert){
        String result = "";
        long remainder = 0;
        long quotient = toConvert;
        long aux = quotient;
        char mult = Character.MIN_VALUE;

        do{
            quotient = aux/26;
            remainder = aux%26;
            aux = quotient;

            switch ((int) remainder){
                case 0:
                    mult = 'A';
                    break;
                case 1:
                    mult = 'B';
                    break;
                case 2:
                    mult = 'C';
                    break;
                case 3:
                    mult = 'D';
                    break;
                case 4:
                    mult = 'E';
                    break;
                case 5:
                    mult = 'F';
                    break;
                case 6:
                    mult = 'G';
                    break;
                case 7:
                    mult = 'H';
                    break;
                case 8:
                    mult = 'I';
                    break;
                case 9:
                    mult = 'J';
                    break;
                case 10:
                    mult = 'K';
                    break;
                case 11:
                    mult = 'L';
                    break;
                case 12:
                    mult = 'M';
                    break;
                case 13:
                    mult = 'N';
                    break;
                case 14:
                    mult = 'O';
                    break;
                case 15:
                    mult = 'P';
                    break;
                case 16:
                    mult = 'Q';
                    break;
                case 17:
                    mult = 'R';
                    break;
                case 18:
                    mult = 'S';
                    break;
                case 19:
                    mult = 'T';
                    break;
                case 20:
                    mult = 'U';
                    break;
                case 21:
                    mult = 'V';
                    break;
                case 22:
                    mult = 'W';
                    break;
                case 23:
                    mult = 'X';
                    break;
                case 24:
                    mult = 'Y';
                    break;
                case 25:
                    mult = 'Z';
                    break;
            }
            result += mult;

        }while(quotient != 0);

        result = new StringBuilder(result).reverse().toString();

        return result;
    }
    //----------------------------------------------------------------------------
}