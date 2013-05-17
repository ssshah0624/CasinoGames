import java.util.Iterator;
import java.util.Scanner;
import java.util.Random;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Set;



public class blackjack {
	//CONTROL PANEL
	static boolean simulationMode = false;
	static int players = 2;
	static int whichUser = 2; //player 1 is dealer by default!
	static LinkedList cardTracker = new LinkedList();
	static boolean dealerReveal = false;
	static int bankAccount = 1000;
	static int cost = 200;
	static boolean scoreHelper = true;

	//END CONTROL PANEL

	public static int main(int bankAccount){
		int score = 0;
		System.out.println("\n* * * * * * * * * * * * * * * * * * *");
		System.out.println("* * * * * * * * BLACK JACK* * * * * * * * *");
		System.out.println("* * * * * * * * * * * * * * * * * * *\n");
		System.out.println("Welcome to BlackJack! \n" +
				"The objective of the game is to beat the dealer. \n" +
				"The dealer will hit below 16. \n"+
				"The cost to play is "+cost +"\n"+
				"Best of luck!");
		System.out.println();

		//int bankAccount = bankSetup();
		//int cost = Math.round(bankAccount/5);

		System.out.println("Starting bank balance: "+bankAccount);

		System.out.println();

		boolean valid = validation(simulationMode, players);
		boolean broke = false;
		HashMap<Integer, HashMap<Integer,String>> deck = setUpDeck();

		while(valid){
			bankAccount -= cost; 
			boolean win = false;
			//System.out.println("Bank Balance: "+bankAccount);
			System.out.println();
			HashMap<Integer,HashMap<Integer,String>> dealtCards = dealCards(players, deck);
			LinkedList<Integer> dealerHand = new LinkedList<Integer>();
			LinkedList<Integer> userHand = informUser(dealtCards, whichUser,dealerHand);
			score = getScore(userHand, score);
			if (scoreHelper){
				System.out.println("Your current score is: "+score);
			}
			System.out.println();
			if(dealerReveal){
				System.out.println("Dealers Hand:" +dealerHand);
			}
			boolean hit = false;
			if(score == 21){
				win = true;
				hit = false;
			}

			if(!win){
				hit = hitVerify();
			}
			boolean stick1 = false;
			while(!hit && !stick1 && !win){
				stick1 = confirmStick(score);
				if(!stick1){
					hit = hitVerify();
				}
			}
			if(score == 21 && win){
				System.out.println("BLACKJACK!");
			}
			boolean bust = false;
			boolean stick = false;
			while(hit && !bust && !win){
				int count = hit(dealtCards, deck);
				userHand = fixUserHand(userHand, dealtCards, count);
				score = getScore(userHand,score);
				if(scoreHelper){
					System.out.println("Your score: "+score);
				}
				bust = bustManage(score);
				if(bust == true){
					System.out.println("bust!");
					win = false;
				}
				if(bust == false){
					hit = hitVerify();
				}
				if(!bust && !hit){
					stick = confirmStick(score);
					if(!stick){
						hit = hitVerify();
					}
				}
				if(stick){
					break;
				}
			}
			if(bust){
				win = false;
				System.out.println("You LOSE!");
			}
			if(!bust){
				System.out.println("You decided to STICK with: "+score);
				System.out.println("----------------------------------- \n");
				System.out.println("Let's check out the dealers hand...\n");
				dealerCardsReveal(dealtCards);
				int dealerScore = getDealerScore(dealerHand);
				boolean dealerBust = false;
				while(dealerScore < 16 && !dealerBust){
					System.out.println("Dealer has decided to HIT");
					int count = hit(dealtCards, deck);
					dealerHand = fixDealerHand(dealerHand, dealtCards, count);
					dealerScore = getDealerScore(dealerHand);
					System.out.println("Dealers score: "+dealerScore);
					dealerBust = bustManage(dealerScore);
				}
				if(!dealerBust){
					System.out.println("Dealer has decided to STICK with a score of " +dealerScore);
				}
				if(dealerBust){
					System.out.println("Dealer has BUSTED.");
				}
				System.out.println("----------------------------------- \n");
				if(score == dealerScore && !dealerBust){
					System.out.println("TIE! "+cost+" dollars has been added to your bank balance.");
					bankAccount+=cost;
					System.out.println("----------------------------------- \n");
				}
				if(score < dealerScore && !dealerBust){
					System.out.println("You LOSE!");
					win = false;
					System.out.println("----------------------------------- \n");
				}
				if(score > dealerScore || dealerBust){
					System.out.println("You WIN! "+(2*cost)+" dollars has been added to your bank balance.");
					bankAccount += 2*cost;
					win = true;
					System.out.println("----------------------------------- \n");
				}
			} //end not bust

			System.out.println("Bank Balance:" +bankAccount);
			System.out.println("----------------------------------- \n");
			valid = validation2(simulationMode, players, cost);
			if(bankAccount < cost){
				broke = true;
				valid = false;
			}
		}
		if(broke){
			System.out.println("Shucks! You have insufficient funds for BlackJack.");
		}
		System.out.println("Thank you for playing BlackJack! Hope you see you again.");
		System.out.println("Your final bank balance: "+bankAccount);
		return bankAccount;
	}

