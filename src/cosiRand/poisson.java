package cosiRand;

import java.util.Random;

public class poisson {
	static Random generator = new Random();
	public static int poission(double xm){
		double em,t,y,sq = 0,alxm = 0,g = 0,oldm;
		oldm = -1.0;
		if(xm<12.0){
			if(xm!=oldm){
				oldm = xm;
				g = Math.exp(-xm);
			}
			em = -1.0;
			t = 1.0;
			do{
				em += 1.0;
				t *= generator.nextDouble();
			}while(t>g);
		}
		else{
			if(xm != oldm){
				oldm = xm;
				sq = Math.sqrt(2.0*xm);
				alxm = Math.log(xm);
				g = xm*alxm - gammaLn.gammLn(xm + 1.0);
			}
			do{
				do{
					y = Math.tan(Math.PI * generator.nextDouble());
					em = sq*y + xm;
					
				}while(em < 0);
				em = Math.floor(em);
				t = .9 * (1.0 + y*y)* Math.exp(em*alxm - gammaLn.gammLn(em + 1.0)-g);
			}while (generator.nextDouble() > t);
		}
		return (int) (em+.5);
		
	}
	public static double poissonGetNext(double rate){
		double ed;
		if(rate ==0) return -1;
		//ed = expDev();
		return (expDev()/rate);
		
	}
	private static double expDev(){
		double dum = 0;
		while(dum == 0.0){
			dum = (double) 1 - generator.nextDouble();
		}
		return - Math.log(dum);
	}
	
}
