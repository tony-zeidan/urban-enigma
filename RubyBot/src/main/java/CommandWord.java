public class CommandWord {

    private String name;
    private String category;
    private String description;
    private String syntax;

    public CommandWord(String name,String category,String description,String syntax)
    {
        this.name = name;
        this.category = category;
        this.description = description;
        this.syntax = syntax;
    }

    public String getName()
    {
        return name;
    }
    public String getCategory()
    {
        return category;
    }
    public String getDescription()
    {
        return description;
    }
    public String getSyntax() { return syntax; }
    public String toString()
    {
        return "**" + name + "** : *" + category + "* : " + description + " - ***" + syntax + "***";
    }
}