	private static int bankSetup() {
		int account = 1000;
		boolean confirm = false;
		while(!confirm){
			Scanner input = new Scanner(System.in);
			System.out.println("How much money would you like to bring to the table?");
			account = input.nextInt();

			Scanner input2 = new Scanner(System.in);
			System.out.println("Are you sure you would like to bring "+account+" dollars to the table?");
			String yes = input.next();
			if(yes.equals("yes") || yes.equals("Yes") || yes.equals("Y") || yes.equals("y") || yes.equals("true") || yes.equals("ok") || yes.equals("hit") || yes.equals("Hit")||yes.equals("OK") || yes.equals("Ok") || yes.equals("sure") ||yes.equals("True") ||yes.equals("yeah")||yes.equals("Yeah")){
				confirm = true;
			}
		}
		return account;
	}

	private static boolean confirmStick(int score) {
		boolean stick = false;
		Scanner input = new Scanner(System.in);
		System.out.println("Would you like to STICK with the current score of: "+score +"?");
		String yes = input.next();
		if(yes.equals("yes") || yes.equals("Yes") || yes.equals("Y") || yes.equals("y") || yes.equals("true") || yes.equals("ok") || yes.equals("hit") || yes.equals("Hit")||yes.equals("OK") || yes.equals("Ok") || yes.equals("sure") ||yes.equals("True") ||yes.equals("yeah")||yes.equals("Yeah")){
			stick = true;
		}
		return stick;
	}

	private static LinkedList<Integer> fixUserHand(LinkedList<Integer> userHand, HashMap<Integer, HashMap<Integer, String>> dealtCards, int count) {
		Set<Integer> a = dealtCards.get(count-1).keySet();
		Iterator<Integer> iter = a.iterator();
		int firstCardNumber = iter.next();
		Object firstCardDisplay = firstCardNumber;
		if(firstCardNumber == 11){
			firstCardDisplay = "Jack";
		}
		if(firstCardNumber == 12){
			firstCardDisplay = "Queen";
		}
		if(firstCardNumber == 13){
			firstCardDisplay = "King";
		}
		if(firstCardNumber == 1){
			firstCardDisplay = "Ace";
		}
		String firstCardSuit = dealtCards.get(count-1).get(firstCardNumber);
		System.out.println("You have been dealt a ("+firstCardDisplay +" of " +firstCardSuit+")");
		userHand.add(firstCardNumber);
		return userHand;
	}

	private static LinkedList<Integer> fixDealerHand(LinkedList<Integer> dealerHand, HashMap<Integer, HashMap<Integer, String>> dealtCards, int count) {
		Set<Integer> a = dealtCards.get(count-1).keySet();
		Iterator<Integer> iter = a.iterator();
		int firstCardNumber = iter.next();
		Object firstCardDisplay = firstCardNumber;
		if(firstCardNumber == 11){
			firstCardDisplay = "Jack";
		}
		if(firstCardNumber == 12){
			firstCardDisplay = "Queen";
		}
		if(firstCardNumber == 13){
			firstCardDisplay = "King";
		}
		if(firstCardNumber == 1){
			firstCardDisplay = "Ace";
		}
		String firstCardSuit = dealtCards.get(count-1).get(firstCardNumber);
		System.out.println("Dealer has been dealt a ("+firstCardDisplay +" of " +firstCardSuit+")");
		dealerHand.add(firstCardNumber);
		return dealerHand;
	}

