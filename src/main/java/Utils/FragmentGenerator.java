package Utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import com.tiggerbiggo.prima.core.Builder;
import com.tiggerbiggo.prima.core.GifSequenceWriter;
import com.tiggerbiggo.prima.core.Vector2;
import com.tiggerbiggo.prima.exception.IllegalMapSizeException;
import com.tiggerbiggo.prima.graphics.Gradient;
import com.tiggerbiggo.prima.processing.ColorProperty;
import com.tiggerbiggo.prima.processing.fragment.CombineFragment;
import com.tiggerbiggo.prima.processing.fragment.CombineType;
import com.tiggerbiggo.prima.processing.fragment.ConstFragment;
import com.tiggerbiggo.prima.processing.fragment.ImageConvertFragment;
import com.tiggerbiggo.prima.processing.fragment.MapGenFragment;
import com.tiggerbiggo.prima.processing.fragment.RenderFragment;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Message.Attachment;

public class FragmentGenerator {

	public static void MapGenImage(Message message, MessageChannel channel) {

		List<Emote> emotes = message.getEmotes();

		Color c1;
		Color c2;

		if (!emotes.isEmpty()) {
			c1 = checkColor(emotes.get(0));
			c2 = checkColor(emotes.get(1));
		} else {
			c1 = Color.MAGENTA;
			c2 = Color.BLUE;
		}

		File img = convertToFile(message, channel);
		channel.sendMessage("This will take a few moments...").queue();

		BufferedImage buff;

		try {
			Gradient g = new Gradient(c1, c2, true);

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

	public static void NoiseGenImage(Message message, MessageChannel channel) {

		List<Emote> emotes = message.getEmotes();

		Color c1;
		Color c2;

		if (!emotes.isEmpty()) {
			c1 = checkColor(emotes.get(0));
			c2 = checkColor(emotes.get(1));
		} else {
			c1 = Color.MAGENTA;
			c2 = Color.BLUE;
		}

		File img = convertToFile(message, channel);
		channel.sendMessage("This will take a few moments...").queue();

		BufferedImage buff;

		try {
			Gradient g = new Gradient(c1, c2, true);

			buff = ImageIO.read(img);

			NoiseGenerator noise = new NoiseGenerator();
			CombineFragment lessNoise = new CombineFragment(noise, new ConstFragment(new Vector2(0.1)),
					CombineType.MULTIPLY);
			MapGenFragment gen = new MapGenFragment(Vector2.ZERO, Vector2.ONE);

			ImageConvertFragment imgFragment = new ImageConvertFragment(buff, gen, ColorProperty.V);

			CombineFragment combine = new CombineFragment(lessNoise, imgFragment, CombineType.MULTIPLY);

			RenderFragment render = new RenderFragment(combine, 60, g);

			Builder builder = new Builder(render.build(new Vector2(200)));

			builder.startBuild();
			builder.joinAll();

			channel.sendFile(writeByteArray(builder.getImgs()), "image.gif").queue();

			channel.sendMessage("Voilà!!").queue();
		} catch (IOException | IllegalMapSizeException e) {
			e.printStackTrace();
		}

	}

	public static Color checkColor(Emote e) {

		Color c = null;

		switch (e.getName()) {

		case "cY":
			c = Color.YELLOW;
			break;

		case "cW":
			c = Color.WHITE;
			break;

		case "cR":
			c = Color.RED;
			break;

		case "cM":
			c = Color.PINK;
			break;

		case "cK":
			c = Color.BLACK;
			break;

		case "cG":
			c = Color.GREEN;
			break;

		case "cC":
			c = Color.CYAN;
			break;

		case "cB":
			c = Color.BLUE;
			break;
		}

		return c;
	}

	public static File convertToFile(Message message, MessageChannel channel) {

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
