package migration;

import cosiRand.ranBinom;
import coalescent.CoalescentMain;

public class migrationWorker {
	migrateRate migrations;
	static double lastRate;
	
	public migrationWorker(){
		lastRate = 0 ;
		migrations = null;
	}
	public void migrateAdd(int from, int to, double rate){
		migrateRate newMigRate = new migrateRate();
		newMigRate.setFromPop(from);
		newMigRate.setToPop(to);
		newMigRate.setRate(rate);
		newMigRate.setNext(migrations);
		migrations = newMigRate;
	}
	public migrateRate getMigrations(){
		return migrations;
	}
	public void migrateDelete(int from, int to){
		migrateRate tempMigRate = migrations;
		migrateRate delmemigrate;
		boolean done = false;
		if(tempMigRate == null) return;
		if(tempMigRate.getFromPop() == from && tempMigRate.getToPop() == to){
			migrations = migrations.getNext();
			done = true;
		}
		while(tempMigRate !=null && tempMigRate.getNext() != null && !done){
			if(tempMigRate.getNext().getFromPop() == from &&
					tempMigRate.getNext().getToPop() == to){
				delmemigrate = tempMigRate.getNext();
				tempMigRate.setNext(tempMigRate.getNext().getNext());
				done = true;
			}
			tempMigRate = tempMigRate.getNext();
		}
	}
	public double migrateGetRate(){
		int numNodes,numNodes1;
		migrateRate tempMigRate = migrations;
		double rate = 0;
		if(migrations == null)
			lastRate = 0;
		else{
			while (tempMigRate!=null){
				numNodes = CoalescentMain.dem.getNumNodesInPopByName(tempMigRate.getFromPop());
				numNodes1 = CoalescentMain.dem.getNumNodesInPopByName(tempMigRate.getToPop());
				if(numNodes<0 || numNodes1 <0){
					migrateDelete(tempMigRate.getFromPop(),tempMigRate.getToPop());
					return migrateGetRate();
				}
				else{
					rate+=numNodes * tempMigRate.getRate();
					tempMigRate = tempMigRate.getNext();
				}
			}
			lastRate = rate;
		}
		return lastRate;
		
	}
	public void migrateExecute(double gen){
		ranBinom random = new ranBinom();
		int numNodes;
		migrateRate tempMigRate = migrations;
		double rate = 0;
		double randCounter = random.getRandomDouble() * lastRate;
		
		if(migrations == null){
			System.out.println("Error in Migrate");
			
		}
		else{
			while(tempMigRate !=null && rate<randCounter){
				numNodes = CoalescentMain.dem.getNumNodesInPopByName(tempMigRate.getFromPop());
				rate += numNodes*tempMigRate.getRate();
				if(rate<randCounter)
					tempMigRate = tempMigRate.getNext();
				else
					CoalescentMain.dem.migrateOneChrom(tempMigRate.getFromPop(),tempMigRate.getToPop(), gen);
				
			}
		}
	}
}
