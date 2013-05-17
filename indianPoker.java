import java.util.Iterator;
import java.util.Scanner;
import java.util.Random;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Set;

public class indianPoker {
	//Control Panel
	static int players = 2;
	static LinkedList cardTracker = new LinkedList();
	static int cautiousDealerBet = 10;
	static boolean revealCard = false;
	static boolean testAce = true;
	//End Control Panel

	public static int main(int bankAccount){
		boolean valid = true;
		while (valid){
			HashMap<Integer, HashMap<Integer,String>> deck = setUpDeck();
			HashMap<Integer,HashMap<Integer,String>> dealtCards = dealCards(players,deck);

			//Dealers Card
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
				if(testAce){
					firstCardNumberDealer = 14;
				}
			}
			String firstCardSuitDealer = dealtCards.get(1).get(firstCardNumberDealer);
			System.out.println("The dealer been dealt a ("+firstCardDisplayDealer +" of " +firstCardSuitDealer + ")");

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
				if(testAce){
					secondCardNumberDealer = 14;
				}
			}
			String secondCardSuitDealer = dealtCards.get(2).get(secondCardNumberDealer);
			if(secondCardNumberDealer==14){
				secondCardNumberDealer = 1;
				secondCardSuitDealer = dealtCards.get(2).get(secondCardNumberDealer);
				secondCardNumberDealer =14;
				
			}