	private static boolean bustManage(int score) {
		if (score >21){
			return true;
		}
		return false;
	}

	private static int getScore(LinkedList<Integer> userHand, int score) {	
		score = 0;
		int[] aces = new int[4];
		aces[0] = 0; aces[1] = 0; aces[2] = 0; aces[3] = 0;
		int acesCount =0;
		for (int i = 0; i < userHand.size(); i++){
			//System.out.println(userHand.get(i));
			if(userHand.get(i) == 11 || userHand.get(i) == 12 || userHand.get(i) == 13){
				score = score + 10;
			}
			else if (userHand.get(i) != 11 && userHand.get(i) != 12 && userHand.get(i) != 13 && userHand.get(i) != 1){
				score = score + userHand.get(i);
			}
			else if(userHand.get(i) == 1 && score <= 10){
				aces[acesCount] = 1;
				acesCount++;
				//score = score + 11;
			}
			else{
				aces[acesCount] = 1;
				acesCount++;
				//score = score + 1;
			}
		}
		for(int i = 0; i < aces.length-1; i++){
			if (aces[i] == 1 && aces[i+1]==0 && score<=10){
				score += 11;
			}
			else if (aces[i] == 1 && aces[i+1]==1 && score <= 9){
				score += 11;
			}
			else if (aces[i] == 1 && aces[i+1]==1 && score > 9){
				score += 1;
			}
			else if (aces[i] == 1 && aces[i+1]==0 && score > 10){
				score += 1;
			}
		}
		return score;
	}

	private static int getDealerScore(LinkedList<Integer> dealerHand) {	
		int score = 0;
		int[] aces = new int[4];
		aces[0] = 0; aces[1] = 0; aces[2] = 0; aces[3] = 0;
		int acesCount =0;
		for (int i = 0; i < dealerHand.size(); i++){
			//System.out.println(userHand.get(i));
			if(dealerHand.get(i) == 11 || dealerHand.get(i) == 12 || dealerHand.get(i) == 13){
				score = score + 10;
			}
			else if(dealerHand.get(i) != 11 && dealerHand.get(i) != 12 && dealerHand.get(i) != 13 && dealerHand.get(i) != 1){
				score = score + dealerHand.get(i);
			}
			else if(dealerHand.get(i) == 1 && score <= 10){
				aces[acesCount] = 1;
				acesCount++;
				//score = score + 11;
			}
			else{
				aces[acesCount] = 1;
				acesCount++;
				//score = score + 1;
			}
		}
		for(int i = 0; i < aces.length-1; i++){
			if (aces[i] == 1 && aces[i+1]==0 && score<=10){
				score += 11;
			}
			else if (aces[i] == 1 && aces[i+1]==1 && score <= 9){
				score += 11;
			}
			else if (aces[i] == 1 && aces[i+1]==1 && score > 9){
				score += 1;
			}
			else if (aces[i] == 1 && aces[i+1]==0 && score > 10){
				score += 1;
			}
		}
		return score;
	}

	private static int hit(HashMap<Integer,HashMap<Integer,String>> dealtCards, HashMap<Integer, HashMap<Integer,String>> deck) {
		int count = dealtCards.size()+1;
		Random rand = new Random();
		int randCard = rand.nextInt(deck.size()+1);
		while(randCard <1 || randCard >52 || cardTracker.contains(randCard)){
			rand = new Random();
			randCard = rand.nextInt(deck.size()+1);
		}
		dealtCards.put(count, deck.get(randCard));
		cardTracker.add(randCard);
		count++;
		return count;
	}

	private static boolean hitVerify() {
		boolean hit = false;
		Scanner input = new Scanner(System.in);
		System.out.println("Would you like to hit?");
		String yes = input.next();
		if(yes.equals("yes") || yes.equals("Yes") || yes.equals("Y") || yes.equals("y") || yes.equals("true") || yes.equals("ok") || yes.equals("hit") || yes.equals("Hit")||yes.equals("OK") || yes.equals("Ok") || yes.equals("sure") ||yes.equals("True") ||yes.equals("yeah")||yes.equals("Yeah")){
			hit = true;
		}
		return hit;
	}

