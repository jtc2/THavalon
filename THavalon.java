import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.HashMap;
import java.util.HashSet;

public class THavalon
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("How many people are playing?");			

		int numPeople = Integer.parseInt(br.readLine());

		ArrayList<String> players = new ArrayList<String>();
		for(int i = 0; i < numPeople; i++)
		{
			System.out.println("Who is player " + i + "?");

			players.add(br.readLine());
		}

		Random rand = new Random();
		ArrayList<String> firstMissionPlayers = new ArrayList<String>();
		
		firstMissionPlayers.add(players.get(rand.nextInt(players.size())));
		firstMissionPlayers.add(players.get(rand.nextInt(players.size())));
		
		while(firstMissionPlayers.get(0).toString().equals(firstMissionPlayers.get(1)))
		{
			System.out.println("Starting players were not distinct. Trying again.");
			firstMissionPlayers.set(1,players.get(rand.nextInt(players.size())));
		}
		
		String startingPlayer = players.get(rand.nextInt(players.size()));
		while (firstMissionPlayers.toString().matches("\\[.*\\b" + startingPlayer + "\\b.*]"))
		{
			startingPlayer = players.get(rand.nextInt(players.size()));
		}
		
		//Setting up the good and evil "decks"
		ArrayList<String> good = new ArrayList<String>();
		ArrayList<String> evil = new ArrayList<String>();

		good.add("Merlin");
		good.add("Percival");
		good.add("Tristan");
		good.add("Iseult");
		
		if (rand.nextBoolean()) 
		{
			good.add("Lancelot [good]");
		}
		else 
		{
			evil.add("Lancelot [evil]");
		}
		
		if(numPeople >= 7)
		{
			good.add("Guinevere");
			good.add("Arthur");
		}
		/*
		if(numPeople >= 9)
		{
			good.add("Arthur"); 
		}
		*/
		evil.add("Mordred");
		evil.add("Morgana");
		
		if(numPeople >= 7)
		{
			evil.add("Agravaine");
			evil.add("Colgrevance");
		}
		
		if (numPeople >= 7 && numPeople != 9)
		{
			evil.add("Oberon");
		}
		
		if(numPeople >= 10)
		{
			good.add("Gawain");
		}

		int numEvil = 0;
		int numGood = players.size();
		if(numPeople == 10 || numPeople == 11)
		{
			numEvil = 4;
			numGood = numPeople - numEvil;
	
		}
		else if(numPeople == 8 || numPeople == 7)
		{
			numEvil = 3;
			numGood = numPeople-numEvil;
		
		}
		else if(numPeople == 9)
		{	
			numEvil = 3; 
			numGood = 4;
		}
		else if(numPeople == 6 || numPeople == 5)
		{
			numEvil = 2;
			numGood = numPeople - numEvil;
		}	
		if(numEvil == 0)
		{
			System.err.println("Can't have that number of players");
			System.exit(1);
		}
		
		HashMap<String, String> assignments = new HashMap<String, String>();
		HashMap<String, String> reverseAssignments = new HashMap<String, String>();
		HashSet<String> roles = new HashSet<String>();
		for(int i = 0; i < numEvil; i++)
		{
			int toRemove = rand.nextInt(players.size());
			String evilPerson = players.remove(toRemove);
			toRemove = rand.nextInt(evil.size());
			String evilRole = evil.remove(toRemove);

			assignments.put(evilPerson, evilRole);
			reverseAssignments.put(evilRole, evilPerson);
			roles.add(evilRole);
		}

		for(int i = 0; i < numGood; i++)
		{
			int toRemove = rand.nextInt(players.size());
			String goodPerson = players.remove(toRemove);
			toRemove = rand.nextInt(good.size());
			String goodRole = good.remove(toRemove);

			assignments.put(goodPerson, goodRole);
			reverseAssignments.put(goodRole, goodPerson);
			roles.add(goodRole);
		}

		if (numPeople == 9)
		{
			int toRemoveA = rand.nextInt(players.size());
			String goodPersonA = players.remove(toRemoveA);
			toRemoveA = rand.nextInt(good.size());

			assignments.put(goodPersonA, "Pelinor");
			reverseAssignments.put("Pelinor", goodPersonA);
			roles.add("Pelinor");
			
			int toRemoveB = rand.nextInt(players.size());
			String goodPersonB = players.remove(toRemoveB);
			toRemoveB = rand.nextInt(good.size());

			assignments.put(goodPersonB, "Quesing Beast");
			reverseAssignments.put("Questing Beast", goodPersonB);
			roles.add("Questing Beast");
		}
		
		File file = new File("game");
		
		if (file.exists() && file.isDirectory())
		{
			recursiveDelete(file);
		}
				
		boolean success = file.mkdir();
		if(!success)
		{
			System.err.println("error making directory");
			System.exit(1);
		}

		PrintWriter writer;

		//Merlin sees: Morgana, Maelegant, Oberon, Agravaine, Colgrevance, Lancelot* as evil 
		if(roles.contains("Merlin"))
		{
			String fileName = "game/" + reverseAssignments.get("Merlin");
			file = new File(fileName);
			writer = new PrintWriter(fileName, "UTF-8");
		   	writer.println("You are Merlin.");
			HashSet<String> seen = new HashSet<String>();
			if(roles.contains("Morgana"))
				seen.add(reverseAssignments.get("Morgana"));
			if(roles.contains("Oberon"))
				seen.add(reverseAssignments.get("Oberon"));
			if(roles.contains("Agravaine"))
				seen.add(reverseAssignments.get("Agravaine"));
			if(roles.contains("Colgrevance"))
				seen.add(reverseAssignments.get("Colgrevance"));
			if(roles.contains("Lancelot [good]"))
				seen.add(reverseAssignments.get("Lancelot [good]"));
			if(roles.contains("Lancelot [evil]"))
				seen.add(reverseAssignments.get("Lancelot [evil]"));

			for(String name : seen)
			{
				writer.println("You see " + name + " as evil.");
			}
			writer.close();	
		}
		
		//Percival sees: Merlin, Morgana* as Merlin 
		if(roles.contains("Percival"))
		{
			String fileName = "game/" + reverseAssignments.get("Percival");
			file = new File(fileName);
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println("You are Percival.");
			HashSet<String> seen = new HashSet<String>();
			if(roles.contains("Morgana"))
				seen.add(reverseAssignments.get("Morgana"));
			if(roles.contains("Merlin"))
				seen.add(reverseAssignments.get("Merlin"));

			for(String name : seen)
				writer.println("You see " + name + " as Merlin (or is it...?).");
			writer.close();
		}
		if(roles.contains("Tristan"))
		{
			
			String fileName = "game/" + reverseAssignments.get("Tristan");
			file = new File(fileName);
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println("You are Tristan.");
			if(roles.contains("Iseult"))
				writer.println(reverseAssignments.get("Iseult") + " is your lover.");
			else
				writer.println("Nobody loves you. Not even your cat.");
			writer.close();
		}
		if(roles.contains("Iseult"))
		{

			String fileName = "game/" + reverseAssignments.get("Iseult");
			file = new File(fileName);
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println("You are Iseult.");
			if(roles.contains("Tristan"))
				writer.println(reverseAssignments.get("Tristan") + " is your lover.");
			else
				writer.println("Nobody loves you.");
			writer.close();
		}
		if(roles.contains("Guinevere"))
		{

			String fileName = "game/" + reverseAssignments.get("Guinevere");
			file = new File(fileName);
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println("You are Guinevere.");
			HashSet<String> seen = new HashSet<String>();

			if(roles.contains("Arthur"))
				seen.add(reverseAssignments.get("Arthur"));
			if(roles.contains("Lancelot [evil]"))
				seen.add(reverseAssignments.get("Lancelot [evil]"));

			for(String name : seen)
				writer.println("You see " + name + " as your either your luscious Lancelot ([evil]) or your lawfully wedded Arthur.");

			writer.close();
		}
		if(roles.contains("Lancelot [good]"))
		{

			String fileName = "game/" + reverseAssignments.get("Lancelot [good]");
			file = new File(fileName);
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println("You are Lancelot. You are on the Good team.");
			/*HashSet<String> seen = new HashSet<String>();
			
			if(roles.contains("Arthur"))
				seen.add(reverseAssignments.get("Arthur"));

			for(String name : seen)
				writer.println("You see " + name + " as your glorious leader, Arthur.");
			*/
			
			writer.println("");
			writer.println("Ability: You are able to play Reversal cards while on missions. A Reversal card inverts the result of a mission; a mission that would have succeeded now fails and vice versa."); 
			writer.println("");
			writer.println("Note: In games with at least 7 players, a Reversal played on the 4th mission results in a failed mission if there is only one Fail card, and otherwise succeeds. Reversal does not interfere with Agravaine's ability to cause the mission to fail.");
			
			writer.close();
		}
		
		if(roles.contains("Gawain"))
		{

			String fileName = "game/" + reverseAssignments.get("Gawain");
			file = new File(fileName);
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println("You are Gawain.");
			
			writer.println("");
			writer.println("Ability: Whenever a mission (other than the 1st) is sent, you may declare as Gawain to reveal a single person's played mission card. The mission card still affects the mission. (This ability functions identically to weak Inquisition and occurs after regular Inquisitions.) If the card you reveal is a Success, you are immediately 'Exiled' and may not go on missions for the remainder of the game, although you may still vote."); 
			writer.println("");
			writer.println("You may use this ability once per mission as long as you are neither on the mission team nor 'Exiled'. You may choose to not use your ability on any round, even if you would be able to use it.");
			
			writer.close();
		}
		
		
		if(roles.contains("Arthur"))
		{

			String fileName = "game/" + reverseAssignments.get("Arthur");
			file = new File(fileName);
			writer = new PrintWriter(fileName, "UTF-8");
			ArrayList<String> goodChars = new ArrayList<String>();
			
			if(roles.contains("Percival"))
				goodChars.add("Percival");
			if(roles.contains("Merlin"))
				goodChars.add("Merlin");
			if(roles.contains("Lancelot [good]"))
				goodChars.add("Lancelot");
			if(roles.contains("Guinevere"))
				goodChars.add("Guinevere");
			if(roles.contains("Tristan"))
				goodChars.add("Tristan");
			if(roles.contains("Iseult"))
				goodChars.add("Iseult");
			if(roles.contains("Gawain"))
				goodChars.add("Gawain");
			
			writer.println("You are Arthur.");
			writer.println("Ability: If three of the first four missions fail, you may reveal that you are Arthur. You may, after consulting the other players, attempt to identify all evil players in the game. If you are correct, then the assassination round occurs as if three missions had succeeded; should the evil team fail to assassinate a viable target, the good team wins.");
			writer.println("");
			writer.println("You know that the following other good roles are in the game:");
			for (String role : goodChars)
			{
				writer.println(role);
			}
			
			writer.close();
		}
		//evil

		if(roles.contains("Mordred"))
		{

			String fileName = "game/" + reverseAssignments.get("Mordred");
			file = new File(fileName);
			writer = new PrintWriter(fileName, "UTF-8");

			writer.println("You are Mordred. (Join us, we have jackets and meet on Thursdays. ~ Andrew and Kath)");
			HashSet<String> seen = new HashSet<String>();

			if(roles.contains("Morgana"))
				seen.add(reverseAssignments.get("Morgana"));
			if(roles.contains("Agravaine"))
				seen.add(reverseAssignments.get("Agravaine"));
			if(roles.contains("Colgrevance"))
				seen.add(reverseAssignments.get("Colgrevance"));
			if(roles.contains("Lancelot [evil]"))
				seen.add(reverseAssignments.get("Lancelot [evil]"));

			for(String name : seen)
			{
				writer.println(name + " is a fellow member of the evil council.");
			}

			writer.close();
		}
		if(roles.contains("Morgana"))
		{

			String fileName = "game/" + reverseAssignments.get("Morgana");
			file = new File(fileName);
			writer = new PrintWriter(fileName, "UTF-8");

			writer.println("You are Morgana.");
			HashSet<String> seen = new HashSet<String>();

			if(roles.contains("Mordred"))
				seen.add(reverseAssignments.get("Mordred"));
			if(roles.contains("Agravaine"))
				seen.add(reverseAssignments.get("Agravaine"));
			if(roles.contains("Colgrevance"))
				seen.add(reverseAssignments.get("Colgrevance"));
			if(roles.contains("Lancelot [evil]"))
				seen.add(reverseAssignments.get("Lancelot [evil]"));

			for(String name : seen)
			{
				writer.println(name + " is a fellow member of the evil council.");
			}

			writer.close();
		}
		if(roles.contains("Oberon"))
		{

			String fileName = "game/" + reverseAssignments.get("Oberon");
			file = new File(fileName);
			writer = new PrintWriter(fileName, "UTF-8");

			writer.println("You are Oberon.");
			HashSet<String> seen = new HashSet<String>();
			HashSet<String> targets = new HashSet<String>();
			
			if(roles.contains("Mordred"))
				seen.add(reverseAssignments.get("Mordred"));
			if(roles.contains("Morgana"))
				seen.add(reverseAssignments.get("Morgana"));
			if(roles.contains("Agravaine"))
				seen.add(reverseAssignments.get("Agravaine"));
			if(roles.contains("Colgrevance"))
				seen.add(reverseAssignments.get("Colgrevance"));
			if(roles.contains("Lancelot [evil]"))
				seen.add(reverseAssignments.get("Lancelot [evil]"));
			
			for(String name : seen)
			{
				writer.println(name + " is a member of the evil council.");
			}
			
			writer.println("");
			
			writer.println("Ability: Should any mission get to the last proposal of the round, after the people on the mission have been named, you may declare as Oberon to replace one person on that mission with yourself.");
			writer.println("");
			writer.println("Note: You may not use this ability after two missions have already failed. Furthermore, you may only use this ability once per game.");
			writer.close();
		}

		if(roles.contains("Agravaine"))
		{

			String fileName = "game/" + reverseAssignments.get("Agravaine");
			file = new File(fileName);
			writer = new PrintWriter(fileName, "UTF-8");

			writer.println("You are Agravaine.");
			
			HashSet<String> seen = new HashSet<String>();
					
			if(roles.contains("Morgana"))
				seen.add(reverseAssignments.get("Morgana"));
			if(roles.contains("Mordred"))
				seen.add(reverseAssignments.get("Mordred"));
			if(roles.contains("Colgrevance"))
				seen.add(reverseAssignments.get("Colgrevance"));
			if(roles.contains("Lancelot [evil]"))
				seen.add(reverseAssignments.get("Lancelot [evil]"));
;
			
			for(String name : seen)
			{
				writer.println(name + " is a fellow member of the evil council.");
			}
			writer.println("");
			writer.println("Ability: On any mission you are on, after the mission cards have been revealed, should the mission not result in a Fail (such as via a Reversal, requiring 2 fails, or other mechanics), you may formally declare as Agravaine to force the mission to Fail anyway.");
			writer.println("Drawback: You may only play Fail cards while on missions.");
			

			writer.close();
		}
		if(roles.contains("Lancelot [evil]"))
		{

			String fileName = "game/" + reverseAssignments.get("Lancelot [evil]");
			file = new File(fileName);
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println("You are Lancelot. You are on the Evil team.");
			
			HashSet<String> seen = new HashSet<String>();

			if(roles.contains("Mordred"))
				seen.add(reverseAssignments.get("Mordred"));
			if(roles.contains("Morgana"))
				seen.add(reverseAssignments.get("Morgana"));
			if(roles.contains("Agravaine"))
				seen.add(reverseAssignments.get("Agravaine"));
			if(roles.contains("Colgrevance"))
				seen.add(reverseAssignments.get("Colgrevance"));

			for(String name : seen)
			{
				writer.println(name + " is a fellow member of the evil council.");
			}
			
			writer.println("");
			writer.println("Ability: You are able to play Reversal cards while on missions. A Reversal card inverts the result of a mission; a mission that would have succeeded now fails and vice versa."); 
			writer.println("");
			writer.println("Note: In games with at least 7 players, a Reversal played on the 4th mission results in a failed mission if there is only one Fail card, and otherwise succeeds. Reversal does not interfere with Agravaine's ability to cause the 4th mission to fail.");
			
			writer.close();	
		}
		
		if(roles.contains("Colgrevance"))
		{

			String fileName = "game/" + reverseAssignments.get("Colgrevance");
			file = new File(fileName);
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println("You are Colgrevance.");
			if(roles.contains("Morgana"))
				writer.println(reverseAssignments.get("Morgana") + " is Morgana.");
			if(roles.contains("Mordred"))
				writer.println(reverseAssignments.get("Mordred") + " is Mordred.");
			if(roles.contains("Lancelot [evil]"))
				writer.println(reverseAssignments.get("Lancelot [evil]") + " is Lancelot.");	
			if(roles.contains("Agravaine"))
				writer.println(reverseAssignments.get("Agravaine") + " is Agravaine.");	
			if(roles.contains("Oberon"))
				writer.println(reverseAssignments.get("Oberon") + " is Oberon.");	
			writer.close();
		}

		if(roles.contains("Pelinor"))
		{
			
			String fileName = "game/" + reverseAssignments.get("Pelinor");
			file = new File(fileName);
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println("You are Pelinor.");
			writer.println("You must fulfill two of the following conditions to win:");
			writer.println("[1]: If Good wins via three mission success.");
			writer.println("[2]: If you go on the last mission with the Questing Beast.");
			writer.println("[3]: If, after the Assassination Round, you can guess the Questing Beast.");
			writer.close();
		}
		if(roles.contains("Questing Beast"))
		{

			String fileName = "game/" + reverseAssignments.get("Questing Beast");
			file = new File(fileName);
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println("You are Questing Beast.");
			writer.println("You must play the 'Questing Beast was here' card on missions.");
			writer.println("You must fulfill exactly one (not both) of the following conditions to win:");
			writer.println("[1]: If Evil wins via three missions failing.");
			writer.println("[2]: You never go on a mission with Pelinor.");
			
			if(roles.contains("Pelinor"))
				writer.println(reverseAssignments.get("Pelinor") + " is Pelinor.");	
			
			writer.close();
		}
		
		
		String fileName = "game/start";
		file = new File(fileName);
		writer = new PrintWriter(fileName, "UTF-8");
		writer.println("The players proposing teams for the first mission are:");
		
		for (String player : firstMissionPlayers)
		{
			writer.println(player);
		}
		writer.println(startingPlayer + " is the starting player of the 2nd round.");
		
		writer.close();
		
		String fileNameAll = "game/DoNotOpen";
		file = new File(fileNameAll);
		writer = new PrintWriter(fileNameAll, "UTF-8");
		writer.println("Player -> Role");
		
		for (String role : roles)
		{
			writer.println(reverseAssignments.get(role) + " -> " + role);
		}
		
		writer.close();
	}
	
	public static void recursiveDelete(File file) 
	{
		if (!file.exists())
		{
			return; 
		}
		
		if (file.isDirectory())
		{
			for (File f : file.listFiles())
			{
				recursiveDelete(f);
			}
		}
		
		file.delete();
	}
}
