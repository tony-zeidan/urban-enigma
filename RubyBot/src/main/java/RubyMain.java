//jda imports
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

//other imports
import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Random;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides the main functionality for the Ruby discord bot.
 *
 * @author Tony Abou-Zeidan
 * @version Jan 31, 2020 : Version 1.0.1
 */
public class RubyMain extends ListenerAdapter {

    public static final String BOT_TOKEN = "BOT_TOKEN";

    //Regex pattern for math operations
    public static final Pattern BRACKET_PATTERN = Pattern.compile("\\((-?\\d{0,4}(.\\d{0,4})?),(-?\\d{0,4}(.\\d{0,4})?)\\)");
    private static final DecimalFormat df = new DecimalFormat("0.0");
    private Matcher patternMatcher;

    public static void main(String[] args)
    {
        try {
            JDA api = new JDABuilder(BOT_TOKEN).addEventListeners(new RubyMain()).build();
        }
        catch (LoginException e)
        {
            System.out.println("Login failed");
        }
    }

    /**
     * This method is called every time a message is received.
     * This contains the main functionality for command processing of the bot.
     *
     * @param event The message received event
     */
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event)
    {
        //get message information
        Message msg = event.getMessage();
        TextChannel channel = event.getTextChannel();
        Guild guild = event.getGuild();
        Member author = event.getMember();
        Member self = guild.getSelfMember();

        // ignore other bots and ourself
        if (event.getAuthor().isBot()) {
            return;
        }

        /*
        Hashtable for printing and storing current message information
         */
        Hashtable<String,String> messageInfo = new Hashtable<String,String>();
        messageInfo.put("Guild Name",guild.getName());
        messageInfo.put("Channel Name",channel.getName());
        messageInfo.put("Channel Type",channel.getType().name());
        messageInfo.put("Author",author.getEffectiveName());
        messageInfo.put("Message",msg.getContentDisplay());
        System.out.println(messageInfo);
        try
        {
                //command information
                RubyCommand input = new RubyCommand(msg.getContentDisplay());
                String first = input.getFirstWord().getName();
                String category = input.getFirstWord().getCategory();
                String[] remain = input.getRemainingWords();
                int arglen = remain.length;

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.RED);
                //check if the bot has permissions to send messages for commands that require a reply
                if (category.equals("general")||category.equals("math")) {

                    if (self.hasPermission(channel,Permission.MESSAGE_WRITE))
                        /*
                        Help command. The bot will post a list of commands if no other arguments are given.
                        If a second parameter is given, the bot will give specific information of the command.
                        Syntax: preix + help OR prefix + help 'target command'
                         */
                        if (first.equals("help")) {

                            if (arglen==0) {
                                //concat all commands and send
                                String result = "";
                                for (String comm : RubyCommand.VALID_COMMANDS.keySet()) {
                                    result += RubyCommand.VALID_COMMANDS.get(comm) + "\n\n";
                                }
                                embedBuilder.setTitle("Help!");
                                embedBuilder.addField("Command List", result, false);
                                embedBuilder.setFooter("", guild.getIconUrl());
                                channel.sendMessage(embedBuilder.build()).queue();
                            }
                            //the user is looking up a specific command
                            else if (arglen==1)
                            {
                                CommandWord lookup = null;
                                for (String comm:RubyCommand.VALID_COMMANDS.keySet())
                                {
                                    if (remain[0].equals(comm))
                                    {
                                        lookup = RubyCommand.VALID_COMMANDS.get(comm);
                                        break;
                                    }
                                }
                                //specific command found
                                if (lookup!=null)
                                {
                                    embedBuilder.setTitle(RubyCommand.PREFIX + lookup.getName());
                                    embedBuilder.addField("Category: " + lookup.getCategory(),lookup.getDescription()+"\n\n*"+lookup.getSyntax()+"*",false);
                                    embedBuilder.setFooter("", guild.getIconUrl());
                                    channel.sendMessage(embedBuilder.build()).queue();
                                }
                                //command could not be found
                                else
                                {
                                    channel.sendMessage("Command not found!").queue();
                                }
                            }
                        }
                        /*
                        Hello command. The bot will say hello to the user given in the command.
                        If no user is specified, it will just give a general hello.
                        Syntax: preix + hello 'target user'
                         */
                        else if (first.equals("hello") || first.equals("hi") || first.equals("hey") || first.equals("welcome")) {

                            String result = "";
                            for (User u : msg.getMentionedUsers()) {
                                result += "<@" + u.getId() + "> ";
                            }
                            channel.sendMessage("Hi there " + result + "!").queue();

                        }
                        /*
                        Showlove command. The bot will send a nice greeting to the specified user. If no
                        user was specified, the bot will show love to everyone.
                         */
                        else if (first.equals("showlove"))
                        {
                            if (arglen == 0)
                            {
                                if (self.hasPermission(Permission.MESSAGE_MENTION_EVERYONE))
                                {
                                    channel.sendMessage("Hope you are having a great day @everyone! <3").queue();
                                    return;
                                }
                                else
                                {
                                    channel.sendMessage("I do not have enough permissions for that!").queue();
                                    return;
                                }
                            }
                            else if (arglen==1)
                            {
                                List<IMentionable> mentions = msg.getMentions();
                                if (mentions.size()==1) {
                                    IMentionable mention = mentions.get(0);
                                    channel.sendMessage("Hope you are having a great day " + mention.getAsMention() + "! <3").queue();
                                    return;
                                }
                            }
                            else
                            {
                                channel.sendMessage("You have input too many arguments!").queue();
                            }
                        }
                        /*
                        Say command. The bot will repeat the string given to it.
                        Syntax: preix + say 'phrase'
                         */
                        else if (first.equals("say")) {

                            if (arglen > 0) {
                                //concat the words given into a string and send it
                                String result = remain[0];
                                for (int i = 1; i < remain.length; i++) {
                                    result += " " + remain[i];
                                }
                                channel.sendMessage(result).queue();
                            } else {
                                channel.sendMessage("You can't make me say nothing!").queue();
                            }
                        }
                        /*
                        Rand command. The bot will generate a random integer between 1 and 10 if no
                        numbers are specified. If two numbers are specified, the range will be taken into
                        account.
                        Syntax: prefix + rand OR prefix + rand 'minimum' 'maximum'
                         */
                        else if (first.equals("rand")) {
                            double max = 10.0;
                            double min = 1.0;
                            Random r = new Random();

                            //try for only one argument
                            if (arglen == 1)
                            {
                                try
                                {
                                    double[] maxmin = getMathNumbers(remain[0]);
                                    if (maxmin!=null)
                                    {
                                        min = maxmin[0];
                                        max = maxmin[1];

                                        //the first number can not be greater than the second
                                        if (min >= max)
                                        {
                                            channel.sendMessage("Your first number must be less than your second!").queue();
                                            return;
                                        }
                                        //both numbers >= 0
                                        else if (min<0||max<0)
                                        {
                                            channel.sendMessage("Your numbers must be greater than zero!").queue();
                                            return;
                                        }
                                    }
                                }
                                //integers could not be parsed
                                catch (Exception e)
                                {
                                    channel.sendMessage("Your parameters are invalid!" ).queue();
                                    return;
                                }
                            }
                            //too many args
                            else if (arglen>1)
                            {
                                channel.sendMessage("You gave too many parameters!").queue();
                                return;
                            }
                            int result = r.nextInt((int)max-(int)min)+((int)min)+1;
                            //set the properties of the embedded message and send it
                            embedBuilder.setTitle("Random Generation");
                            embedBuilder.addField("Number Generated", "" + result, false);
                            embedBuilder.setFooter("Between " + min + " and " + max, guild.getIconUrl());
                            channel.sendMessage(embedBuilder.build()).queue();
                        }
                        /*
                         * Evaluate command. Provides a simple calculator with a variety of options.
                         * Syntax: prefix + evaluate 'operation' '(num1,num2)'
                         */
                        else if (first.equals("evaluate"))
                        {
                            if (arglen==2)
                            {
                                String oper = remain[0].toLowerCase();
                                double[] mathNums = getMathNumbers(remain[1]);
                                if (mathNums!=null)
                                {
                                    String title = "";
                                    double result = 0.0;
                                    double numOne = mathNums[0];
                                    double numTwo = mathNums[1];
                                    //add oper
                                    if (oper.equals("+")||oper.equals("add")||oper.equals("sum"))
                                    {
                                        title = "Addition";
                                        oper="+";
                                        result = numOne+numTwo;
                                    }
                                    //sub oper
                                    else if (oper.equals("-")||oper.equals("sub")||oper.equals("subtract"))
                                    {
                                        title = "Subtraction";
                                        oper = "-";
                                        result = numOne-numTwo;
                                    }
                                    //mult oper
                                    else if (oper.equals("*")||oper.equals("x")||oper.equals("mult")||oper.equals("multiply"))
                                    {
                                        title = "Multiplication";
                                        oper = "*";
                                        result = numOne*numTwo;
                                    }
                                    //div oper
                                    else if (oper.equals("/")||oper.equals("div")||oper.equals("divide"))
                                    {
                                        title = "Division";
                                        oper = "/";
                                        result = numOne/numTwo;
                                    }
                                    //rem oper
                                    else if (oper.equals("%")||oper.equals("rem")||oper.equals("remainder")||oper.equals("mod")||oper.equals("modulus"))
                                    {
                                        title = "Remainder";
                                        oper = "%";
                                        result = numOne%numTwo;
                                    }
                                    //min oper
                                    else if (oper.equals("min")||oper.equals("<"))
                                    {
                                        title = "Minimum";
                                        oper = "<";
                                        result = (numOne<numTwo)?numOne:numTwo;
                                    }
                                    //max oper
                                    else if (oper.equals("max")||oper.equals(">"))
                                    {
                                        title = "Maximum";
                                        oper = ">";
                                        result = (numOne>numTwo)?numOne:numTwo;
                                    }
                                    //operation not in list
                                    else
                                    {
                                        channel.sendMessage("Your operation must be valid!").queue();
                                        return;
                                    }

                                    //send as an embed
                                    embedBuilder.setTitle("Computation");
                                    embedBuilder.addField(title,numOne + " " + oper + " " + numTwo + " = " + df.format(result),false);
                                    embedBuilder.setFooter("Math",guild.getIconUrl());
                                    channel.sendMessage(embedBuilder.build()).queue();
                                }
                            }
                            else
                            {
                                channel.sendMessage("You input too little or too many arguments!");
                            }
                        }
                    }
                //chat category of commands
                else if (category.equals("chat")) {
                    if (self.hasPermission(Permission.MESSAGE_MANAGE)) {
                        /*
                        Purge command. Deletes 'n' messages from the current text channel. If no number
                        is specified it will delete 10 messages by default.
                         */
                        if (first.equals("purge")) {
                            int deleteAmount = 10;
                            if (arglen==1)
                            {
                                try
                                {
                                    System.out.println(remain[0]);
                                    System.out.println(Integer.parseInt(remain[0]));
                                    deleteAmount = Integer.parseInt(remain[0]);
                                    if (deleteAmount<0)
                                    {
                                        channel.sendMessage("You can not purge negative messages!").queue();
                                        return;
                                    }
                                    else if (deleteAmount>50)
                                    {
                                        channel.sendMessage("You can only purge up to 50 messages at one time!").queue();
                                        return;
                                    }
                                }
                                catch(Exception e)
                                {
                                    System.out.print("Arg not number");
                                    return;
                                }
                            }
                            //get message history
                            List<Message> messageList;
                            MessageHistory history = new MessageHistory(event.getTextChannel());
                            messageList = history.retrievePast(deleteAmount).complete();
                            //channel.deleteMessages(messageList).queue();

                            //delete each message one by one
                            for (Message m:messageList)
                            {
                                m.delete().complete();
                            }

                            if (self.hasPermission(channel,Permission.MESSAGE_WRITE))
                                channel.sendMessage(deleteAmount + " messages purged.").queue();
                        }
                    }
                }
        }
        catch (Exception e)
        {
            System.out.println("Invalid command format");
        }
    }

    /**
     * Method to determine if a string fits the bracket pattern defined above
     * and find the numbers contained within the pattern
     *
     * @param s The string to be processed
     * @return The numbers found in the bracket pattern, null if nothing found
     */
    private double[] getMathNumbers(String s)
    {
        patternMatcher = BRACKET_PATTERN.matcher(s);
        if (patternMatcher.matches())
        {
            return new double[]{Double.parseDouble(patternMatcher.group(1)),Double.parseDouble(patternMatcher.group(3))};
        }
        return null;
    }


}
