package cosiRand;

import java.lang.management.ManagementFactory;
import java.util.Date;


public class randomNum{
	
final static int IM1 = 2147483563;
final static int IM2 = 2147483399;
final static double AM = (1.0/IM1);
final static int IMM1 = (IM1-1);
final static int  IA1 = 40014;
final static int IA2  = 40692;
final static int IQ1 = 53668;
final static int IQ2 = 52774;
final static int IR1 = 12211;
final static int IR2 = 3791;
final static int  NTAB = 32;
final static double  NDIV = (1+IMM1/NTAB);
final static double EPS = 1.2e-12;
final static double  RNMX = (1.0-EPS);
static long randseed;
public static double randomDouble(){
	return ran2(randseed);
}
public static void setRngSeed(long rseed){
	randseed = rseed;
}
public static long getRandSeed(){
	return randseed;
}
public static long seedRNG(){
	seedRandom();
	return randseed;
}
public static void seedRandom(){
	long tim,newSeed;
	long pid = getPid();
	pid = pid*65538;
	Date time = new Date();
	tim = time.getTime();
	newSeed = (long) Math.pow(pid,tim);
	newSeed *= (-1);
	randseed = newSeed;
	
}
private static double ran2(long idum){
	int j;
	long k;
	long idum2 = 123456789;
	long iy = 0;
	long[] iv = new long[NTAB];
	double temp;
	if (idum <= 0) {
        if ( -(idum) < 1) idum=1;
        else idum = -(idum);
        idum2=(idum);
        for (j=NTAB+7;j>=0;j--) {
                k=(idum)/IQ1;
                idum=IA1*(idum-k*IQ1)-k*IR1;
                if (idum < 0) idum += IM1;
                if (j < NTAB) iv[j] = idum;
        }
        iy=iv[0];
}
k=(idum)/IQ1;
idum=IA1*(idum-k*IQ1)-k*IR1;
if (idum < 0) idum += IM1;
k=idum2/IQ2;
idum2=IA2*(idum2-k*IQ2)-k*IR2;
if (idum2 < 0) idum2 += IM2;
j=(int) (iy/NDIV);
if (j == NTAB) j--;
iy=iv[j]-idum2;
iv[j] = idum;
if (iy < 1) iy += IMM1;
if ((temp=AM*iy) > RNMX) return RNMX;
else return temp;
}

private static long getPid(){
	String name = ManagementFactory.getRuntimeMXBean().getName();
	int p = name.indexOf('@');
	String pid = name.substring(0,p);
	return Long.parseLong(pid);
	
}
}
