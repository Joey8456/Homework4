/*
Joseph Barron
Homework 4
This program is used to load feeds into a social media platform and generate random feeds for users who are
subscribed to that platform, with analysis capabilities.
 */
import java.util.*;
import java.io.*;
/* Feed */
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
/* NewsFeed */
class NewsFeed {
    private ArrayList<Feed> _newsFeed;
    public NewsFeed() {
        _newsFeed = new ArrayList<Feed>();
    }
    public NewsFeed(String fileName) throws IOException {
        _newsFeed = new ArrayList<Feed>();
        Scanner fileScanner = new Scanner(new File(fileName));
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
/* Strategy Pattern Interface/Classes  */
interface AnalysisBehavior {
    double analyze(String[] words, String searchWord);
}
class CountIfAnalysis implements AnalysisBehavior {
   public double analyze(String[] words, String searchWord){
       boolean found = false;
       int i = 0;
       while (i < words.length && !found){
           if((words[i].replaceAll("\\p{Punct}","")).equalsIgnoreCase(searchWord)){
               found = true;
           }
           i++;
       }
       if (found) return 1;
       else return -1;
    }
}
class CountAllAnalysis implements AnalysisBehavior {
    public double analyze(String[] words, String searchWord) {
        double cntr = 0;
        for (int i = 0; i < words.length; i++) {
            if ((words[i].replaceAll("\\p{Punct}", "")).equalsIgnoreCase(searchWord)) {
                cntr += 1;
            }
        }
        return cntr;
    }
}
/* The actual searching for the keywords is the same in both, the only difference is in CountALL I did an
exhaustive search and in CountIf I did the opposite. The value returned by both also varies, CountAll returns a
counter of the key word and CountIf returns a 1 if true and a -1 if false. A double is a good return type because
doubles avoid any issues with further calculations or uses of that number returned avoiding the need to convert
the number from integer to a double*/

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
    public void generateFeed(NewsFeed nf) {
        Random rand = new Random();
        int randInt = rand.nextInt(100);
        if (randInt < _updateRate) {
            Feed f = nf.getRandomFeed();
            _feed.add(f);
            notifyObservers(f);
        }
    }
    public double analyzeFeed(String w, AnalysisBehavior ab) {
        String[] s = toString().split(" ");
        return ab.analyze(s, w);
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
    /*I chose snapchat and tiktok because they are popular app with different expected update rates.Snapchat
     does not have much of an algorithm so less frequent rate makes sense whereas TikTok is more alive and changing*/
}
class TikTok extends SocialMediaPlatform {
    public TikTok(){
        super("TikTok",30);
    }
}
class SnapChat extends SocialMediaPlatform{
    public SnapChat(){
        super("SnapChat",10);
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
    public void addPlatform(SocialMediaPlatform smp){
        _myfeeds.add(smp);
    }
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
/* Factory Pattern Interface/Classes */
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
class TikTokFactory implements SMPFactory {
    public SocialMediaPlatform createPlatform() {
        return new TikTok();
    }
}
class SnapChatFactory implements SMPFactory {
    public SocialMediaPlatform createPlatform() {
        return new SnapChat();
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        // Create main newsfeed from file
        NewsFeed nf = new NewsFeed("hw4data.txt");
        // Create SMP factories 
        SMPFactory instagramFactory = new InstagramFactory();
        SMPFactory tikTokFactory = new TikTokFactory();
        SMPFactory snapChatFactory = new SnapChatFactory();
        // Create the platforms container and add SMPs 
        ArrayList<SocialMediaPlatform> platforms = new ArrayList<>();
        platforms.add(instagramFactory.createPlatform());
        platforms.add(snapChatFactory.createPlatform());
        platforms.add(tikTokFactory.createPlatform());
        // Create Users and subscribe
        User user1 = new User("Alice");
        User user2 = new User( "Joey");
        User user3 = new User();
        user1.addPlatform(instagramFactory.createPlatform());  //User1 subscribed to all
        user1.addPlatform(tikTokFactory.createPlatform());
        user1.addPlatform(snapChatFactory.createPlatform());
        user2.addPlatform(tikTokFactory.createPlatform());    //User2 subscribed to two
        user2.addPlatform(snapChatFactory.createPlatform());
        user3.addPlatform(tikTokFactory.createPlatform());    //User3 subscribed to one
        platforms.get(0).subscribe(user1);
        platforms.get(1).subscribe(user1);
        platforms.get(2).subscribe(user1);
        platforms.get(1).subscribe(user2);
        platforms.get(2).subscribe(user2);
        platforms.get(2).subscribe(user3);
        // Run a simulation to generate random feeds for the SMPs
        System.out.println("Loading Feeds for 20 seconds stand by ...");
        long startTime = System.currentTimeMillis();
        long duration = 20000;
        while(System.currentTimeMillis() - startTime < duration){
            for (SocialMediaPlatform platform : platforms) {
                platform.generateFeed(nf);
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("Finished Loading");
        // Perform analysis
        AnalysisBehavior interesting = new CountAllAnalysis();
        for (SocialMediaPlatform platform : platforms) {
            double test = platform.analyzeFeed("new", interesting);
            System.out.println((platform.getName()) + " has the word 'new' in it " + test + " times!");
        }
        AnalysisBehavior exists = new CountIfAnalysis();
        for (SocialMediaPlatform platform : platforms) {
            double test = platform.analyzeFeed("AI", exists);
            if(test == 1) {
                System.out.println((platform.getName()) + " does contain the word 'AI'");
            }
            else{
                System.out.println((platform.getName()) + " does not contain the word 'AI'");
            }
        }
        // Print Users' Contents
        System.out.println("User1 Contents\n" + user1.toString());
        System.out.println("User2 Contents\n" + user2.toString());
        System.out.println("User3 Contents\n" + user3.toString());
    }
}