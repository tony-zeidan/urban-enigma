import java.util.*;

/**
 * A simple model of an auction.
 * The auction maintains a list of lots of arbitrary length.
 *
 * @author David J. Barnes and Michael Kolling.
 * @version 2006.03.30
 *
 * @author (of AuctionSkeleton) Lynn Marshall
 * @version 2.0
 * 
 * @author Tony Abou-Zeidan
 * @version October 5th, 2019
 */
public class Auction
{
    /** The list of Lots in this auction. */
    private ArrayList<Lot> lots;

    /** 
     * The number that will be given to the next lot entered
     * into this auction.  Every lot gets a new number, even if some lots have
     * been removed.
     */
    private int nextLotNumber;

    /**
     * Determines whether the auction is open.
     * True if the acution is open.
     * False if the auction is closed.
     */
    private boolean openAuction;

    /**
     * Create a new auction given no parameters.
     */
    public Auction()
    {
        lots = new ArrayList<Lot>();
        nextLotNumber = 1;
        openAuction = true;
    }
    
    /**
     * Create a new auction based on another.
     * If the given auction is open or null, create a new auction with no lots (see constructor above).
     * If and only if the given auction is closed, take the lots that were not sold in the given auction
     * and create a new auction with them.
     * 
     * @param baseAuction the auction that the new auction will be based upon.
     */
    
    public Auction(Auction baseAuction) 
    {
        
        if (baseAuction.openAuction || baseAuction == null) 
        { 
            lots = new ArrayList<Lot>();
            nextLotNumber = 1;
            openAuction = true;
        } else 
        {
            lots = baseAuction.getNoBids();
            nextLotNumber = baseAuction.nextLotNumber;
            openAuction = true;
        }
        
    }


    /**
     * Attempt to enter a new lot into the Auction. 
     * Returns false if the auction is not open or the description given is null.
     * Returns true if the lot was successfuly entered in the Auction.
     *
     * @param description A string description of the lot.
     */
    public boolean enterLot(String description)
    {
        if (!openAuction || description == null) {
            return false;
        }
        lots.add(new Lot(nextLotNumber, description));
        nextLotNumber++;
        return true;
    }

    /**
     * Attempts to print a the list of lots.
     * If theere are no lots to be printed, prints message.
     */
    public void showLots()
    {
        if (lots.size() > 0) {
            for(Lot lot : lots) {
                System.out.println("\n{" + lot.toString() + "}");
            }
        } else {
            System.out.println("\nSorry! There are no lots to be printed!");
        }
    }
    
    /**
     * Attempts to bid for the given lot.
     * If the lot, bidder, or bid are not valid returns false and prints message.
     * If the bid was not successful, but the parameters are valid,
     * returns true and prints information (Bid Success, Lot Number, Highest Bid).
     * If the bid was successful and the parameters are valid,
     * returns true and prints information (Bid Success, Lot Number, Highest Bid, Highest Bidder).
     *
     * @param number The lot number being bid for.
     * @param bidder The person bidding for the lot.
     * @param value  The value of the bid.
     * 
     * @return the validity of the parameters.
     */
    public boolean bidFor(int lotNumber, Person bidder, long value)
    {
        Lot currLot = getLot(lotNumber);
        if (currLot == null || bidder == null || value < 0 || !openAuction) {
            return false;
        }
        boolean bidOnLot = currLot.bidFor(new Bid(bidder,value));
        String message = "\n{Bid Success: " + bidOnLot + "}\n{Lot Number: " + lotNumber + "}\n{Highest Bid: " + currLot.getHighestBid().getValue() + "}\n";  
        if (bidOnLot) {
            message += "{Highest Bidder: " + bidder.getName() + "}\n";
        }
        System.out.println(message);
        return true;
    }


    /**
     * Attempts to retrieve the lot with the given number.
     * Will return null if the lot with the given number does not exist.
     *
     * @param lotNumber The number of the lot to return.
     *
     * @return the Lot with the given number
     */
    public Lot getLot(int lotNumber)
    {
        for (Lot tempLot : lots) {
            if (tempLot.getNumber() == lotNumber) {
                return tempLot;
            }
        }
        return null; 
    }
    
    /**
     * Closes the current instance of the auction class.
     * Prints information for all the lots in the auction:
     * ->If the lot was sold print (Lot Number, Description, Highest Bidder/Bid, If Sold)
     * ->If the lot was not sold print (Lot Number, Description, If Sold)
     * 
     * @return true if the auction was already closed, otherwise false.
     */
    public boolean close()
    {
        if (!openAuction) {
            return false;
        }
        openAuction = false;
        System.out.println("");
        for (Lot tempLot : lots) {
            Bid highBid = tempLot.getHighestBid();
            if (highBid == null) {
                System.out.println("{Lot Number: " + tempLot.getNumber() + "}\n{Description: " + tempLot.getDescription() + "}\n{Sold: false}\n");
            } else { 
                System.out.println("{Lot Number: " + tempLot.getNumber() + "}\n{Description: " + tempLot.getDescription() + "}\n{Highest Bidder/Bid: " + highBid.getBidder().getName() + "/$" + highBid.getValue() + "}\n{Sold: true}\n");
            }
        }
        return true;
    }
    
    /**
     * Goes through all the lots in the current instance of the auction class,
     * gathering all of the lots that have not sold. 
     * Returns an ArrayList of the null auctions.
     * 
     * @return an ArrayList of the Lots which currently have no bids
     */
    public ArrayList<Lot> getNoBids()
    {
       ArrayList<Lot> nullLots = new ArrayList<Lot>();
       
       for (Lot tempLot : lots) {
           if (tempLot.getHighestBid() == null) {
               nullLots.add(tempLot);
            }
       }
       return nullLots;
    }
    
    /**
     * Attempts to remove the lot with the given number.
     * Returns true if the lot has been successfully removed, otherwise false.
     *
     * @param number The number of the lot to be removed.
     * 
     * @return the success of removing the lot.
     */
    public boolean removeLot(int number)
    {
        if (openAuction) {
            Iterator<Lot> lotAccess = lots.iterator();
            while (lotAccess.hasNext()) {
                Lot iterItem = lotAccess.next();
                if (iterItem.getNumber() == number && iterItem.getHighestBid() == null) {
                    lotAccess.remove();
                    return true;
                }
            }
        }
        return false;
    }
          
}