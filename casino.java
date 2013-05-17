import java.text.NumberFormat;
import java.util.Scanner;
import java.util.LinkedList;

public class casino {
	String[] arg;
	public static void main(String[] args){
		System.out.println("* * * * * * * * * * * * * * * * * * *");
		System.out.println("Welcome to the Casino! \n");

		int bankAccount = bankRequest();
		boolean wantsToPlay = true;
		int initialAmount = bankAccount;

		while(wantsToPlay){
			int choice = welcome();
			if(choice == 0){
				System.out.println("\n* * * * * * * * * * * * * * * * * * *\n");
				craps CRAPS = new craps();
				int toCraps = tableMoney(bankAccount);
				int amountLeft = bankAccount - toCraps;
				int resultCraps = CRAPS.main(toCraps);
				bankAccount = amountLeft + resultCraps;
			}
			else if(choice == 1){
				System.out.println("\n* * * * * * * * * * * * * * * * * * *\n");
				blackjack BLACKJACK = new blackjack();
				int toBJ = tableMoney(bankAccount);
				int amountLeft = bankAccount - toBJ;
				int resultBlackJack = BLACKJACK.main(toBJ);
				bankAccount = amountLeft + resultBlackJack;
			}
			else if(choice == 2){
				System.out.println("\n* * * * * * * * * * * * * * * * * * *\n");
				powerBall POWERBALL = new powerBall();
				int toPOWER = tableMoney(bankAccount);
				int amountLeft = bankAccount - toPOWER;
				int resultPOWERBALL = POWERBALL.main(toPOWER);
				bankAccount = amountLeft + resultPOWERBALL;
			}
			else if(choice == 3){
				System.out.println("\n* * * * * * * * * * * * * * * * * * *\n");
				indianPoker INDIANPOKER = new indianPoker();
				int toINDIANPOKER = tableMoney(bankAccount);
				int amountLeft = bankAccount - toINDIANPOKER;
				int resultINDIANPOKER = INDIANPOKER.main(toINDIANPOKER);
				bankAccount = amountLeft + resultINDIANPOKER;
			}
			
			System.out.println("\n* * * * * * * * * * * * * * * * * * *\n");
			System.out.println("Welcome bank to the Casino!");
			System.out.println("We hope you had fun. Your current bank balance is "+bankAccount);

			double netAmount = bankAccount-initialAmount;
			
			if(netAmount > 0){
				System.out.println("Wow, you're up by "+netAmount+" dollars. Lady luck seems to be on your side!");
			}
			else if(netAmount == 0){
				System.out.println("Nice you're net even!");
			}
			else if(netAmount < 0 && bankAccount != 0){
				System.out.println("Time to turn your luck around! You're down by "+(netAmount*-1)+" dollars");
			}
			wantsToPlay = validation();
			if(bankAccount <= 0){
				System.out.println("Shucks! You have insufficient funds to continue.");
				wantsToPlay = false;
			}
		}
		System.out.println("\n* * * * * * * * * * * * * * * * * * *\n");
		System.out.println("Thanks for playing in the Casino! Hope you see you again real soon.");
	}

	private static boolean validation() {
		boolean name = false;
		Scanner input = new Scanner(System.in);
		System.out.println("Would you like to go for another round in the Casino?");
		//boolean name = input.nextBoolean();
		String yes = input.next();
		if(yes.equals("yes") || yes.equals("Yes") || yes.equals("Y") || yes.equals("y") || yes.equals("true") || yes.equals("ok") || yes.equals("OK") || yes.equals("Ok") || yes.equals("sure") ||yes.equals("True") ||yes.equals("yeah")||yes.equals("Yeah")){
			name = true;
		}
		return name;
	}

