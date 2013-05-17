import java.util.Scanner;
import java.util.Random;
import java.util.LinkedList;

public class craps {

	public static int main(int amountToPlayWith){
		int bankAccount = 1000;
		boolean simulationMode = false;
		int simulationRounds = 50;

		int prize1 = 200;
		int prize2 = 300;
		int prize3 = 1000;
		int cost = 200;

		int choice1,choice2,choice3 = 0;

		if(!simulationMode){
			System.out.println("Welcome to Craps! Here, you will pick any 3 numbers between 2 and 12.\n " +
					"We will then roll the dice 3 times. \n" +
					"You win prizes based on how many rolls you correctly predict.");

			System.out.println();

			boolean valid1 = validation0(bankAccount, cost, simulationMode);
			if(valid1){
				boolean doubleCheck = false;
				while(doubleCheck == false){
					bankAccount = setBankAccount(amountToPlayWith);
					//doubleCheck = doublecheck(bankAccount);
					doubleCheck = true;
				}


				cost = (int) Math.round(bankAccount*.2);
				prize1= cost;
				prize2= (int) Math.round(prize1 * 1.5);
				if(prize1%2 == 1){
					prize2= (int) Math.round((prize1+1) * 1.5);
				}
				prize3 = prize1*2;
			}
			System.out.println("Based on your level of play, here are the parameters: "+
					"If you get 1 right, you recieve "+prize1+" dollars.\n" +
					"if you get 2 right, you recieve "+prize2+" dollars.\n" +
					"If you get 3 right, you receive "+prize3+" dollars \n" +
					"The cost to play is "+cost+" dollars.");
		}

		if(simulationMode){
			System.out.println("Welcome to Craps! Here, you will pick any 3 numbers between 2 and 12. \n " +
					"We will then roll the dice 3 times. \n" +
					"If you get 1 right, you recieve "+prize1+" dollars.\n" +
					"if you get 2 right, you recieve "+prize2+" dollars.\n" +
					"If you get 3 right, you receive "+prize3+" dollars \n" +
					"The cost to play is "+cost+" dollars.");

			System.out.println();
		}

		boolean valid = validation(bankAccount, cost, simulationMode);

		LinkedList<Integer> bankBalances = new LinkedList<Integer>();
		bankBalances.add(bankAccount);
		while(valid && !simulationMode){
			bankAccount = bankAccount - cost; 

			choice1= getChoice1();
			choice2= getChoice2(choice1);
			choice3= getChoice3(choice1, choice2);

			int[] guesses = {choice1, choice2, choice3};

			preRoll(choice1,choice2,choice3);
			int diceRoll1 = diceRoll();
			System.out.println("The first dice roll is: "+diceRoll1);
			int diceRoll2 = diceRoll();
			System.out.println("The second dice roll is: "+diceRoll2);
			int diceRoll3 = diceRoll();
			System.out.println("The third dice roll is: "+diceRoll3);

			System.out.println(".......");

			int[] rolls = {diceRoll1, diceRoll2, diceRoll3};

			int prize = winner(guesses, rolls, prize1,prize2,prize3);
			if(prize != 0){
				System.out.println("You win "+prize+" dollars! Congratulations.");
			}
			if(prize == 0){
				System.out.println("Not your lucky round!");
			}
			bankAccount += prize;
			bankBalances.add(bankAccount);
			System.out.println("This brings your cash on hand to: "+bankAccount);
			valid = validation2(bankAccount, cost);
		}

		//Simulation Mode for Testing
		while(simulationMode && (simulationRounds > 0) && ((bankAccount-cost)>0)){
			bankAccount = bankAccount - cost;
			choice1= getChoice1SIM();
			choice2= getChoice2SIM(choice1);
			choice3= getChoice3SIM(choice1, choice2);
			int[] guesses = {choice1, choice2, choice3};
			int diceRoll1 = diceRoll();
			int diceRoll2 = diceRoll();
			int diceRoll3 = diceRoll();
			int[] rolls = {diceRoll1, diceRoll2, diceRoll3};
			int prize = winner(guesses, rolls, prize1,prize2,prize3);
			bankAccount += prize;
			bankBalances.add(bankAccount);
			simulationRounds--;
		}

		System.out.println("Thank you for playing! Come back soon.");
		System.out.println("Final Bank Amount: "+bankAccount);
		System.out.println("Rounds: "+bankBalances.size());
		System.out.println(bankBalances);
		
		return bankAccount;
	}



	private static boolean doublecheck(int a) {
		boolean t = false;
		
		Scanner input = new Scanner(System.in);
		System.out.println("Right now, you have the bank account set at: "+a+"\n" +
				"Is this ok?");
		//boolean name = input.nextBoolean();
		String yes = input.next();
		if(yes.equals("yes") || yes.equals("Yes") || yes.equals("Y") || yes.equals("y") || yes.equals("true") || yes.equals("ok") || yes.equals("OK") || yes.equals("Ok") || yes.equals("sure") ||yes.equals("True") ||yes.equals("yeah")||yes.equals("Yeah")){
			t = true;
		}
		return t;
	}



