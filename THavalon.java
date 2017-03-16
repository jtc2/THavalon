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
		good.add("Guinevere");
		
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
		}
		
		if (numPeople >= 7 && numPeople != 9)
		{
			evil.add("Oberon");
		}
		
		if(numPeople >= 10)
		{
			evil.add("Colgrevance");
		}

		int numEvil = 0;
		if(numPeople == 10 || numPeople == 11)
			numEvil = 4;
		else if(numPeople == 8 || numPeople == 9 || numPeople == 7)
			numEvil = 3;
		else if(numPeople == 6 || numPeople == 5)
			numEvil = 2;

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

		int numGood = players.size();
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

			if(roles.contains("Lancelot [good]"))
				seen.add(reverseAssignments.get("Lancelot [good]"));
			if(roles.contains("Lancelot [evil]"))
				seen.add(reverseAssignments.get("Lancelot [evil]"));

			for(String name : seen)
				writer.println("You see " + name + " as your dear beloved Lancelot.");

			writer.close();
		}
		if(roles.contains("Lancelot [good]"))
		{

			String fileName = "game/" + reverseAssignments.get("Lancelot [good]");
			file = new File(fileName);
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println("You are Lancelot. You are on the Good team.");
			HashSet<String> seen = new HashSet<String>();
			
			if(roles.contains("Arthur"))
				seen.add(reverseAssignments.get("Arthur"));

			for(String name : seen)
				writer.println("You see " + name + " as your glorious leader, Arthur.");

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
			
			if(roles.contains("Tristan") && roles.contains("Iseult"))
				targets.add("The Lovers are");
			if(roles.contains("Merlin"))
				targets.add("Merlin is");
			
			for(String name : seen)
			{
				writer.println(name + " is a member of the evil council.");
			}
			
			writer.println("");
			
			for(String name : targets)
			{
				writer.println(name + " a valid assassination target.");
			}
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
			writer.println("Ability: After the cards from the 4th mission have been revealed, you may reveal that you are Agravaine and cause the mission to fail, even if it would have succeeded. You may only do this if you were on the 4th mission team.");
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
			if(roles.contains("Mordred"))
				writer.println(reverseAssignments.get("Mordred") + " is your glorious leader, Mordred.");
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
