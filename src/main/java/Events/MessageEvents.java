package Events;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Message.Attachment;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import com.tiggerbiggo.prima.core.Builder;
import com.tiggerbiggo.prima.core.FileManager;
import com.tiggerbiggo.prima.core.GifSequenceWriter;
import com.tiggerbiggo.prima.core.Vector2;
import com.tiggerbiggo.prima.exception.IllegalMapSizeException;
import com.tiggerbiggo.prima.graphics.Gradient;
import com.tiggerbiggo.prima.processing.ColorProperty;
import com.tiggerbiggo.prima.processing.fragment.ImageConvertFragment;
import com.tiggerbiggo.prima.processing.fragment.MapGenFragment;
import com.tiggerbiggo.prima.processing.fragment.RenderFragment;

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