	private static int setBankAccount(int a) {
		//Scanner yo = new Scanner(System.in);
		//System.out.println("How much money would you like to bring to the table?");
		//int account = yo.nextInt();
		
		int account = a;
		return account;
	}



	private static int getChoice1SIM() {
		Random dice = new Random();
		int dice1 = 14;

		while(dice1 < 2 || dice1 > 12){
			dice1 = dice.nextInt(14);
		}
		return dice1;
	}
	private static int getChoice2SIM(int choice1) {
		Random dice = new Random();
		int dice1 = 14;

		while((dice1 < 2 || dice1 > 12) && (dice1 != choice1)){
			dice1 = dice.nextInt(14);
		}
		return dice1;
	}

	private static int getChoice3SIM(int choice1, int choice2) {
		Random dice = new Random();
		int dice1 = 14;

		while((dice1 < 2 || dice1 > 12) && (dice1 != choice1) && (dice1 != choice2)){
			dice1 = dice.nextInt(14);
		}
		return dice1;
	}

	private static boolean validation0(int bankAccount, int cost, boolean simulationMode) {
		if(bankAccount - cost < 0 || simulationMode){
			return false;
		}
		boolean name = false;
		Scanner input = new Scanner(System.in);
		System.out.println("Would you like to play?");
		//boolean name = input.nextBoolean();
		String yes = input.next();
		if(yes.equals("yes") || yes.equals("Yes") || yes.equals("Y") || yes.equals("y") || yes.equals("true") || yes.equals("ok") || yes.equals("OK") || yes.equals("Ok") || yes.equals("sure") ||yes.equals("True") ||yes.equals("yeah")||yes.equals("Yeah")){
			name = true;
		}
		return name;
	}


	private static boolean validation(int bankAccount, int cost, boolean simulationMode) {
		if(bankAccount - cost < 0 || simulationMode){
			return false;
		}
		boolean name = false;
		Scanner input = new Scanner(System.in);
		System.out.println("Ready to go?");
		//boolean name = input.nextBoolean();
		String yes = input.next();
		if(yes.equals("yes") || yes.equals("Yes") || yes.equals("Y") || yes.equals("y") || yes.equals("true") || yes.equals("ok") || yes.equals("OK") || yes.equals("Ok") || yes.equals("sure") ||yes.equals("True") ||yes.equals("yeah")||yes.equals("Yeah")){
			name = true;
		}
		return name;
	}

	private static boolean validation2(int bankAccount, int cost) {
		if(bankAccount-cost < 0){
			return false;
		}
		boolean name = false;
		Scanner input = new Scanner(System.in);
		System.out.println("Would you like to play again? (true or false)");
		//boolean name = input.nextBoolean();
		String yes = input.next();
		if(yes.equals("yes") || yes.equals("Yes") || yes.equals("Y") || yes.equals("y") || yes.equals("true") || yes.equals("ok") || yes.equals("OK") || yes.equals("Ok") || yes.equals("sure") ||yes.equals("True") ||yes.equals("yeah")||yes.equals("Yeah")){
			name = true;
		}
		return name;
	}



	private static int getChoice1(){
		int choice1 = 13;
		while(choice1 > 12 || choice1 < 2){
			Scanner input = new Scanner(System.in);
			System.out.println("Please pick your first number:");
			choice1 = input.nextInt();
		}

		return choice1;
	}

	private static int getChoice2(int guess1){
		int choice2 = guess1;
		while(choice2 == guess1 || choice2 < 2 || choice2 > 12){
			Scanner input = new Scanner(System.in);
			System.out.println("Please pick your second number:");
			choice2 = input.nextInt();
		}
		return choice2;
	}

	private static int getChoice3(int guess1,int guess2){
		int choice3 = guess1;
		while(choice3 == guess1 || choice3 == guess2 || choice3 < 2 || choice3 >12){
			Scanner input = new Scanner(System.in);
			System.out.println("Please pick your third number:");
			choice3 = input.nextInt();
		}
		return choice3;
	}

	private static void preRoll(int choice1, int choice2, int choice3) {
		System.out.println();
		System.out.println("Your three guesses: "+choice1+", "+choice2+" and "+choice3);
		System.out.println("Let's start rolling!");
		System.out.println(".......");
	}


	private static int diceRoll() {
		Random diceX = new Random();
		Random diceY = new Random();

		int dice1 = 0;
		int dice2 = 0;

		while(dice1 < 1 || dice1 > 6){
			dice1 = diceX.nextInt(7);
		}
		while(dice2 < 1 || dice2 > 6){
			dice2 = diceY.nextInt(7);
		}

		return dice1+dice2;
	}

	private static int winner(int[] guesses, int[] rolls, int prize1, int prize2, int prize3) {
		int count = 0;
		for(int i = 0; i < guesses.length; i++){
			if (guesses[i] == rolls[0]){
				count ++;
			}
			if (guesses[i] == rolls[1]){
				count ++;
			}
			if (guesses[i] == rolls[2]){
				count ++;
			}
		}
		if (count==0){
			return 0;
		}
		if (count==1){
			return prize1;
		}
		if (count==2){
			return prize2;
		}
		if (count==3){
			return prize3;
		}
		return 20;
	}

}
