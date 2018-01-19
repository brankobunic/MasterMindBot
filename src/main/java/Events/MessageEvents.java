package Events;

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

public class MessageEvents {
	private String strMessage;
	private String strUser;
	private String strChannel;

	public void handle(Message message, User user, MessageChannel channel) {
		strMessage = message.getContentRaw();
		strUser = user.getName();
		strChannel = channel.getName();

		if (strMessage.startsWith("!")) { // checks to see if the message starts with an !, which will be the default
											// command invoke character on this bot
			handleCommands(strMessage, channel); // passes the data off to a separate function to handle commands
		}

		if (strMessage.equals("?map")) {

			File img = convertToFile(message, channel);
			channel.sendMessage("This will take a few moments...").queue();

			BufferedImage buff;
			try {
				Gradient g = new Gradient(Color.MAGENTA, Color.DARK_GRAY, true);

				buff = ImageIO.read(img);

				MapGenFragment gen = new MapGenFragment(Vector2.ZERO, Vector2.ONE);

				ImageConvertFragment imgFragment = new ImageConvertFragment(buff, gen, ColorProperty.V);

				RenderFragment render = new RenderFragment(imgFragment, 50, g);
				Builder builder = new Builder(render.build(new Vector2(200)));

				builder.startBuild();
				builder.joinAll();

				channel.sendFile(writeByteArray(builder.getImgs()), "image.gif").queue();

				channel.sendMessage("Voilà!!").queue();
			} catch (IOException | IllegalMapSizeException e) {
				e.printStackTrace();
			}

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

	public File convertToFile(Message message, MessageChannel channel) {

		List<Attachment> lista = message.getAttachments();
		Attachment att = null;
		File img = new File("img.jpg");

		if (lista.size() != 0) {
			att = lista.get(0);
			InputStream input = null;
			OutputStream out = null;

			try {
				input = att.getInputStream();

				out = new FileOutputStream(img);

				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = input.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		} else {
			channel.sendMessage("Please add an image").queue();
		}

		return img;
	}

	public static byte[] writeByteArray(BufferedImage[] imgSequence) {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try {
			try (ImageOutputStream output = new MemoryCacheImageOutputStream(outStream)) {
				GifSequenceWriter writer = new GifSequenceWriter(output, BufferedImage.TYPE_INT_RGB, 0, true);
				for (BufferedImage B : imgSequence) {
					writer.writeToSequence(B);
				}
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return outStream.toByteArray();
	}
}
