import java.util.*;
import java.io.*;
/* Feed COMPLETED TESTED AND WORKING*/
class Feed{
    private String _title;
    private String _desc;
    public Feed(){}
    public Feed(String a, String b){
        _title = a; _desc = b;
    }
    public void setTitle(String a){_title = a;}
    public void setDesc(String a){_desc = a;}
    public String getTitle(){return _title;}
    public String getDesc(){return _desc;}
    public String toString(){return _title + " " + _desc;}
}
/* NewsFeed COMPLETED TESTED AND WORKING*/
class NewsFeed {
    private ArrayList<Feed> _newsFeed;
    public NewsFeed() {
        _newsFeed = new ArrayList<Feed>();
    }
    public NewsFeed(String fileName) throws IOException {
        _newsFeed = new ArrayList<Feed>();
        Scanner fileScanner = new Scanner(new File(fileName + ".txt"));
        String title;
        String description;
        try {
            while (fileScanner.hasNext()) {
                String newFeed = fileScanner.nextLine();
                String[] t = newFeed.split(";");
                title = t[0];
                description = t[1];
                Feed temp = new Feed(title,description);
                _newsFeed.add(temp);
            }
        }
        catch (Exception e) {
            System.out.println("Error reading file");
            System.exit(1);
        }
    }
    public Feed getRandomFeed(){
        Random rand = new Random();
        int randInt = rand.nextInt(_newsFeed.size());
        return _newsFeed.get(randInt);
    }
}

/**********************************************************/
/* Strategy Pattern Interface/Classes                     */

// Provided: Strategy Interface 
interface AnalysisBehavior {
    double analyze(String[] words, String searchWord);
}

// Task: Complete Class CountIfAnalysis
class CountIfAnalysis implements AnalysisBehavior {
   public double analyze(String[] words, String searchWord){
       boolean found = false;
       int i = 0;
       while (i < words.length && !found){
           System.out.println(words[i]);
           if((words[i].replaceAll("\\p{Punct}","")).equalsIgnoreCase(searchWord)){
               found = true;
           }
           i++;
       }
       if (found) return 1;
       else return -1;
    }
}
// Task: Complete Class CountAllAnalysis ADD DOCUMENTATION
class CountAllAnalysis implements AnalysisBehavior {
    public double analyze(String[] words, String searchWord) {
        double cntr = 0;
        for(int i = 0; i <words.length;i++){
            if((words[i].replaceAll("\\p{Punct}","")).equalsIgnoreCase(searchWord)){
                cntr += 1;
            }
        }
        return cntr;
    }
}


/**********************************************************/
/* Observer Pattern Interface/Classes                     */


interface Subject {  // Notifying about state changes 
    void subscribe(Observer obs);
    void unsubscribe(Observer obs);
    void notifyObservers(Feed f);
}

interface Observer {  // Waiting for notification of state changes 
    void update(Feed f, String platformName);
}


abstract class SocialMediaPlatform implements Subject {
    private String _name;
    private ArrayList<Feed> _feed;
    private ArrayList<Observer> _observers;
    private int _updateRate;

    public SocialMediaPlatform(String n, int x){
        _name = n;
        _feed = new ArrayList<Feed>();
        _observers = new ArrayList<Observer>();
        _updateRate = x;
    }
    public void addFeed(Feed f){_feed.add(f);}
    public Feed getFeed(int i){return _feed.get(i);}
    public int getRate(){return _updateRate;}
    public String getName(){return _name;}
    public int size(){return _feed.size();}
    public void subscribe(Observer obs){_observers.add(obs);}
    public void unsubscribe(Observer obs){_observers.remove(obs);}
    public void notifyObservers(Feed f){
        for (Observer observer : _observers)
            observer.update(f, _name);
    }
    public void generateFeed(NewsFeed nf){

    }
    public double analyzeFeed(String w, AnalysisBehavior ab){

    }
    public String toString(){
        String s = "";
        for (Feed f: _feed)
            s = s + f.getTitle() + ", " + f.getDesc() + "\n";
        return s;
    }
}

// Concrete Social Media Platforms 
class Instagram extends SocialMediaPlatform {
    public Instagram() {
        super("Instagram", 30);  // 30% update rate 
    }
}

class User implements Observer{
    private String _name;
    private ArrayList<SocialMediaPlatform> _myfeeds;
    public User(){_myfeeds = new ArrayList<SocialMediaPlatform>();}
    public User(String s){
        _name = s;
        _myfeeds = new ArrayList<SocialMediaPlatform>();
    }
    public void addPlatform(SocialMediaPlatform smp){_myfeeds.add(smp);}
    public void update(Feed f, String s){
        for (int i=0; i<_myfeeds.size(); i++){
            SocialMediaPlatform smp = _myfeeds.get(i);
            if (smp.getName().equals(s))
                _myfeeds.get(i).addFeed(f);
        }
    }
    public String toString(){
        String s = "";
        for (SocialMediaPlatform smp : _myfeeds) {
            for (int i=0; i<smp.size(); i++){
                Feed f = smp.getFeed(i);
                s = s + f.getTitle() + " " + f.getDesc() + "\n";
            }
        }
        return s;
    }
}

/**********************************************************/
/* Factory Pattern Interface/Classes                      */
/**********************************************************/
/* ADDED
// Factory Creator Interface 
interface SMPFactory {
    SocialMediaPlatform createPlatform();
}

// Concrete Factory classes for each platform 
class InstagramFactory implements SMPFactory {
    public SocialMediaPlatform createPlatform() {
        return new Instagram();
    }
}

*/ //ADDED
public class Main {
    public static void main(String[] args) throws IOException {




        //Feed f = new Feed("orange Apple", "Orange. Orange Orange!@ Orange banna ola fgf");
       // String[] s = f.toString().split(" ");
       // CountAllAnalysis test = new CountAllAnalysis();
        //double t = test.analyze(s,"Orange");
       // System.out.println(t);
        //NewsFeed news = new NewsFeed("hw4data");
       // System.out.println(news.getRandomFeed());

    }
}


        /*  // Create main newsfeed from file
        NewsFeed nf = new NewsFeed("data.txt");

        // Create SMP factories 
        SMPFactory instagramFactory = new InstagramFactory();

        // Create the platforms container and add SMPs 
        ArrayList<SocialMediaPlatform> platforms = new ArrayList<>();
        platforms.add(instagramFactory.createPlatform());

        // Create Users and subscribe 
        User user1 = new User("Alice");
        user1.addPlatform(instagramFactory.createPlatform());
        platforms.get(0).subscribe(user1); // notice 0 - not the best yet

        // Run a simulation to generate random feeds for the SMPs 


        // Perform analysis
        AnalysisBehavior ab = new CountAllAnalysis();
        System.out.println(platforms.get(0).analyzeFeed("guess", new CountAllAnalysis()));

        // Print Users' Contents

    }

} 
*/