			boolean satisfied = false;
			int tableBet = 0;
			int userBetRecord = 0;
			int dealerBetRecord=0;
			boolean forfeit = false;
			boolean call = false;
			boolean userCall = false;
			while (!satisfied){
				System.out.println("Your bank balance: "+bankAccount+" dollars.");
				int bet = -1;
				do{
					bet = getBet(bankAccount);
				}while(bet<0);
				System.out.println("You have bet: "+bet+" dollars.");
				tableBet+= bet;
				userBetRecord+= bet;
				bankAccount -= bet;

				if (userBetRecord == dealerBetRecord){
					userCall = true;
				}

				if(revealCard){
					System.out.println("My Card: "+secondCardNumberDealer);
				}
				int dealerBet = 0;
				boolean allin = false;
				if(!userCall){

					dealerBet = getDealerBet(bet, secondCardNumberDealer);

					allin = false;
					if(dealerBet-bet > bankAccount){
						dealerBet = bankAccount+bet;
						allin = true;
					}
					if(dealerBet < bet){
						dealerBet = bet;
					}

				}
				if(userCall){
					dealerBet = 0;
				}
				tableBet += dealerBet;
				dealerBetRecord += dealerBet;
				System.out.println("Dealer bets: "+dealerBet+" dollars.");


				if(dealerBetRecord-userBetRecord > 0){
					System.out.println("The dealer has raised by "+(dealerBetRecord-userBetRecord)+" dollars.");
					if (allin){
						System.out.println("You must go all-in to continue playing.");
					}
				}


				if(dealerBetRecord-userBetRecord == 0 && !userCall){
					System.out.println("The dealer has called your bet of "+bet+" dollars. There are currently "+tableBet+" dollars on the table.");
					call = true;
				}
				if(userCall){
					System.out.println("You have called the dealers bet. There are currently "+tableBet+" dollars on the table.");
					call = true;
				}

				if(!call){
					satisfied = more();
					if(satisfied){
						forfeit = true;
					}
				}
				if(call){
					satisfied = true;
				}

			}
			if(!forfeit){
				System.out.println("\n******************************************\n");
				System.out.println("Dealers Card: ("+firstCardDisplayDealer +" of " +firstCardSuitDealer + ")");
				System.out.println("Your Card: ("+secondCardDisplayDealer +" of " +secondCardSuitDealer + ")");

				if(firstCardNumberDealer > secondCardNumberDealer && secondCardNumberDealer != 14){
					System.out.println("Dealer wins!");
					System.out.println("Your bank balance: "+bankAccount+" dollars.");
				}
				if(firstCardNumberDealer == secondCardNumberDealer && secondCardNumberDealer != 14){
					System.out.println("Tie!");
					bankAccount += userBetRecord;
					System.out.println("Your bank balance: "+bankAccount+" dollars.");
				}
				if(firstCardNumberDealer < secondCardNumberDealer || secondCardNumberDealer == 14){
					System.out.println("You win!");
					if(secondCardNumberDealer == 14){
						System.out.println("Aces go to user.");
					}
					bankAccount +=tableBet;
					System.out.println("Your bank balance: "+bankAccount+" dollars.");
				}
				System.out.println("\n******************************************\n");
			}
			if(forfeit){
				System.out.println("You forfeit. Dealer wins!");
				System.out.println("Your bank balance: "+bankAccount+" dollars.");
			}
			valid = validation();
		}

		return bankAccount;
	}

	private static int getDealerBet(int userBet, int userCard) {

		Random rand = new Random();
		int ternary = -1; //really 0, 1, 2, AND 3
		//System.out.println("ternary pre: "+ternary);
		int mixer = 50;
		while(mixer > 0 || ternary > 3){
			ternary = rand.nextInt(4); // T0 -> call; T1 -> cautiously bet; T2 -> big money logic; T3 -> random risk logic
			mixer--;
		}
		//System.out.println("ternary post: "+ternary);

		if(ternary == 0){
			return userBet;
		}
		else if(ternary==1 || userBet==0){
			return cautiousDealerBet;
		}
		else if (userCard == 1){
			return userBet;
		}
		else if(ternary==1 && userCard <7){
			return userBet * 2;
		}
		else if(ternary==1 && userCard >=7){
			return userBet;
		}
		else if(ternary==2 && userCard <5){
			Random risky = new Random();
			int temp = 0;
			while(temp == 0){
				temp = risky.nextInt(4);
			}
			//System.out.println("multiplier used: "+temp);
			return userBet * temp;
		}
		else if(ternary==2 && userCard >5 && userCard<10){
			Random risky = new Random();
			int temp = 0;
			while(temp == 0){
				temp = risky.nextInt(3);
			}
			//System.out.println("multiplier used: "+temp);
			return userBet * temp;
		}
		else if(ternary==2  && userCard>10){
			Random risky = new Random();
			int temp = 0;
			while(temp == 0){
				temp = risky.nextInt(2);
			}
			//System.out.println("multiplier used: "+temp);
			return userBet * temp;
		}
		else if(ternary==3){
			Random superRisky = new Random();
			int temp = 0;
			while(temp == 0){
				temp = superRisky.nextInt(4);
			}
			//System.out.println("multiplier used: "+temp);
			return userBet * temp;
		}
		return userBet;
	}

	private static boolean more() {
		boolean settle = true;
		Scanner input2 = new Scanner(System.in);
		System.out.println("Do you want to bet more?");
		String yes = input2.next();
		if(yes.equals("yes") || yes.equals("Yes") || yes.equals("Y") || yes.equals("y") || yes.equals("true") || yes.equals("ok") || yes.equals("OK") || yes.equals("Ok") || yes.equals("sure") ||yes.equals("True") ||yes.equals("yeah")|| yes.equals("es")||yes.equals("Yeah")){
			settle = false;
		}
		return settle;
	}

	private static int getBet(int bankAccount) {
		boolean doubleCheck = false;
		int bet = 0;
		boolean firstTime = true;
		while(!doubleCheck || bet>bankAccount){
			if(bet>bankAccount){
				System.out.println("Error: You don't have sufficient funds to honor your bet.");
			}
			Scanner input = new Scanner(System.in);
			if(firstTime){
				System.out.println("How much would you like to bet that your card is greater than your opponents?");
			}
			if(!firstTime){
				System.out.println("No problem! Let's try again. ");
				System.out.println("How much would you like to bet that your card is greater than your opponents?");
			}
			bet = input.nextInt();

			Scanner input2 = new Scanner(System.in);
			System.out.println("Are you sure you want to bet "+bet +"?");
			String yes = input2.next();
			if(yes.equals("yes") || yes.equals("Yes") || yes.equals("Y") || yes.equals("y") || yes.equals("true") || yes.equals("ok") || yes.equals("OK") || yes.equals("Ok") || yes.equals("sure") ||yes.equals("True") ||yes.equals("yeah")|| yes.equals("es")||yes.equals("Yeah")){
				doubleCheck = true;
			}
		}
		return bet;
	}

	private static boolean validation() {
		boolean wantsToPlay = false;
		Scanner input = new Scanner(System.in);
		System.out.println("Want to go for another round?");
		String yes = input.next();
		if(yes.equals("yes") || yes.equals("Yes") || yes.equals("Y") || yes.equals("y") || yes.equals("true") || yes.equals("ok") || yes.equals("OK") || yes.equals("Ok") || yes.equals("sure") ||yes.equals("True") ||yes.equals("yeah")||yes.equals("Yeah")){
			wantsToPlay = true;
		}
		return wantsToPlay;
	}

	private static HashMap<Integer,HashMap<Integer,String>> dealCards(int players, HashMap<Integer, HashMap<Integer,String>> deck) {
		int randCard = 0;
		int cardsToDeal = players;
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



}
