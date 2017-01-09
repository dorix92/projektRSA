package projektrsa;

import java.math.BigInteger;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Rsa {

    private BigInteger e;
    private BigInteger n;
    private BigInteger d;

    public Rsa() throws FileNotFoundException {

        
        Scanner sc = new Scanner(new FileReader("Keys.txt"));
        sc.next();
        n = new BigInteger(sc.next());
        sc.next();
        e = new BigInteger(sc.next());
        sc.next();
        d = new BigInteger(sc.next());
    }
    

    public synchronized BigInteger enc(BigInteger msg) {
        return msg.modPow(e, n); // msg^e(mod n) skorzystano z przykładu https://en.wikipedia.org/wiki/RSA_(cryptosystem) tam jest ten wzór

    }

    public synchronized BigInteger dec(BigInteger msg) {
        return msg.modPow(d, n); //msg^d(mod n)
    }

    public static void main(String[] args) throws FileNotFoundException {

    }
}
