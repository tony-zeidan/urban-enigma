import java.util.HashMap;
import java.util.Hashtable;
import java.util.IllegalFormatException;
import java.util.Set;

public class RubyCommand {

    public static final String PREFIX = "r/";

    public static final HashMap<String, CommandWord> VALID_COMMANDS = new HashMap<String, CommandWord>();
    static
    {
        VALID_COMMANDS.put("help",new CommandWord("help","general","A helpful chart of all commands.",PREFIX+"help"));
        VALID_COMMANDS.put("hello",new CommandWord("hello","general","A way of saying hello to a specific someone.",PREFIX+"hello 'target'"));
        VALID_COMMANDS.put("hi",new CommandWord("hi","general","A way of saying hello to a specific someone.",PREFIX+"hi 'target'"));
        VALID_COMMANDS.put("hey",new CommandWord("hey","general","A way of saying hello to a specific someone.",PREFIX+"hey 'target'"));
        VALID_COMMANDS.put("welcome",new CommandWord("welcome","general","A way of saying hello to a specific someone.",PREFIX+"welcome 'target'"));
        VALID_COMMANDS.put("rand",new CommandWord("rand","math","retrieves a random integer between 1 and 10.",PREFIX+"rand"));
        VALID_COMMANDS.put("say",new CommandWord("say","general","get me to say what you said!",PREFIX+"say 'phrase'"));
        VALID_COMMANDS.put("showlove",new CommandWord("showlove","general","I wanna show you guys some love!",PREFIX+"showlove 'target'"));
        VALID_COMMANDS.put("evaluate",new CommandWord("evaluate","math","a multitude of math operations, including: (+,-,x,/,%,<,>)",PREFIX+"evaluate 'operation' '(num1,num2)'"));
        VALID_COMMANDS.put("purge",new CommandWord("purge","chat","purges 10 messages from chat.",PREFIX+"purge 'number of messages'"));
    }

    private CommandWord firstWord;
    private String[] remainingWords;

    public RubyCommand(String str)
    {
        if (!str.startsWith(PREFIX))
        {
            throw new IllegalArgumentException("Command does not start with prefix");
        }
        firstWord = null;
        remainingWords = null;

        boolean validCommand = false;
        for (String comm : VALID_COMMANDS.keySet())
        {
            if (str.toLowerCase().startsWith(comm,PREFIX.length()))
            {
                validCommand = true;
                firstWord = VALID_COMMANDS.get(comm);
                break;
            }
        }
        if (!validCommand)
        {
            throw new IllegalArgumentException("Command word not valid");
        }
        String[] splitWord = str.split(" ");
        remainingWords = new String[splitWord.length-1];
        for (int i = 1; i < splitWord.length; i ++)
        {
            remainingWords[i-1] = splitWord[i];
        }
    }

    public CommandWord getFirstWord()
    {
        return this.firstWord;
    }

    public String[] getRemainingWords()
    {
        return this.remainingWords;
    }


}
