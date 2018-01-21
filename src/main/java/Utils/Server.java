package Utils;

import Events.MessageEvents;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import javax.security.auth.login.LoginException;

/*This class will set up the JDA object with token to access our bot
* in the main method
* This class will also listen for events and call the appropriate class and handle method when
* the event is an instance of a certain event that we want to scan for*/

public class Server implements EventListener {
	private static String TOKEN = Settings.getToken(); // this token is the
																									// token provided by
																									// discord (Insert
																									// your bot token
																									// here!!!!)
	private MessageEvents messageEvents = new MessageEvents(); // create a new messageEvents object to forward message
																// events to the handler in the events class

	public static void main(String[] args) throws LoginException, InterruptedException {
		try {
			JDA jda = new JDABuilder(AccountType.BOT).setToken(TOKEN).addEventListener(new Server()).buildBlocking();
		} catch (LoginException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof MessageReceivedEvent) {
			Message message = ((MessageReceivedEvent) event).getMessage();
			User author = ((MessageReceivedEvent) event).getAuthor();
			MessageChannel channel = ((MessageReceivedEvent) event).getChannel();
			messageEvents.handle(message, author, channel);
		}
	}
}
