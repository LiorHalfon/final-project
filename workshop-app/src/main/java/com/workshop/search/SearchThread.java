package com.workshop.search;

import com.aylien.textapi.responses.Article;
import com.aylien.textapi.responses.Sentiment;
import com.aylien.textapi.responses.Summarize;
import com.aylien.textapi.responses.TaxonomyClassifications;
import com.google.gson.Gson;
import com.workshop.service.SearchResultsService;
import com.workshop.service.mail.MailComposer;
import com.workshop.model.User;
import com.workshop.model.UserFeedback;
import com.workshop.service.UserFeedbackService;
import com.workshop.service.UserService;
import com.workshop.service.mail.MailSender;
import de.svenjacobs.loremipsum.LoremIpsum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
@Scope("prototype")
public class SearchThread extends Thread {

    @Autowired
    private UserService userService;

    @Autowired
    private UserFeedbackService userFeedbackService;

    @Autowired
    private SearchResultsService searchResultsService;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private MailComposer mailComposer;

    private List<String> haveToAppear;
    private List<String> cantAppear;
    private String queryValueParam;
    private User user;
    private int resultsId;

    @Override
    public void run() {
        ArrayList<String> blackListDomains = new ArrayList<>();
        List<RelevantNews> results;
        boolean testing = true;

        String[] data = queryValueParam.split(",");
        ArrayList<String> queries = new ArrayList<>(Arrays.asList(data));
        queries.remove("");

        List<UserFeedback> userFB = userFeedbackService.getUserFeedbacks(user, UserFeedback.ActivityType.BLOCK_DOMAIN);
        if (userFB != null) {
            for (UserFeedback activity : userFB) {
                if (activity.getActivityType().equals(UserFeedback.ActivityType.BLOCK_DOMAIN)) {
                    blackListDomains.add(activity.getUrl());
                }
            }
        }

        String json;
        if(testing == false) {
            NewsFinder newsFinder = new BingNewsFinder();
            newsFinder.init(haveToAppear, cantAppear, blackListDomains, queries, user);
            try {
                newsFinder.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            results = newsFinder.getResults();

            List<RelevantNewsView> resultView = new ArrayList<>();
            for (int i = 0; i < results.size(); i++) {
                resultView.add(new RelevantNewsView(results.get(i), resultsId, i));
            }

            Gson gson = new Gson();
            json = gson.toJson(resultView);
        }
        else{
            json="[{\"id\": "+resultsId+", \"url\": \"http://www.businessinsider.com/apple-iphone-8-rumor-roundup-tim-cook-2017-9\", \"index\": 0, \"title\": \"Everything we know about the Apple iPhone 8 — which should be announced next week\", \"domain\": \"businessinsider.com\", \"summary\": \"\", \"imageUrl\": \"http://static1.businessinsider.com/image/59494365e592ed19008b5906-1190-625/everything-we-know-about-the-apple-iphone-8-which-should-be-announced-next-week.jpg\", \"sentiment\": {\"text\": \"Apple is hosting a big press event at its new \\\"spaceship\\\" headquarters in Cupertino on September 12, 2017. Here's what the company might announce based on various rumors and reports that have popped up over the last few months.\", \"polarity\": \"positive\", \"subjectivity\": \"unknown\", \"polarityConfidence\": 0.4411025643348694, \"subjectivityConfidence\": 0}, \"classifications\": [\"Construction\", \"Mac Support\", \"Technology & Computing\", \"Business\"], \"sentimentImageUrl\": \"https://cdn2.iconfinder.com/data/icons/emoticons-17/24/emoticons-01-32.png\"}, {\"id\": 48, \"url\": \"http://time.com/4927440/iphone-8-edition-names/\", \"index\": 1, \"title\": \"What Will the New iPhone Be Called? Here Are the Rumors So Far\", \"domain\": \"time.com\", \"summary\": \"Since the iPhone 4s launched in 2011, Apple has tick-tocked between giving its new smartphones a name with a single whole number and a name with the letter “s” tagged on every other year. \\n\\nAs this is a “tock” year, rumors suggest these new iPhones will run on the same processor as Apple’s 10th anniversary edition iPhone, but it’s unclear if they’ll land any of the high-end model’s other bells and whistles. \\n\\nAccording to the same 9to5Mac report, Apple plans to name its 10th anniversary smartphone the iPhone Edition. \\n\\n\", \"imageUrl\": \"https://timedotcom.files.wordpress.com/2017/06/gettyimages-476377530.jpg?w=720\", \"sentiment\": {\"text\": \"While Apple has kept understandably quiet about its next iPhones, incessant leaks have painted a picture of what we can expect: a new design with an OLED screen that dominates the phone’s front, a 3D sensor for facial recognition and the removal of the home button are all part of Apple’s next major iPhone, according to multiple reports. But one piece of information that’s been less consistently bandied by rumormongers is what, precisely, Apple plans to call its next-gen smartphones. On September 12, the company is widely expected to debut three new iPhones: a 10-year anniversary edition iPhone that will incorporate the changes mentioned above, and two new models that update the current iPhone 7 and 7 Plus models. Here’s what we know so far about what these new phones might be called. iPhone 7s and iPhone 7s Plus If Apple follows the naming convention it’s established in recent years, updated versions of Apple’s current iPhone 7 and iPhone 7 Plus will be called the iPhone 7s and iPhone 7s Plus. Since the iPhone 4s launched in 2011, Apple has tick-tocked between giving its new smartphones a name with a single whole number and a name with the letter “s” tagged on every other year. These “s” generation iPhones typically include new technologies while maintaining the same form factor as their predecessors: the iPhone 4s looked just like the iPhone 4 but introduced Siri, for instance. Similarly, the iPhone 5s was the same size and shape as the iPhone 5, but was the first to have Touch ID. As this is a “tock” year, rumors suggest these new iPhones will run on the same processor as Apple’s 10th anniversary edition iPhone, but it’s unclear if they’ll land any of the high-end model’s other bells and whistles. iPhone 8 If Apple does release an iPhone 7s and 7s Plus next month, similar logic — calibrated for this being the iPhone’s celebratory 10th anniversary — suggests that a rumored premium model will be called the iPhone 8. This would follow from Apple’s history of revamping the iPhone’s form factor with each singular whole number generation. But a recent report from 9to5Mac suggests the rumor mill may have this one wrong. Instead of adding an ‘s’ to the iPhone 7 and 7 Plus’ successors, Apple may instead call them the iPhone 8 and iPhone 8 Plus, says this report, which cites insider information obtained from third-party iPhone case makers. If true, it means that Apple views these phones as sufficiently advanced to brand them as next-generational, not just iterative updates to the current models. iPhone Edition If the iPhone 7 and iPhone 7 Plus’ successors are in fact to be an iPhone 8 and iPhone 8 Plus, where does that leave the rumored premium edition iPhone? According to the same 9to5Mac report, Apple plans to name its 10th anniversary smartphone the iPhone Edition. This isn’t the first time someone’s suggested as much: back in March, Japanese blog Macotakara reported the same. iPhone X Another rumored possible name for Apple’s 10th anniversary iPhone is simply iPhone X, where the “X” stands for the Roman numeral, pronounced “ten” just as when you say “OS X.” Longtime Apple analyst and Loup Ventures managing partner Gene Munster believes Apple will choose this name, skipping ‘9’ to ring in the iPhone’s 10th anniversary. VentureBeat’s Evan Blass, who has a steady track record reporting on unreleased smartphones, also said he’s heard iPhone X as a possible name for Apple’s high-end iPhone. iPhone Pro Prominent Apple blogger John Gruber suspects that Apple could call the special edition iPhone the iPhone Pro or iPhone 8 Pro. If so, the title would fit with the nomenclature Apple uses for its other premium products, the iPad Pro and MacBook Pro. It would thus convey the notion that the company’s 10th anniversary iPhone is more for power users rather than mainstream smartphone shoppers.\", \"polarity\": \"neutral\", \"subjectivity\": \"unknown\", \"polarityConfidence\": 0.7881144285202026, \"subjectivityConfidence\": 0}, \"classifications\": [\"Mac Support\", \"Technology & Computing\"], \"sentimentImageUrl\": \"https://cdn2.iconfinder.com/data/icons/emoticons-17/24/emoticons-04-32.png\"}, {\"id\": 48, \"url\": \"http://mashable.com/2017/09/05/iphone-8-price-tier-rumors-/\", \"index\": 2, \"title\": \"Are you ready for a $1,200 iPhone?\", \"domain\": \"mashable.com\", \"summary\": \"The iPhone 8 (or Edition or X, take your pick) could weigh down your bank account.Image:  loris ravera/mashableBy Brett Williams2017-09-05 16:20:18 UTCApple is finally slated to reveal the highly-anticipated deluxe anniversary iPhone on Sept. 12, and you will want to buy it immediately — but the sticker price could wind up dampening your excitement for the phone's next-gen features. \\n\\nApple insider John Gruber suggested the deluxe new device would debut at the price point back in July, speculating that Apple could justify the cost by showcasing next-level tech that will be common in future iPhones in a premium device today. \\n\\nSpeculation over the price of the iPhone is nothing new for the rumor cycle, with reports flying about the extra costs for as long as there have been rumors about a new OLED screen. \\n\\n\", \"imageUrl\": \"https://i.amz.mshcdn.com/7qKyHPuCb5CTyhT93TDOkuCjg14=/1200x627/https%3A%2F%2Fblueprint-api-production.s3.amazonaws.com%2Fuploads%2Fstory%2Fthumbnail%2F55946%2F03898169-d697-4906-aee1-79068714610a.jpg\", \"sentiment\": {\"text\": \"Are you ready for a $1,200 iPhone?The iPhone 8 (or Edition or X, take your pick) could weigh down your bank account.Image:  loris ravera/mashableBy Brett Williams2017-09-05 16:20:18 UTCApple is finally slated to reveal the highly-anticipated deluxe anniversary iPhone on Sept. 12, and you will want to buy it immediately — but the sticker price could wind up dampening your excitement for the phone's next-gen features. Rumors claim the iPhone 8 (or Edition or X, depending on who you trust) will be much more expensive than any of its predecessors, pushing the starting cost up to at least the $1,000 mark. That means the top-of-the-line model will cost a whopping $1,200, for anyone who wants more than just the basic level of storage on their deluxe device. Leaker Benjamin Geskin tweeted out a pricing tier for the new iPhones, citing information from a friend who has a friend at Apple. The sourcing sounds sketchy, but Geskin is far from the first to suggest that the next iPhone will cost more than $1,000. Apple insider John Gruber suggested the deluxe new device would debut at the price point back in July, speculating that Apple could justify the cost by showcasing next-level tech that will be common in future iPhones in a premium device today. A New York Times report also backed the idea of a starting price \\\"around $999,\\\" for the iPhone, citing anonymous sources who had been briefed on the device. That's a much more reliable report than just the whispers of friend of a friend — but others aren't so convinced that Apple will ask such a high price for a phone.UBS analysts Steven Milunovich and Benjamim Wilson wrote in an investors note that they \\\"questioned the logic\\\" of Apple putting such a premium on an iPhone. They claim instead that the company will roll out the deluxe device at a $900 starting point for a 64GB model, with a 256GB version eclipsing the $1,000 mark. The analysts also noted that Apple typically takes some cues from its competitors, and with Samsung's latest offerings starting well under $1,000 — the new Galaxy Note 8 starts at $930 unlocked — there's little incentive for Apple to set the bar any higher.   None of these projections questioned the features expected in the deluxe iPhone, which include a new edge-to-edge OLED display, a nearly bezel-free screen with no home button, and a new sensor system for facial recognition. Speculation over the price of the iPhone is nothing new for the rumor cycle, with reports flying about the extra costs for as long as there have been rumors about a new OLED screen. Now that we're a week away from the big reveal, however, those projected costs are all the more pressing, since we're finally closer to getting a shot to put down the cash for one of our own.\", \"polarity\": \"neutral\", \"subjectivity\": \"unknown\", \"polarityConfidence\": 0.6192851066589355, \"subjectivityConfidence\": 0}, \"classifications\": [\"Technology & Computing\", \"Mac Support\"], \"sentimentImageUrl\": \"https://cdn2.iconfinder.com/data/icons/emoticons-17/24/emoticons-04-32.png\"}, {\"id\": 48, \"url\": \"https://www.usatoday.com/videos/tech/2017/09/05/new-apple-augmented-reality-apps-iphone/105293930/\", \"index\": 3, \"title\": \"New Apple augmented reality apps for iPhone\", \"domain\": \"usatoday.com\", \"summary\": \"\", \"imageUrl\": \"https://www.gannett-cdn.com/-mm-/2e56892f6a349ad47192b530425d443fb365e5e9/r=x1803&c=3200x1800/https/media.gannett-cdn.com/29906170001/29906170001_5565489722001_5565475210001-vs.jpg?pubId=29906170001\", \"sentiment\": {\"text\": \"Jefferson Graham previews new augmented reality apps for the iPhone, which mix animation with real life. #TalkingTech\", \"polarity\": \"neutral\", \"subjectivity\": \"objective\", \"polarityConfidence\": 0.6085818409919739, \"subjectivityConfidence\": 0.999972471517092}, \"classifications\": [\"Architects\", \"Animation\", \"Hobbies & Interests\", \"Technology & Computing\", \"Real Estate\", \"Screenwriting\"], \"sentimentImageUrl\": \"https://cdn2.iconfinder.com/data/icons/emoticons-17/24/emoticons-04-32.png\"}, {\"id\": 48, \"url\": \"https://www.fool.com/investing/2017/09/05/apple-incs-rumored-iphone-x-name-sounds-ridiculous.aspx\", \"index\": 4, \"title\": \"Apple Inc.'s Rumored \\\"iPhone X\\\" Name Sounds Ridiculous\", \"domain\": \"fool.com\", \"summary\": \"However, a report from iCulture (via BGR), claims the premium iPhone with OLED display won't be called the iPhone Edition, but will instead go by the name \\\"iPhone X.\\\"Image source: Apple. \\n\\nAn iPhone Edition in addition to those models doesn't diminish the value of the mainstream models; the name builds up a premium image for the more expensive device. \\n\\nHowever, if Apple goes with the \\\"iPhone Edition\\\" name, then it can release new models each year, and each year the latest premium smartphone can carry the name \\\"iPhone Edition.\\\" \\n\\n\", \"imageUrl\": \"https://g.foolcdn.com/image/?url=https%3A%2F%2Fg.foolcdn.com%2Feditorial%2Fimages%2F455688%2Fiphone7plus-lineup.jpg&h=630&w=1200&op=resize\", \"sentiment\": {\"text\": \"A story in 9to5Mac, citing sources at case makers who claim to have \\\"sources in Shenzhen who claim to have seen the new iPhone packaging,\\\" recently said that this year's new Apple (NASDAQ:AAPL) iPhone models could be called iPhone 8, iPhone 8 Plus, and iPhone Edition.However, a report from iCulture (via BGR), claims the premium iPhone with OLED display won't be called the iPhone Edition, but will instead go by the name \\\"iPhone X.\\\"Image source: Apple.The report explicitly says that the device's name is pronounced \\\"iPhone 10.\\\"This name comes from \\\"a reliable source\\\" at a \\\"worldwide operating telecom company.\\\" The report further goes on to explain that Apple held a summit in Cupertino, California, \\\"to discuss the launch of the new devices.\\\"Separately, noted leaker Evan Blass tweeted that he's \\\"also heard 'iPhone X.'\\\"iPhone X sounds sillyIf it were up to me -- and, unfortunately, it's clearly not -- the name \\\"iPhone X\\\" (rumored pronunciation and all) would easily lose out to the name \\\"iPhone Edition.\\\"After all, \\\"Edition\\\" is, as I've written previously, an established premium branding for Apple and would be appropriate for a premium device that's supposed to sit atop the standard models in the product stack.The name \\\"iPhone X\\\" as pronounced seems problematic. For one thing, Apple still needs to sell the iPhone 8 and iPhone 8 Plus as its current mainstream models. If Apple has an iPhone \\\"10\\\" available simultaneously with the iPhone 8, then the mainstream models will seem two generations behind the premium iPhone model.Although the premium iPhone will have some advantages over the mainstream versions, the mainstream phones are still going to be class-leading smartphones where it counts. An iPhone Edition in addition to those models doesn't diminish the value of the mainstream models; the name builds up a premium image for the more expensive device.An iPhone \\\"10\\\" alongside iPhone 8/iPhone 8 Plus is just plain weird and, frankly, the name sounds ridiculous.Looking to the futureIf Apple does a so-called iPhone X this year, then it'll probably want to release a successor to this device in the future. After all, the concept of building a no-holds-barred phone for customers willing to pay more is a good one that the company should exploit to its benefit in the years ahead.If Apple goes with the \\\"iPhone X\\\" name for such a model this year, what does it call next year's model? iPhone XI?That just wouldn't sound right.However, if Apple goes with the \\\"iPhone Edition\\\" name, then it can release new models each year, and each year the latest premium smartphone can carry the name \\\"iPhone Edition.\\\"It doesn't even have to be \\\"Edition\\\" -- it just needs to be some premium brand that has a relatively evergreen and premium-sounding name. While I don't think \\\"Pro\\\" would be appropriate (it's evergreen but not premium-sounding), Apple's marketing team can definitely cook up something better than \\\"X.\\\"                                                                                                Ashraf Eassa has no position in any of the stocks mentioned. The Motley Fool owns shares of and recommends Apple. The Motley Fool has a disclosure policy.\", \"polarity\": \"neutral\", \"subjectivity\": \"unknown\", \"polarityConfidence\": 0.8020519018173218, \"subjectivityConfidence\": 0}, \"classifications\": [\"Mac Support\", \"Technology & Computing\"], \"sentimentImageUrl\": \"https://cdn2.iconfinder.com/data/icons/emoticons-17/24/emoticons-04-32.png\"}, {\"id\": 48, \"url\": \"http://www.msn.com/en-us/money/other/tuesday-apple-rumors-iphone-8-may-start-at-dollar900/ar-AArlGkf\", \"index\": 5, \"title\": \"Tuesday Apple Rumors: iPhone 8 May Start at $900\", \"domain\": \"msn.com\", \"summary\": \"Leading the Apple Inc. (NASDAQ:AAPL) rumor mill today is another stab at guessing the price of the iPhone 8. \\n\\n© Provided by InvestorPlace Tuesday Apple Rumors: iPhone 8 May Start at $900 iPhone 8 Price: A recent rumor claims that the iPhone 8 won’t cost as much as expected, reports Business Insider. \\n\\nThe recent report claims that the tech company has already placed the order, but that the cameras won’t be for the upcoming 2017 iPhone line. \\n\\n\", \"imageUrl\": \"http://img-s-msn-com.akamaized.net/tenant/amp/entityid/AArlNmQ.img\", \"sentiment\": {\"text\": \"Leading the Apple Inc. (NASDAQ:AAPL) rumor mill today is another stab at guessing the price of the iPhone 8. Today, we’ll look at that and other Apple Rumors for Tuesday.© Provided by InvestorPlace Tuesday Apple Rumors: iPhone 8 May Start at $900 iPhone 8 Price: A recent rumor claims that the iPhone 8 won’t cost as much as expected, reports Business Insider. UBS analysts believe that Apple is planning to start the iPhone 8 off at $900 for the 64GB version. They claim that the company will also have a 256GB version of the smartphone that will cost $1000. These analysts claim that there will only be two versions of the iPhone 8, not three. Previous rumors claim that the iPhone 8 will start at $1000 and go up from there.iPhone 8 SIM Trays: Possible SIM trays for the iPhone 8 have leaked, AppleInsider notes. A few images of the SIM trays have shown up online. The colors of these trays may offer insight into the colors of the iPhone 8. This includes both a black and silver option. A third SIM tray appears to be copper in color. Previous leaks and rumors have said that the iPhone 8 will come in this new copper color.Cameras: Apple is reportedly ordering camera modules above 12MP, reports DigiTimes. The recent report claims that the tech company has already placed the order, but that the cameras won’t be for the upcoming 2017 iPhone line. Instead, it claims that AAPL is preparing for the next generation of its smartphones that will come out in 2018. The report claims that the camera modules are coming from Largan Precision in Taiwan.Check out more recent Apple Rumors or © Provided by InvestorPlace Subscribe to Apple Rumors Email Subscribe to Apple Rumors : RSSAs of this writing, William White did not hold a position in any of the aforementioned securities.\", \"polarity\": \"neutral\", \"subjectivity\": \"unknown\", \"polarityConfidence\": 0.8682097792625427, \"subjectivityConfidence\": 0}, \"classifications\": [\"Technology & Computing\", \"Computer Reviews\"], \"sentimentImageUrl\": \"https://cdn2.iconfinder.com/data/icons/emoticons-17/24/emoticons-04-32.png\"}, {\"id\": 48, \"url\": \"https://www.forbes.com/sites/ewanspence/2017/09/04/apple-iphone8-new-leak-rumor-price-expensive-secret/\", \"index\": 6, \"title\": \"New iPhone 8 Leak Reveals Apple's Stunning Shock\", \"domain\": \"forbes.com\", \"summary\": \"With the presumptive launch of the new iPhone just over a week away, almost everything is known about Tim Cook’s tenth-anniversary handset. \\n\\nApple PR                Tim Cook in the shadows at WWDC 2015            No doubt Apple will also be maintaining the profit margin of the iPhone 8 - generally accepted to be around thirty-five to forty percent - but consumers will be wondering what they will be getting when they pay the ‘Apple premium’. \\n\\nThe wireless charging system may be based on the Qi standard but it looks like Apple will lock the iPhone hardware to only accepting a charge from third-party devices that have been licensed by Cupertino. \\n\\n\", \"imageUrl\": \"https://thumbor.forbes.com/thumbor/600x315/smart/https://blogs-images.forbes.com/ewanspence/files/2017/08/OLM_iphone8_topbar2_square-500x500.jpg\", \"sentiment\": {\"text\": \"With the presumptive launch of the new iPhone just over a week away, almost everything is known about Tim Cook’s tenth-anniversary handset. It’s unlikely there’s going to be a genuine ‘one more thing’ to surprise the crowd, but Apple’s latest smartphone is still able to deliver a shock. All $1200 of a shock.         Oscar Luna Martinez                Artists Impression of the iPhone 8 (image: Oscar Luna Martinez)           While there are a number of different options for device configuration and pricing, the consensus is forming around three storage options and an eye-watering price. The latest data point in the pricing graph comes from Benjamin Geskin and backs up the widely held view of the iPhone’s price. The entry-level 64 GB model of the iPhone 8 is expected to come in at $999 plus tax, a mid-tier 256 GB model at $1099, and a top tier 512 GB of $1199. While that’s a new line-up for an iPhone it does match the iPad Pro choices. Apple loves repeating patterns, so this is another nod towards the iPhone 8 being the ‘Pro’ smartphone in the portfolio.    As always, Apple has maintained a $100 separation between the storage tiers, which leads to the curious situation of the first storage increase of 192 GB costing the same as the second increase of 256 GB. Presumably, Apple would not be so crass as to say 'buy another 192 GB, get a bonus 64 GB for free' but the thought is there.         Apple PR                Tim Cook in the shadows at WWDC 2015            No doubt Apple will also be maintaining the profit margin of the iPhone 8 - generally accepted to be around thirty-five to forty percent - but consumers will be wondering what they will be getting when they pay the ‘Apple premium’. Many of the new features that Tim Cook and his team will be introducing to the iOS ecosystem are common features on high-end Android devices. OLED screens, curved glass, dual lens cameras and wireless charging can all be found in handsets manufactured by the competition. And some of those features are going to be limited. The wireless charging system may be based on the Qi standard but it looks like Apple will lock the iPhone hardware to only accepting a charge from third-party devices that have been licensed by Cupertino. I’m expecting a lot of media-friendly cheering from the specially invited audience at The Steve Jobs Theatre when the iPhone 8 is unveiled on September 12th, along with talk of Apple taking existing technology and ’perfecting’ it. Does that justify the sticker shock of $1200 for a smartphone? For a few members of the geekerati, yes that is a price worth paying. For the general public, the iPhone 8 may look too expensive for what it offers. Now read about Tim Cook’s love for Augmented Reality and how it might be brought to the masses…\", \"polarity\": \"positive\", \"subjectivity\": \"unknown\", \"polarityConfidence\": 0.9941601753234864, \"subjectivityConfidence\": 0}, \"classifications\": [\"Mac Support\", \"Technology & Computing\"], \"sentimentImageUrl\": \"https://cdn2.iconfinder.com/data/icons/emoticons-17/24/emoticons-01-32.png\"}, {\"id\": 48, \"url\": \"http://fortune.com/2017/09/05/apple-iphone-8-samsung-lg-sony/\", \"index\": 7, \"title\": \"How Samsung, LG, and Sony Are Preparing for Apple's iPhone 8\", \"domain\": \"fortune.com\", \"summary\": \"Samsung’s new Note 8 is ready to take on the upcoming iPhones with an even bigger 6.3-inch screen that has narrow bezels so that the display covers more of the front of the phone. \\n\\nOn the flip side, Samsung caught up to last year’s iPhone 7 Plus by adding a second camera to the back of the new Note that adds an optical zoom and can create a blurred background effect. \\n\\nMost of the Android phones rely on Qualcomm’s central processors, but Apple has its own line of chips and leaked benchmark tests suggest the new A11 chip could be considerably faster than Qualcomm’s cutting edge Snapdragon 835. \\n\\n\", \"imageUrl\": \"https://fortunedotcom.files.wordpress.com/2017/08/gettyimages-837876886.jpg?w=720\", \"sentiment\": {\"text\": \"Apple will unveil its newest iPhones in one week, and possibly an upgraded smartwatch, as well. But the rest of the industry has been trying to anticipate Apple’s moves with new phones and watches of their own. And the competition is bringing more useful new features to consumers than ever. At the IFA trade show in Berlin last week, LG, Sony, and Lenovo’s Motorola introduced new phones. And after announcing its Galaxy Note 8 phone in New York two weeks ago, Samsung debuted several new smart wearables at IFA, as did Garmin. And Fitbit finally took the wraps off its long-awaited smartwatch on August 28. Still, none of those new products garnered as much attention as Apple will grab next week on September 12 when it shows off its latest and greatest mobile devices. Apple is expected to show three new iPhone models, two that are quite similar in appearance to the current iPhone 7 and 7 Plus, and one that is all-new, which analysts have dubbed the iPhone X, iPhone Edition, or possibly just the iPhone 8. A third generation of the Apple Watch is rumored to look like the current models, but includes an LTE modem to connect to wireless networks all on its own. Get Data Sheet, Fortune’s technology newsletter. Last year, Apple won the all important holiday shopping season, just narrowly beating out Samsung, according to IDC. Apple shipped 78.3 million phones versus Samsung’s 77.5 million. But that was in the middle of an ugly recall of Samsung’s Galaxy Note 7, which had a propensity to overheat and even explode due to a flawed battery design. At the same time, Apple failed to generate as much excitement as in the past by sticking with a similar exterior design for a third year in a row. This year, both companies are hoping to improve. Analysts are salivating that Apple’s new designs for 2017 will stimulate a massive upgrade cycle and get iPhone sales booming again. Apple’s share price has already climbed 41% this year in anticipation and touched an all-time high of just below $165 last week. Samsung created new safety procedures to ensure no repeat of last year’s battery debacle. Samsung’s new Note 8 is ready to take on the upcoming iPhones with an even bigger 6.3-inch screen that has narrow bezels so that the display covers more of the front of the phone. None of Apple’s models are rumored to have a screen bigger than 5.8 inches, but they will match the Note’s bright, sharp display by using OLED technology for the first time. On the flip side, Samsung caught up to last year’s iPhone 7 Plus by adding a second camera to the back of the new Note that adds an optical zoom and can create a blurred background effect. Rumors suggest Apple is tweaking the dual camera design on this year’s models to improve image stabilization. Lots of Phones A lot of other phones are going to OLED displays with narrow bezels, including the new LG V30. The V30 also has dual cameras, but with some of the best specs every put in a phone, including the widest aperture too take in more light in darker photo opportunities and a video color mode that will help pro videographers process footage for the best appearance. Sony’s new XZ1 will be one of the first phones to compete with the latest Android software against the new iPhones and the upgraded iOS 11 operating system. The 5.2-inch phone has am industry-leading 19 megapixel camera. Motorola decided to bring out new phones that can compete on a different feature: price. With starting iPhones expected at the usual $650 and the all-new top-end model going for $1,000 or more, some competitors decided to bring out much cheaper models to fill the void with penny pinching consumers. Moto’s X4 starts at 400 euros, or about $477. But Sony sne and Motorola have struggled to match Apple and Samsung in advertising, distribution, and consumer excitement in the past, and that doesn’t seem much likely to change this year. There are still several areas where Apple is likely to trump all of the Android contenders. Most of the Android phones rely on Qualcomm’s central processors, but Apple has its own line of chips and leaked benchmark tests suggest the new A11 chip could be considerably faster than Qualcomm’s cutting edge Snapdragon 835. And despite Google’s best efforts, none can match the iTunes ecosystem of the newest cutting edge mobile apps. In the smartwatch market, Apple is rumored to be adding wireless capability to make its device less dependent on a nearby iPhone for connectivity. Several competitors that rely on Google’s googl Android Wear software tried that trick last year, but without much success. The key for Apple aapl may be maintaining solid battery life with the additional power draw from a wireless modem. LG made its LTE-capable smartwatch ticker and heavier to include a bigger battery and still lasted barely a day.\", \"polarity\": \"positive\", \"subjectivity\": \"unknown\", \"polarityConfidence\": 0.9913813471794128, \"subjectivityConfidence\": 0}, \"classifications\": [\"Technology & Computing\", \"Cell Phones\"], \"sentimentImageUrl\": \"https://cdn2.iconfinder.com/data/icons/emoticons-17/24/emoticons-01-32.png\"}]";
        }

        try {
            searchResultsService.saveResults(resultsId, json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String html = mailComposer.ComposeSearchDoneMail("MyBuzz Search Completed", resultsId, user.getEmail());
            mailSender.sendMail("mybuzzworkshop@gmail.com", userService.getAdminEmail(), queryValueParam + " Search Completed", html);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHaveToAppearParam(List<String> haveToAppearParam) {
        this.haveToAppear = haveToAppearParam;
    }

    public void setCantAppearParam(List<String> cantAppearParam) {
        this.cantAppear = cantAppearParam;
    }

    public void setQueryValueParam(String queryValueParam) {
        this.queryValueParam = queryValueParam;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setResultsId(int resultsId) { this.resultsId = resultsId; }

    //To Test server without really searching:
    private List<RelevantNews> GetMockResults() {
        RelevantNews firstRes;

        Summarize summ = new Summarize();
        summ.setSentences(new String[]{
                "Every time Apple's developer conference rolls around we get a smattering of changes to the App Store Review guidelines.",
                "This corpus of rules can be, in turns, opaque and explicit, and has caused a decent amount of consternation over the years for developers as they try to read into how Apple might interpret one rule or another."
        });
        Sentiment sentiment = new Sentiment();
        sentiment.setPolarity("Positive");
        sentiment.setPolarityConfidence(0.856248);
        URL url = null;
        try {
            url = new URL("https://techcrunch.com/2017/06/21/apple-goes-after-clones-and-spam-on-the-app-store/");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        TaxonomyClassifications classifications = new TaxonomyClassifications();
        Article article = new Article();
        article.setTitle("Apple goes after clones and spam on the App Store");
        article.setImages(new String[]{"https://tctechcrunch2011.files.wordpress.com/2017/06/apple-liveblog0706.jpg?w=764&h=400&crop=1"});
        firstRes = new RelevantNews(summ, sentiment, url, classifications,article);

        List<RelevantNews> results = new ArrayList<>();
        results.add(firstRes);
        for (int i=0;i<9;i++){
            results.add(GenerateMockResults());
        }

        return results;
    }

    private RelevantNews GenerateMockResults() {
        LoremIpsum loremIpsum = new LoremIpsum();
        Random rand = new Random();


        Summarize summ = new Summarize();
        summ.setSentences(new String[]{
                loremIpsum.getWords(rand.nextInt(20) + 10),
                loremIpsum.getWords(rand.nextInt(20) + 10)
        });
        Sentiment sentiment = new Sentiment();
        sentiment.setPolarity(rand.nextInt(2) == 1 ?"Positive": "Negative");
        sentiment.setPolarityConfidence(rand.nextFloat());
        URL url = null;
        try {
            url = new URL("https://techcrunch.com/2017/06/21/apple-goes-after-clones-and-spam-on-the-app-store/");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        TaxonomyClassifications classifications = new TaxonomyClassifications();
        Article article = new Article();
        article.setTitle(loremIpsum.getWords(8));
        article.setImages(new String[]{"http://loremflickr.com/320/240/business"});
        return new RelevantNews(summ, sentiment, url, classifications,article);

    }
}