	private static void dealerCardsReveal(HashMap<Integer,HashMap<Integer,String>> dealtCards){
		Set<Integer> dealer1 = dealtCards.get(1).keySet();
		Iterator<Integer> iterdeal1 = dealer1.iterator();
		int firstCardNumberDealer = iterdeal1.next();
		Object firstCardDisplayDealer = firstCardNumberDealer;
		if(firstCardNumberDealer == 11){
			firstCardDisplayDealer = "Jack";
		}
		if(firstCardNumberDealer == 12){
			firstCardDisplayDealer = "Queen";
		}
		if(firstCardNumberDealer == 13){
			firstCardDisplayDealer = "King";
		}
		if(firstCardNumberDealer == 1){
			firstCardDisplayDealer = "Ace";
		}
		String firstCardSuitDealer = dealtCards.get(1).get(firstCardNumberDealer);

		Set<Integer> dealer2 = dealtCards.get(2).keySet();
		Iterator<Integer> iterdeal2 = dealer2.iterator();
		int secondCardNumberDealer = iterdeal2.next();
		Object secondCardDisplayDealer = secondCardNumberDealer;
		if(secondCardNumberDealer == 11){
			secondCardDisplayDealer = "Jack";
		}
		if(secondCardNumberDealer == 12){
			secondCardDisplayDealer = "Queen";
		}
		if(secondCardNumberDealer == 13){
			secondCardDisplayDealer = "King";
		}
		if(secondCardNumberDealer == 1){
			secondCardDisplayDealer = "Ace";
		}
		String secondCardSuitDealer = dealtCards.get(2).get(secondCardNumberDealer);

		System.out.println("The dealer been dealt a ("+firstCardDisplayDealer +" of " +firstCardSuitDealer 
				+ ") and ("+secondCardDisplayDealer +" of " + secondCardSuitDealer+")");

	}

	private static LinkedList<Integer> informUser(HashMap<Integer,HashMap<Integer,String>> dealtCards, int whichUser, LinkedList<Integer> dealerHand) {
		LinkedList<Integer> userHand = new LinkedList<Integer>();

		Set<Integer> dealer1 = dealtCards.get(1).keySet();
		Iterator<Integer> iterdeal1 = dealer1.iterator();
		int firstCardNumberDealer = iterdeal1.next();
		Object firstCardDisplayDealer = firstCardNumberDealer;
		if(firstCardNumberDealer == 11){
			firstCardDisplayDealer = "Jack";
		}
		if(firstCardNumberDealer == 12){
			firstCardDisplayDealer = "Queen";
		}
		if(firstCardNumberDealer == 13){
			firstCardDisplayDealer = "King";
		}
		if(firstCardNumberDealer == 1){
			firstCardDisplayDealer = "Ace";
		}
		String firstCardSuitDealer = dealtCards.get(1).get(firstCardNumberDealer);

		Set<Integer> dealer2 = dealtCards.get(2).keySet();
		Iterator<Integer> iterdeal2 = dealer2.iterator();
		int secondCardNumberDealer = iterdeal2.next();
		Object secondCardDisplayDealer = secondCardNumberDealer;
		if(secondCardNumberDealer == 11){
			secondCardDisplayDealer = "Jack";
		}
		if(secondCardNumberDealer == 12){
			secondCardDisplayDealer = "Queen";
		}
		if(secondCardNumberDealer == 13){
			secondCardDisplayDealer = "King";
		}
		if(secondCardNumberDealer == 1){
			secondCardDisplayDealer = "Ace";
		}
		String secondCardSuitDealer = dealtCards.get(2).get(secondCardNumberDealer);

		dealerHand.add(firstCardNumberDealer);
		dealerHand.add(secondCardNumberDealer);

		System.out.println("The dealer been dealt a ("+firstCardDisplayDealer +" of " +firstCardSuitDealer 
				+ ") and (UNKNOWN)");


		Set<Integer> a = dealtCards.get(3).keySet();
		Iterator<Integer> iter = a.iterator();
		int firstCardNumber = iter.next();
		Object firstCardDisplay = firstCardNumber;
		if(firstCardNumber == 11){
			firstCardDisplay = "Jack";
		}
		if(firstCardNumber == 12){
			firstCardDisplay = "Queen";
		}
		if(firstCardNumber == 13){
			firstCardDisplay = "King";
		}
		if(firstCardNumber == 1){
			firstCardDisplay = "Ace";
		}
		String firstCardSuit = dealtCards.get(3).get(firstCardNumber);

		Set<Integer> b = dealtCards.get(4).keySet();
		Iterator<Integer> iter2 = b.iterator();
		int secondCardNumber = iter2.next();
		String secondCardSuit = dealtCards.get(4).get(secondCardNumber);
		Object secondCardDisplay = secondCardNumber;

		if(secondCardNumber == 11){
			secondCardDisplay = "Jack";
		}
		if(secondCardNumber == 12){
			secondCardDisplay = "Queen";
		}
		if(secondCardNumber == 13){
			secondCardDisplay = "King";
		}
		if(secondCardNumber == 1){
			secondCardDisplay = "Ace";
		}

		System.out.println("You have been dealt a ("+firstCardDisplay +" of " +firstCardSuit 
				+ ") and (" + secondCardDisplay +" of " +secondCardSuit +")");

		userHand.add(firstCardNumber);
		userHand.add(secondCardNumber);
		return userHand;
	}