	private static int tableMoney(int bankAccount) {
		boolean doubleCheck = false;
		int output = 0;
		while(!doubleCheck){
			int upperBound = bankAccount;
			int lowerBound = 0;
			do{
				Scanner input = new Scanner(System.in);
				System.out.println("How much money would you like to bring to the table?");
				output = input.nextInt();
			}while(output<lowerBound || output > upperBound);

			Scanner input2 = new Scanner(System.in);
			System.out.println("Are you sure you would like to bring "+output+" dollars to the table?");
			String yes = input2.next();
			if(yes.equals("yes") || yes.equals("Yes") || yes.equals("Y") || yes.equals("y") || yes.equals("true") || yes.equals("ok") || yes.equals("OK") || yes.equals("Ok") || yes.equals("sure") ||yes.equals("True") ||yes.equals("yeah")||yes.equals("Yeah")){
				doubleCheck = true;
			}
		}
		return output;
	}

	private static int bankRequest() {
		boolean doubleCheck = false;
		int output = 0;
		while(!doubleCheck){
			Scanner input = new Scanner(System.in);
			System.out.println("How much money would you like to bring to the Casino?");
			output = input.nextInt();

			Scanner input2 = new Scanner(System.in);
			System.out.println("Are you sure you would like to bring "+output+" dollars to the casino?");
			String yes = input2.next();
			if(yes.equals("yes") || yes.equals("Yes") || yes.equals("Y") || yes.equals("y") || yes.equals("true") || yes.equals("ok") || yes.equals("OK") || yes.equals("Ok") || yes.equals("sure") ||yes.equals("True") ||yes.equals("yeah")||yes.equals("Yeah")){
				doubleCheck = true;
			}
		}
		return output;

	}

	private static int welcome() {
		String output = "";
		System.out.println("\n* * * * * * * * * * * * * * * * * * *");
		System.out.println("\nToday you can play either blackjack, craps, powerball, or indian poker.");
		Scanner input = new Scanner(System.in);
		System.out.println("Which would you like to play?");
		output = input.next();
		
		LinkedList<String> craps = new LinkedList<String>();
		addCraps(craps);
		
		LinkedList<String> blackjack = new LinkedList<String>();
		addBlackJack(blackjack);
		
		LinkedList<String> powerball = new LinkedList<String>();
		addPowerBall(powerball);
		
		LinkedList<String> indianpoker = new LinkedList<String>();
		addIndianPoker(indianpoker);
		
		if(craps.contains(output)){
			return 0;
		}
		else if(blackjack.contains(output)){
			return 1;
		}
		else if(powerball.contains(output)){
			return 2;
		}
		else if(indianpoker.contains(output)){
			return 3;
		}
		return 3;
	}

	private static void addIndianPoker(LinkedList<String> indianpoker) {
		indianpoker.add("indian poker");
		indianpoker.add("Indian Poker");
		indianpoker.add("indianpoker");
		indianpoker.add("INDIAN POEKR");
		indianpoker.add("Indianpoker");
		indianpoker.add("IndianPoker");
		indianpoker.add("INDIANPOKER");
		indianpoker.add("indian poekr");
		indianpoker.add("i");
		indianpoker.add("ip");
	}

	private static void addPowerBall(LinkedList<String> powerball) {
		powerball.add("powerball");
		powerball.add("p");
		powerball.add("pb");
		powerball.add("P");
		powerball.add("power ball");
		powerball.add("pwoerball");
		powerball.add("Powerball");
		powerball.add("Power Ball");
	}

	private static void addBlackJack(LinkedList<String> blackjack) {
		blackjack.add("BJ");
		blackjack.add("Blackjack");
		blackjack.add("blackjack");
		blackjack.add("black jack");
		blackjack.add("Black Jack");
		blackjack.add("Black jack");
		blackjack.add("blackJack");
		blackjack.add("black Jack");
		blackjack.add("b");
		blackjack.add("bj");
	}

	private static void addCraps(LinkedList<String> craps) {
		craps.add("craps"); 
		craps.add("Xraps"); 
		craps.add("xraps"); 
		craps.add("Craps"); 
		craps.add("crap");
		craps.add("c");
	}
}
