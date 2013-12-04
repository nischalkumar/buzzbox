package com.elevenestates.buzzbox;

import java.io.File;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.ibb.provider.CloseIQProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.DataPacketProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.OpenIQProvider;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.packet.AttentionExtension;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.Nick;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.HeaderProvider;
import org.jivesoftware.smackx.provider.HeadersProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.pubsub.provider.AffiliationProvider;
import org.jivesoftware.smackx.pubsub.provider.AffiliationsProvider;
import org.jivesoftware.smackx.pubsub.provider.ConfigEventProvider;
import org.jivesoftware.smackx.pubsub.provider.EventProvider;
import org.jivesoftware.smackx.pubsub.provider.FormNodeProvider;
import org.jivesoftware.smackx.pubsub.provider.ItemProvider;
import org.jivesoftware.smackx.pubsub.provider.ItemsProvider;
import org.jivesoftware.smackx.pubsub.provider.PubSubProvider;
import org.jivesoftware.smackx.pubsub.provider.RetractEventProvider;
import org.jivesoftware.smackx.pubsub.provider.SimpleNodeProvider;
import org.jivesoftware.smackx.pubsub.provider.SubscriptionProvider;
import org.jivesoftware.smackx.pubsub.provider.SubscriptionsProvider;
import org.jivesoftware.smackx.search.UserSearch;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	public static final String HOST = "talk.google.com";
	public static final int PORT = 5222;
	public static final String SERVICE = "gmail.com";
	public static Chat newChat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		ConnectionConfiguration connConfig = new ConnectionConfiguration(HOST,PORT,SERVICE);
		//connConfig.setTruststoreType("BKS");
		XMPPConnection connection = new XMPPConnection(connConfig);
		//XMPPConnection connection = new XMPPConnection("gmail.com");
		 
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//while(connection==null)
		//{
		try {
			  //Connect to the server
			connection = new XMPPConnection(connConfig);
			  connection.connect();
			  //ping("talk.google.com");
			  Log.d("connection","connection successfull");
			  connection.login("xxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxx");
			// Set the status to available
	          Presence presence = new Presence(Presence.Type.available);
	          connection.sendPacket(presence);
	          //xmppClient.setConnection(connection);
			  Log.d("connection","Login successfull");
			} catch (XMPPException ex) {
			  connection = null;
			  ex.printStackTrace();
			  Log.d("connection","connection fail");
			  //Unable to connect to server
			}
		if(connection!=null)
		{
			configureProviderManager();
			FileTransferManager manager = new FileTransferManager(connection);
			OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer("nischal@jabber.ccc.de/jabber.ccc.de");
			File file = new File("/sdcard/DCIM/Camera/1385869353956.jpg");
			try {
			   transfer.sendFile(file, "test_file");
			} catch (XMPPException e) {
			   e.printStackTrace();
			}
			while(!transfer.isDone()) {
				   if(transfer.getStatus().equals(Status.error)) {
				      System.out.println("ERROR!!! " + transfer.getError());
				   } else if (transfer.getStatus().equals(Status.cancelled)
				                    || transfer.getStatus().equals(Status.refused)) {
				      System.out.println("Cancelled!!! " + transfer.getError());
				   }
				   try {
				      Thread.sleep(1000L);
				   } catch (InterruptedException e) {
				      e.printStackTrace();
				   }
				}
				if(transfer.getStatus().equals(Status.refused))
						 System.out.println("refused  " + transfer.getError());
				else if( transfer.getStatus().equals(Status.error))
					 System.out.println(" error " + transfer.getError());
				else if(transfer.getStatus().equals(Status.cancelled))
				   System.out.println(" cancelled  " + transfer.getError());
				else
				   System.out.println("Success");
			
		}
		//}
		
		/*
		Message msg = new Message("nischal1251.11@bitmesra.ac.in", Message.Type.chat);  
        msg.setBody("mike testing ok");
        if (connection != null) {
          connection.sendPacket(msg);
         
        }
       	/*
		Cursor c = getBaseContext().getContentResolver().query(Data.CONTENT_URI,
	            new String[] {Data._ID, Data.DISPLAY_NAME,Phone.NUMBER, Data.CONTACT_ID,Phone.TYPE, Phone.LABEL},
	                          Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'",
	                          null, 
	                          Data.DISPLAY_NAME);
	                          
	    */
		/*
		Intent myIntent = new Intent( getBaseContext(), Contact.class);
        startActivity(myIntent);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		*/
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	public void configureProviderManager() {
	    ProviderManager pm = ProviderManager.getInstance();

	    // The order is the same as in the smack.providers file

	    //  Private Data Storage
	    pm.addIQProvider("query","jabber:iq:private", new PrivateDataManager.PrivateDataIQProvider());
	    //  Time
	    try {
	        pm.addIQProvider("query","jabber:iq:time", Class.forName("org.jivesoftware.smackx.packet.Time"));
	    } catch (ClassNotFoundException e) {
	        System.err.println("Can't load class for org.jivesoftware.smackx.packet.Time");
	    }

	    //  Roster Exchange
	    pm.addExtensionProvider("x","jabber:x:roster", new RosterExchangeProvider());
	    //  Message Events
	    pm.addExtensionProvider("x","jabber:x:event", new MessageEventProvider());
	    //  Chat State
	    pm.addExtensionProvider("active","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
	    pm.addExtensionProvider("composing","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
	    pm.addExtensionProvider("paused","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
	    pm.addExtensionProvider("inactive","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
	    pm.addExtensionProvider("gone","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());

	    //  XHTML
	    pm.addExtensionProvider("html","http://jabber.org/protocol/xhtml-im", new XHTMLExtensionProvider());

	    //  Group Chat Invitations
	    pm.addExtensionProvider("x","jabber:x:conference", new GroupChatInvitation.Provider());
	    //  Service Discovery # Items
	    pm.addIQProvider("query","http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());
	    //  Service Discovery # Info
	    pm.addIQProvider("query","http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());
	    //  Data Forms
	    pm.addExtensionProvider("x","jabber:x:data", new DataFormProvider());
	    //  MUC User
	    pm.addExtensionProvider("x","http://jabber.org/protocol/muc#user", new MUCUserProvider());
	    //  MUC Admin
	    pm.addIQProvider("query","http://jabber.org/protocol/muc#admin", new MUCAdminProvider());
	    //  MUC Owner
	    pm.addIQProvider("query","http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());
	    //  Delayed Delivery
	    pm.addExtensionProvider("x","jabber:x:delay", new DelayInformationProvider());
	    pm.addExtensionProvider("delay", "urn:xmpp:delay", new DelayInformationProvider());
	    //  Version
	    try {
	        pm.addIQProvider("query","jabber:iq:version", Class.forName("org.jivesoftware.smackx.packet.Version"));
	    } catch (ClassNotFoundException e) {
	        System.err.println("Can't load class for org.jivesoftware.smackx.packet.Version");
	    }
	    //  VCard
	    pm.addIQProvider("vCard","vcard-temp", new VCardProvider());
	    //  Offline Message Requests
	    pm.addIQProvider("offline","http://jabber.org/protocol/offline", new OfflineMessageRequest.Provider());
	    //  Offline Message Indicator
	    pm.addExtensionProvider("offline","http://jabber.org/protocol/offline", new OfflineMessageInfo.Provider());
	    //  Last Activity
	    pm.addIQProvider("query","jabber:iq:last", new LastActivity.Provider());
	    //  User Search
	    pm.addIQProvider("query","jabber:iq:search", new UserSearch.Provider());
	    //  SharedGroupsInfo
	    pm.addIQProvider("sharedgroup","http://www.jivesoftware.org/protocol/sharedgroup", new SharedGroupsInfo.Provider());

	    //  JEP-33: Extended Stanza Addressing
	    pm.addExtensionProvider("addresses","http://jabber.org/protocol/address", new MultipleAddressesProvider());

	    //   FileTransfer
	    pm.addIQProvider("si","http://jabber.org/protocol/si", new StreamInitiationProvider());
	    pm.addIQProvider("query","http://jabber.org/protocol/bytestreams", new BytestreamsProvider());
	    pm.addIQProvider("open","http://jabber.org/protocol/ibb", new OpenIQProvider());
	    pm.addIQProvider("data","http://jabber.org/protocol/ibb", new DataPacketProvider());
	    pm.addIQProvider("close","http://jabber.org/protocol/ibb", new CloseIQProvider());
	    pm.addExtensionProvider("data","http://jabber.org/protocol/ibb", new DataPacketProvider());

	    //  Privacy
	    pm.addIQProvider("query","jabber:iq:privacy", new PrivacyProvider());

	    // SHIM
	    pm.addExtensionProvider("headers", "http://jabber.org/protocol/shim", new HeadersProvider());
	    pm.addExtensionProvider("header", "http://jabber.org/protocol/shim", new HeaderProvider());

	    // PubSub
	    pm.addIQProvider("pubsub", "http://jabber.org/protocol/pubsub", new PubSubProvider());
	    pm.addExtensionProvider("create", "http://jabber.org/protocol/pubsub", new SimpleNodeProvider());
	    pm.addExtensionProvider("items", "http://jabber.org/protocol/pubsub", new ItemsProvider());
	    pm.addExtensionProvider("item", "http://jabber.org/protocol/pubsub", new ItemProvider());
	    pm.addExtensionProvider("subscriptions", "http://jabber.org/protocol/pubsub", new SubscriptionsProvider());
	    pm.addExtensionProvider("subscription", "http://jabber.org/protocol/pubsub", new SubscriptionProvider());
	    pm.addExtensionProvider("affiliations", "http://jabber.org/protocol/pubsub", new AffiliationsProvider());
	    pm.addExtensionProvider("affiliation", "http://jabber.org/protocol/pubsub", new AffiliationProvider());
	    pm.addExtensionProvider("options", "http://jabber.org/protocol/pubsub", new FormNodeProvider());
	    // PubSub owner
	    pm.addIQProvider("pubsub", "http://jabber.org/protocol/pubsub#owner", new PubSubProvider());
	    pm.addExtensionProvider("configure", "http://jabber.org/protocol/pubsub#owner", new FormNodeProvider());
	    pm.addExtensionProvider("default", "http://jabber.org/protocol/pubsub#owner", new FormNodeProvider());
	    // PubSub event
	    pm.addExtensionProvider("event", "http://jabber.org/protocol/pubsub#event", new EventProvider());
	    pm.addExtensionProvider("configuration", "http://jabber.org/protocol/pubsub#event", new ConfigEventProvider());
	    pm.addExtensionProvider("delete", "http://jabber.org/protocol/pubsub#event", new SimpleNodeProvider());
	    pm.addExtensionProvider("options", "http://jabber.org/protocol/pubsub#event", new FormNodeProvider());
	    pm.addExtensionProvider("items", "http://jabber.org/protocol/pubsub#event", new ItemsProvider());
	    pm.addExtensionProvider("item", "http://jabber.org/protocol/pubsub#event", new ItemProvider());
	    pm.addExtensionProvider("retract", "http://jabber.org/protocol/pubsub#event", new RetractEventProvider());
	    pm.addExtensionProvider("purge", "http://jabber.org/protocol/pubsub#event", new SimpleNodeProvider());

	    // Nick Exchange
	    pm.addExtensionProvider("nick", "http://jabber.org/protocol/nick", new Nick.Provider());

	    // Attention
	    pm.addExtensionProvider("attention", "urn:xmpp:attention:0", new AttentionExtension.Provider());

	}
	
	public void configure(ProviderManager pm) {

	    // Private Data Storage
	    pm.addIQProvider("query", "jabber:iq:private",
	            new PrivateDataManager.PrivateDataIQProvider());

	    // Time
	    try {
	        pm.addIQProvider("query", "jabber:iq:time",
	                    Class.forName("org.jivesoftware.smackx.packet.Time"));
	    } catch (ClassNotFoundException e) {
	        Log.w("TestClient",
	                "Can't load class for org.jivesoftware.smackx.packet.Time");
	    }

	    // Roster Exchange
	    pm.addExtensionProvider("x", "jabber:x:roster",
	            new RosterExchangeProvider());

	    // Message Events
	    pm.addExtensionProvider("x", "jabber:x:event",
	            new MessageEventProvider());

	    // Chat State
	    pm.addExtensionProvider("active",
	            "http://jabber.org/protocol/chatstates",
	            new ChatStateExtension.Provider());
	    pm.addExtensionProvider("composing",
	            "http://jabber.org/protocol/chatstates",
	            new ChatStateExtension.Provider());
	    pm.addExtensionProvider("paused",
	            "http://jabber.org/protocol/chatstates",
	            new ChatStateExtension.Provider());
	    pm.addExtensionProvider("inactive",
	            "http://jabber.org/protocol/chatstates",
	            new ChatStateExtension.Provider());
	    pm.addExtensionProvider("gone",
	            "http://jabber.org/protocol/chatstates",
	            new ChatStateExtension.Provider());

	    // XHTML
	    pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
	            new XHTMLExtensionProvider());

	    // Group Chat Invitations
	    pm.addExtensionProvider("x", "jabber:x:conference",
	            new GroupChatInvitation.Provider());

	    // Service Discovery # Items
	    pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
	            new DiscoverItemsProvider());

	    // Service Discovery # Info
	    pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
	            new DiscoverInfoProvider());

	    // Data Forms
	    pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

	    // MUC User
	    pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
	            new MUCUserProvider());

	    // MUC Admin
	    pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
	            new MUCAdminProvider());

	    // MUC Owner
	    pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
	            new MUCOwnerProvider());

	    // Delayed Delivery
	    pm.addExtensionProvider("x", "jabber:x:delay",
	            new DelayInformationProvider());

	    // Version
	    try {
	        pm.addIQProvider("query", "jabber:iq:version",
	                Class.forName("org.jivesoftware.smackx.packet.Version"));
	    } catch (ClassNotFoundException e) {
	        // Not sure what's happening here.
	    }

	    // VCard
	    pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

	    // Offline Message Requests
	    pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
	            new OfflineMessageRequest.Provider());

	    // Offline Message Indicator
	    pm.addExtensionProvider("offline",
	            "http://jabber.org/protocol/offline",
	            new OfflineMessageInfo.Provider());

	    // Last Activity
	    pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());

	    // User Search
	    pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());

	    // SharedGroupsInfo
	    pm.addIQProvider("sharedgroup",
	            "http://www.jivesoftware.org/protocol/sharedgroup",
	            new SharedGroupsInfo.Provider());

	    // JEP-33: Extended Stanza Addressing
	    pm.addExtensionProvider("addresses",
	            "http://jabber.org/protocol/address",
	            new MultipleAddressesProvider());

	    // FileTransfer
	    pm.addIQProvider("si", "http://jabber.org/protocol/si",
	            new StreamInitiationProvider());

	    pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
	            new BytestreamsProvider());

	    // Privacy
	    pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
	    pm.addIQProvider("command", "http://jabber.org/protocol/commands",
	            new AdHocCommandDataProvider());
	    pm.addExtensionProvider("malformed-action",
	            "http://jabber.org/protocol/commands",
	            new AdHocCommandDataProvider.MalformedActionError());
	    pm.addExtensionProvider("bad-locale",
	            "http://jabber.org/protocol/commands",
	            new AdHocCommandDataProvider.BadLocaleError());
	    pm.addExtensionProvider("bad-payload",
	            "http://jabber.org/protocol/commands",
	            new AdHocCommandDataProvider.BadPayloadError());
	    pm.addExtensionProvider("bad-sessionid",
	            "http://jabber.org/protocol/commands",
	            new AdHocCommandDataProvider.BadSessionIDError());
	    pm.addExtensionProvider("session-expired",
	            "http://jabber.org/protocol/commands",
	            new AdHocCommandDataProvider.SessionExpiredError());
	}
}
