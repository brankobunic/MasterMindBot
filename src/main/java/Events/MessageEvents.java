package Events;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Message.Attachment;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;


import Utils.FragmentGenerator;

public class MessageEvents {
	private String strMessage;
	private String strUser;
	private String strChannel;

	public void handle(Message message, User user, MessageChannel channel) {
		strMessage = message.getContentRaw();
		strUser = user.getName();
		strChannel = channel.getName();
		List<Emote> emotes = message.getEmotes();

		if (strMessage.startsWith("!")) { // checks to see if the message starts with an !, which will be the default
											// command invoke character on this bot
			handleCommands(strMessage, channel); // passes the data off to a separate function to handle commands

		}

		if (strMessage.contains("?map")) {
			FragmentGenerator.MapGenImage(message, channel);
		}
		
		if (strMessage.contains("?noise")) {
			FragmentGenerator.NoiseGenImage(message, channel);
		}

	}

	private void handleCommands(String command, MessageChannel channel) {

		String trimCommand = command.substring(1); // trims off the ! of the command
		String[] commandArgs = trimCommand.split(" "); // splits the message into separate strings using a single white
														// space as the separator
		List<String> commandList = Arrays.asList(commandArgs); // stores the array as a list

		if (trimCommand.equals("test")) {
			channel.sendMessage("https://www.youtube.com/watch?v=vmDDOFXSgAs").queue();
		}
		if (trimCommand.equals("awesome")) {
			channel.sendMessage("Hell yeah!").queue();
		}
		if (trimCommand.equals("really")) {
			channel.sendMessage("Fucking hell yeah!").queue();
		}

	}

}
