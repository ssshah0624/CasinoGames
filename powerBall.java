import java.util.Iterator;
import java.util.Scanner;
import java.util.Random;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Set;

public class powerBall {
	static boolean mixedStats = true;
	static boolean psychic = false;
	static boolean seriousHouse = true;

	public static int main(int bet){
		int bankAccount = bet;
		int min = 0;
		int max = 3;
		if (seriousHouse && bet>999){
			int betDividedByTen = bet/10;
			max = bet/betDividedByTen;
		}
		int jackpot = bet*(10*(max-min));
		LinkedList<Integer> tracker = new LinkedList<Integer>();
		LinkedList<Integer> userTracker = new LinkedList<Integer>();

		int[] powerball = new int[3];

		if(psychic){
			powerball[0] = selectRandomNumber(tracker, min, max);
			powerball[1] = selectRandomNumber(tracker, min, max);
			powerball[2] = selectRandomNumber(tracker, min, max);
			System.out.println("1 -> "+powerball[0]);
			System.out.println("2 -> "+powerball[1]);
			System.out.println("3 -> "+powerball[2]);
		}

		int[] choice = welcome(jackpot, min, max, tracker, userTracker);
		System.out.println("\n* * * * * * * * * * * * * * * * * * *");
		System.out.println("* * * * * * * * POWERBALL* * * * * * * * *");
		System.out.println("* * * * * * * * * * * * * * * * * * *\n");
		System.out.println("Your choices are confirmed as ");
		System.out.println("Your Position 1: "+choice[0]);
		System.out.println("Your Position 2: "+choice[1]);
		System.out.println("Your Position 3: "+choice[2]);
		System.out.println();

		if(!psychic){
			powerball[0] = selectRandomNumber(tracker, min, max);
			powerball[1] = selectRandomNumber(tracker,min, max);
			powerball[2] = selectRandomNumber(tracker,min, max);
		}

		System.out.println("\n* * * * * * * * * * * * * * * * * * *\n");
		System.out.println("The selected numbers are....");
		System.out.println("PowerBall Position 1: "+powerball[0]);
		System.out.println("PowerBall Position 2: "+powerball[1]);
		System.out.println("PowerBall Position 3: "+powerball[2]);
		System.out.println();
		int winner = 0;
		if(choice[0] == powerball[0]){
			winner++;
		}
		if(choice[1] == powerball[1]){
			winner++;
		}
		if(choice[2] == powerball[2]){
			winner++;
		}
		if(winner == 0){
			System.out.println("Sorry you got none right!");
			bankAccount = 0;
		}
		if(winner == 1){
			System.out.println("You got one right! Unfortunately this doesn't entail a prize.");
			bankAccount = 0;
		}
		if(winner == 2){
			System.out.println("You got two right! You win the smaller pot. Congratulations!");
			bankAccount = bet * (max-min);
		}
		if(winner == 3){
			System.out.println("POWERBALL WINNER!!! Congratulations!");
			bankAccount = jackpot;
		}
		System.out.println("* * * * * * * * * * * * * * * * * * *");
		System.out.println("Thank you for playing PowerBall. See you around!");
		return bankAccount;
	}

	private static int[] welcome(int jackpot, int min, int max, LinkedList<Integer> tracker, LinkedList<Integer> userTracker) {
		int[] selection = new int[3];
		System.out.println("Welcome to PowerBall!\n");
		System.out.println("This game is not for the faint-hearted.\n" +
				"You will select 3 numbers between "+min+" and "+max+" in a unique order.\n" +
				"3 numbers will then be drawn by the power ball.\n" +
				"If your numbers are selected (in order), you are the POWERBALL WINNER!\n" +
				"Today's jackpot: "+jackpot +"\n");
		selection[0] = selectUserNumber(userTracker, min, max);
		selection[1] = selectUserNumber(userTracker, min, max);
		selection[2] = selectUserNumber(userTracker, min, max);
		return selection;
	}

	private static int selectUserNumber(LinkedList<Integer> userTracker, int min, int max) {
		Scanner input = new Scanner(System.in);
		int yes = -1;
		while(yes < min || yes >max || userTracker.contains(yes)){
			System.out.println("Please pick a number...");
			yes = input.nextInt();
		}
		userTracker.add(yes);
		return yes;
	}

	private static int selectRandomNumber(LinkedList<Integer> tracker, int min, int max) {
		int number = -1;
		Random mixerRand = new Random();
		Random rand = new Random();
		int mixer = 0;
		while(mixer == 0){
			mixer = mixerRand.nextInt(4000);
		}
		if(mixedStats){
			System.out.println("Shuffled: "+mixer+" times");
		}
		while(mixer>0 || number<min || number>max || tracker.contains(number)){
			number = rand.nextInt(max+1);
			mixer--;
		}

		tracker.add(number);
		return number;
	}

}