	private static HashMap<Integer,HashMap<Integer,String>> dealCards(int players, HashMap<Integer, HashMap<Integer,String>> deck) {
		int randCard = 0;
		int cardsToDeal = players*2;
		int count = 1;
		HashMap<Integer,HashMap<Integer,String>> dealtCards = new HashMap<Integer,HashMap<Integer,String>>();
		//LinkedList cardTracker = new LinkedList(); //ensures no repeats
		while(cardsToDeal>0){
			Random rand = new Random();
			randCard = rand.nextInt(deck.size()+1);
			while(randCard <1 || randCard >52 || cardTracker.contains(randCard)){
				rand = new Random();
				randCard = rand.nextInt(deck.size()+1);
			}
			dealtCards.put(count, deck.get(randCard));
			cardTracker.add(randCard);
			cardsToDeal--;
			count++;
		}
		return dealtCards;
	}

	private static HashMap<Integer, HashMap<Integer,String>> setUpDeck() {
		HashMap<Integer, HashMap<Integer,String>> deck = new HashMap<Integer, HashMap<Integer,String>>();

		int count = 1; 

		for(int i = 1; i <= 13; i++){
			for(int j = 1; j <=4; j++){
				String suit = "null";
				if(j ==1){
					suit = "Spades";
				}
				if(j ==2){
					suit = "Hearts";
				}
				if(j ==3){
					suit = "Clovers";
				}
				if(j ==4){
					suit = "Diamonds";
				}
				HashMap<Integer,String> temp = new HashMap<Integer,String>();
				temp.put(i,suit);
				deck.put(count,temp);
				count++;
			}
		}
		return deck;
	}

	private static boolean validation(boolean sim, int players) {
		if(sim || (players<2) ){
			return false;
		}
		boolean wantsToPlay = false;
		Scanner input = new Scanner(System.in);
		System.out.println("Ready to go?");
		String yes = input.next();
		if(yes.equals("yes") || yes.equals("Yes") || yes.equals("Y") || yes.equals("y") || yes.equals("true") || yes.equals("ok") || yes.equals("OK") || yes.equals("Ok") || yes.equals("sure") ||yes.equals("True") ||yes.equals("yeah")||yes.equals("Yeah")){
			wantsToPlay = true;
		}
		return wantsToPlay;
	}
	private static boolean validation2(boolean sim, int players, int cost) {
		if(sim || (players<2) ){
			return false;
		}
		boolean wantsToPlay = false;
		Scanner input = new Scanner(System.in);
		System.out.println("Want to play another round? The cost is "+cost+" dollars.");
		String yes = input.next();
		if(yes.equals("yes") || yes.equals("Yes") || yes.equals("Y") || yes.equals("y") || yes.equals("true") || yes.equals("ok") || yes.equals("OK") || yes.equals("Ok") || yes.equals("sure") ||yes.equals("True") ||yes.equals("yeah")||yes.equals("Yeah")){
			wantsToPlay = true;
		}
		return wantsToPlay;
	}

}
