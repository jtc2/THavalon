#!/usr/bin/env python3

import os
import random
import shutil

def main():
	# get the number of players
	num_players = int(input("How many people are playing?\n"))
	if num_players < 5 or num_players > 10:
		print("Invalid number of players")
		exit(1)
	
	# get the names of players
	players = set() # use as set to avoid duplicate players
	for player_num in range(1, num_players+1):
		players.add(input("Who is player " + str(player_num) + "?\n"))
	players = list(players) # convert to list
	random.shuffle(players) # ensure random order, though set should already do that
	if len(players) != num_players:
		print("No duplicate player names")
		exit(1)

	# choose 3 players
	three_players = random.sample(players, 3)

	# first two proppose for the first mission, last is starting player of second round
	first_mission_proposers = three_players[:2]
	second_mission_starter = three_players[2]

	# assign the roles in the game
	good_roles = ["Merlin", "Percival", "Tristan", "Iseult"]
	evil_roles = ["Mordred", "Morgana"]

	good_roles.append("Lancelot")
	evil_roles.append("Maelegant")


	if num_players >= 7:
		good_roles.append("Guinevere")
		good_roles.append("Arthur")
		evil_roles.append("Agravaine")
		evil_roles.append("Colgrevance")
		if num_players != 9:
			evil_roles.append("Oberon")

	if num_players == 10:
		good_roles.append("Gawain")

	# shuffle the roles
	random.shuffle(good_roles)
	random.shuffle(evil_roles)

	# determine the number of roles in the game
	if num_players == 10:
		num_evil = 4
		num_good = 6
	elif num_players == 9:
		num_evil  = 3
		num_good = 4
	elif num_players == 7 or num_players == 8:
		num_evil = 3
		num_good = num_players - num_evil
	else: # 5 or 6
		num_evil = 2
		num_good = num_players - num_evil

	# assign players to teams
	assignments = {}
	reverse_assignments = {}
	good_roles_in_game = set()
	evil_roles_in_game = set()

	if num_players == 9:
		pelinor = players[8]
		assignments[pelinor] = "Pelinor"
		reverse_assignments["Pelinor"] = pelinor

		questing_beast = players[7]
		assignments[questing_beast] = "Questing Beast"
		reverse_assignments["Questing Beast"] = questing_beast

	good_players = players[:num_good]
	evil_players = players[num_good:num_good + num_evil]


	# assign good roles
	for good_player in good_players:
		player_role = good_roles.pop()
		assignments[good_player] = player_role
		reverse_assignments[player_role] = good_player
		good_roles_in_game.add(player_role)

	# assign evil roles
	for evil_player in evil_players:
		player_role = evil_roles.pop()
		assignments[evil_player] = player_role
		reverse_assignments[player_role] = evil_player
		evil_roles_in_game.add(player_role)

	# delete and recreate game directory
	if os.path.isdir("game"):
		shutil.rmtree("game")
	os.mkdir("game")

	# make every role's file

	# Merlin sees: Morgana, Maelegant, Oberon, Agravaine, Colgrevance, Lancelot* as evil 
	if "Merlin" in good_roles_in_game:
		# determine who Merlin sees
		seen = []
		for evil_player in evil_players:
			if assignments[evil_player] != "Mordred":
				seen.append(evil_player)
		if "Lancelot" in good_roles_in_game:
			seen.append(reverse_assignments["Lancelot"])
		random.shuffle(seen)

		# and write this info to Merlin's file
		player_name = reverse_assignments["Merlin"]
		filename = "game/" + player_name
		with open(filename, "w") as file:
			file.write("You are Merlin.\n")
			for seen_player in seen:
				file.write("You see " + seen_player + " as evil.\n")

	# Percil sees Merlin, Morgana* as Merlin
	if "Percival" in good_roles_in_game:
		# determine who Percival sees
		seen = []
		if "Merlin" in good_roles_in_game:
			seen.append(reverse_assignments["Merlin"])
		if "Morgana" in evil_roles_in_game:
			seen.append(reverse_assignments["Morgana"])
		random.shuffle(seen)

		# and write this info to Percival's file
		player_name = reverse_assignments["Percival"]
		filename = "game/" + player_name
		with open(filename, "w") as file:
			file.write("You are Percival.\n")
			for seen_player in seen:
				file.write("You see " + seen_player + " as Merlin (or is it...?).\n")

	if "Tristan" in good_roles_in_game:
		# write the info to Tristan's file
		player_name = reverse_assignments["Tristan"]
		filename = "game/" + player_name
		with open(filename, "w") as file:
			file.write("You are Tristan.\n")
			# write Iseult's info to file
			if "Iseult" in good_roles_in_game:
				iseult_player = reverse_assignments["Iseult"]
				file.write(iseult_player + " is your lover.\n")
			else: 
				file.write("Nobody loves you. Not even your cat.\n")

	if "Iseult" in good_roles_in_game:
		# write this info to Iseult's file
		player_name = reverse_assignments["Iseult"]
		filename = "game/" + player_name
		with open(filename, "w") as file:
			file.write("You are Iseult.\n")
			# write Tristan's info to file
			if "Tristan" in good_roles_in_game:
				tristan_player = reverse_assignments["Tristan"]
				file.write(tristan_player + " is your lover.\n")
			else: 
				file.write("Nobody loves you.\n")

	if "Lancelot" in good_roles_in_game: 
		# write ability to Lancelot's file 
		player_name = reverse_assignments["Lancelot"] 
		filename = "game/" + player_name 
		with open(filename, "w") as file:
			file.write("You are Lancelot. You are on the Good team. \n\n") 
			file.write("Ability: Reversal \n")	
			file.write("You are able to play Reversal cards while on missions. A Reversal card inverts the result of a mission; a mission that would have succeeded now fails and vice versa. \n \n")
			file.write("Note: In games with at least 7 players, a Reversal played on the 4th mission results in a failed mission if there is only one Fail card, and otherwise succeeds. Reversal does not interfere with Agravaine's ability to cause the mission to fail.")

	if "Guinevere" in good_roles_in_game:
		# determine who Guinevere sees
		seen = []
		if "Arthur" in good_roles_in_game:
			seen.append(reverse_assignments["Arthur"])
		if "Maelegant" in evil_roles_in_game:
			seen.append(reverse_assignments["Maelegant"])
		if "Lancelot" in good_roles_in_game:
			seen.append(reverse_assignments["Lancelot"])
		random.shuffle(seen)

		# and write this info to Guinevere's file
		player_name = reverse_assignments["Guinevere"]
		filename = "game/" + player_name
		with open(filename, "w") as file:
			file.write("You are Guinevere.\n")
			for seen_player in seen:
				file.write("You see " + seen_player + " as either your luscious Lancelot, your lawfully wedded Arthur, or your kidnapper Maelegant.\n")

	if "Arthur" in good_roles_in_game:
		# determine which roles Arthur sees
		seen = []
		for good_role in good_roles_in_game:
			seen.append(good_role)
		random.shuffle(seen)

		# and write this info to Arthur's file
		player_name = reverse_assignments["Arthur"]
		filename = "game/" + player_name
		with open(filename, "w") as file:
			file.write("You are Arthur.\n\n")
			file.write("The following good roles are in the game:\n")
			for seen_role in seen:
				if seen_role != "Arthur":
					file.write(seen_role + "\n")
			file.write("\n")
			file.write("Ability: Proclamation\n")
			file.write("If two missions have failed, you may formally reveal that you are Arthur, establishing that you are Good for the remainder of the game. You may still propose and vote on missions, as well as be chosen to be part of a mission team, as per usual.\n")

	if "Gawain" in good_roles_in_game: 
		# determine what Gawain sees 
		seen = []
		player_name = reverse_assignments["Gawain"]
		good_players_no_gawain = set(good_players) - set([player_name])
		# guaranteed see a good player
		seen_good = random.sample(good_players_no_gawain, 1)
		seen.append(seen_good[0])

		# choose two other players randomly
		remaining_players = set(players) - set([player_name]) - set(seen_good)
		seen += random.sample(remaining_players, 2)

		random.shuffle(seen)
		
		# write info to Gawain's file 
		filename = "game/" + player_name
		with open(filename, "w") as file:
			file.write("You are Gawain.\n\n")
			file.write("The following players are not all evil:\n")
			for seen_player in seen:
				file.write(seen_player + "\n")
			file.write("\nAbility: Whenever a mission (other than the 1st) is sent, you may declare as Gawain to reveal a single person's played mission card. The mission card still affects the mission. (This ability functions identically to weak Inquisition and occurs after regular Inquisitions.) If the card you reveal is a Success, you are immediately 'Exiled' and may not go on missions for the remainder of the game, although you may still vote and propose missions.\n\n")
			file.write("You may use this ability once per mission as long as you are neither on the mission team nor 'Exiled'. You may choose to not use your ability on any round, even if you would be able to use it.\n");


	# make list of evil players seen to other evil
	if "Oberon" in evil_roles_in_game:
		evil_players = list(set(evil_players) - set([reverse_assignments["Oberon"]]))
	random.shuffle(evil_players)

	if "Mordred" in evil_roles_in_game:
		player_name = reverse_assignments["Mordred"]
		filename = "game/" + player_name
		with open(filename, "w") as file:
			file.write("You are Mordred. (Join us, we have jackets and meet on Thursdays. ~ Andrew and Kath)\n")
			for evil_player in evil_players:
				if evil_player != player_name:
					file.write(evil_player + " is a fellow member of the evil council.\n")

	if "Morgana" in evil_roles_in_game:
		player_name = reverse_assignments["Morgana"]
		filename = "game/" + player_name
		with open(filename, "w") as file:
			file.write("You are Morgana.\n")
			for evil_player in evil_players:
				if evil_player != player_name:
					file.write(evil_player + " is a fellow member of the evil council.\n")

	if "Oberon" in evil_roles_in_game:
		player_name = reverse_assignments["Oberon"]
		filename = "game/" + player_name
		with open(filename, "w") as file:
			file.write("You are Oberon.\n")
			for evil_player in evil_players:
				file.write(evil_player + " is a fellow member of the evil council.\n")
			file.write("\nAbility: Should any mission get to the last proposal of the round, after the people on the mission have been named, you may declare as Oberon to replace one person on that mission with yourself.\n\n")
			file.write("Note: You may not use this ability after two missions have already failed. Furthermore, you may only use this ability once per game.\n")

	if "Agravaine" in evil_roles_in_game:
		player_name = reverse_assignments["Agravaine"]
		filename = "game/" + player_name
		with open(filename, "w") as file:
			file.write("You are Agravaine.\n")
			for evil_player in evil_players:
				if evil_player != player_name:
					file.write(evil_player + " is a fellow member of the evil council.\n")
			file.write("\nAbility: On any mission you are on, after the mission cards have been revealed, should the mission not result in a Fail (such as via a Reversal, requiring 2 fails, or other mechanics), you may formally declare as Agravaine to force the mission to Fail anyway.\n\n");
			file.write("Drawback: You may only play Fail cards while on missions.\n");

	if "Maelegant" in evil_roles_in_game: 
		# write ability to Lancelot's file 
		player_name = reverse_assignments["Maelegant"] 
		filename = "game/" + player_name 
		with open(filename, "w") as file:
			file.write("You are Maelegant. \n\n") 
			for evil_player in evil_players:
				if evil_player != player_name:
					file.write(evil_player + " is a fellow member of the evil council.\n")
			file.write("\nAbility: Reversal \n")	
			file.write("You are able to play Reversal cards while on missions. A Reversal card inverts the result of a mission; a mission that would have succeeded now fails and vice versa. \n \n")
			file.write("Note: In games with at least 7 players, a Reversal played on the 4th mission results in a failed mission if there is only one Fail card, and otherwise succeeds. Reversal does not interfere with Agravaine's ability to cause the mission to fail.")

	if "Colgrevance" in evil_roles_in_game:
		player_name = reverse_assignments["Colgrevance"]
		filename = "game/" + player_name
		with open(filename, "w") as file:
			file.write("You are Colgrevance.\n")
			for evil_player in evil_players:
				if evil_player != player_name:
					file.write(evil_player + " is " + assignments[evil_player] + ".\n")
			if "Oberon" in evil_roles_in_game:
				file.write(reverse_assignments["Oberon"] + " is Oberon.\n")

	# TODO: pelinor + questing beast
	if num_players == 9:
		# write pelinor's information
		pelinor_filename = "game/" + pelinor
		with open(pelinor_filename, "w") as file:
			file.write("You are Pelinor.\n\n")
			file.write("You win if one of the following conditions are met:\n")
			file.write("[1]: No Questing Beast Was Here cards are played.\n")
			file.write("[2]: You are on a mission where a Questing Beast Was Here Card is played, and three missions succeed.\n")
			file.write("[3]: If neither of the previous two conditions are met at the end of the game, you declare as Pelinor prior to Assassination and name the person you believe to be the Questing Beast. You are told if you are correct at the conclusion of any other post-game phases. If you are correct, you win.\n")

		questing_beast_filename = "game/" + questing_beast
		with open(questing_beast_filename, "w") as file:
			file.write("You are the Questing Beast.\n")
			file.write("You must play the 'Questing Beast Was Here' card on missions. Once per game, you can play a Success card instead of a Questing Beast Was Here card.\n\n")
			file.write("You win if all of the following conditions are met:\n")
			file.write("[1]: You play at least one Questing Beast Was Here card.\n")
			file.write("[2]: Either a) Pelinor is never on a mission where a Questing Beast Was Here card is played; or b) 3 Quests fail.\n\n")
			file.write("[3]: Pelinor fails to identify you after the conclusion of the game.\n\n")
			file.write(pelinor + " is Pelinor.\n")


	# write start file
	with open("game/start", "w") as file:
		file.write("The players proposing teams for the first mission are:\n")
		for first_mission_proposer in first_mission_proposers:
			file.write(first_mission_proposer + "\n")
		file.write("\n" + second_mission_starter + " is the starting player of the 2nd round.\n")

	# write do not open
	with open("game/DoNotOpen", "w") as file:
		file.write("Player -> Role\n")
		for player in players:
			file.write(player + " -> " + assignments[player] + "\n")

if __name__ == "__main__":
	main()
