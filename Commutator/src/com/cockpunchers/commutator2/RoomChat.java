package com.cockpunchers.commutator2;

import java.util.Calendar;
import java.util.Date;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.quickblox.module.chat.QBChatRoom;
import com.quickblox.module.chat.QBChatService;
import com.quickblox.module.chat.listeners.ChatMessageListener;
import com.quickblox.module.chat.listeners.RoomListener;
import com.quickblox.module.chat.utils.QBChatUtils;
import com.quickblox.module.users.model.QBUser;

public class RoomChat implements Chat, RoomListener, ChatMessageListener {

	// public static final String EXTRA_ROOM_NAME = "name";
	// public static final String EXTRA_ROOM_ACTION = "CREATE";
	private static final String TAG = RoomChat.class.getSimpleName();
	private RideChatFragment chatActivity;
	private static QBChatRoom chatRoom;
	// String filename = "created_or_not";
	// private String ObjId;
	// private int join;
	String objectId;
	public static final String PREFS_NAME = "MyObj";
	public static final String PREF_NAME = "MyO";
	int i = -1;

	SharedPreferences preferences;
	public void onResume() throws XMPPException{
//		QBChatService.getInstance().destroyRoom(chatRoom);
		release();

	}

	public RoomChat(RideChatFragment chatActivity) {
		this.chatActivity = chatActivity;
		// ObjId=RidesCreatedFragment.objectId;
		// String chatRoomName = ((Activity)
		// chatActivity).getIntent().getStringExtra("yo my friend");
		// RoomAction action = (RoomAction)
		// chatActivity.getIntent().getSerializableExtra();

		preferences = chatActivity.getActivity().getSharedPreferences(
				"RidePreferences", Context.MODE_PRIVATE);
		i = preferences.getInt("iValue", 0);

		// FileInputStream fis;
		// try {
		// fis = this.chatActivity.getActivity().openFileInput(filename);
		// i = fis.read();
		// fis.close();
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		if (i == 1) {
			SharedPreferences settings = this.chatActivity.getActivity()
					.getSharedPreferences(PREFS_NAME, 0);
			objectId = settings.getString("Obj", "aint no");
		} else {
			SharedPreferences settings = this.chatActivity.getActivity()
					.getSharedPreferences(PREF_NAME, 0);
			objectId = settings.getString("Object", "aint no");
		}

		switch (i) {
		case 1:
			create(""); // creating rooms
			break;
		case 0:
			join(((App) chatActivity.getActivity().getApplicationContext())// joining
																			// rooms
					.getCurrentRoom());
			break;
		}
	}

	@Override
	public void sendMessage(String message) throws XMPPException {
		if (chatRoom != null) {
			chatRoom.sendMessage(message);
		} else {
			// Toast.makeText(chatActivity, "Join unsuccessful",
			// Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void release() throws XMPPException {
		if (chatRoom != null) {
			QBChatService.getInstance().leaveRoom(chatRoom);
			chatRoom.removeMessageListener(this);
		}
	}
	 
	 static void ondelete() {
		 QBChatService.getInstance().stopAutoSendPresence();
		 Log.e("chat room del", "yo mava here" + chatRoom + " room destroyed : "
		 + QBChatService.getInstance().destroyRoom(chatRoom));
		 }

	@Override
	public void onCreatedRoom(QBChatRoom room) {
		Log.d(TAG, "room was created");
		chatRoom = room;
		chatRoom.addMessageListener(this);
	}

	@Override
	public void onJoinedRoom(QBChatRoom room) {
		Log.d(TAG, "joined to room");
		chatRoom = room;
		chatRoom.addMessageListener(this);
	}

	@Override
	public void onError(String msg) {
		Log.d(TAG, "error joining to room");
	}

	@Override
	public void processMessage(Message message) {
		
		Date time = QBChatUtils.parseTime(message);
		if (time == null) {
			time = Calendar.getInstance().getTime();
		}
		// Show message
		String sender = QBChatUtils.parseRoomOccupant(message.getFrom());
		QBUser qbUser = ((App) (chatActivity.getActivity().getApplicationContext())).getQbUser();
		Log.e("RoomChat", "Sender is : " + sender + " User is : " + qbUser);
		if(sender!=null)
		{
		if (sender.equals(qbUser.getFullName())
				|| sender.equals(qbUser.getId().toString())) {
			chatActivity.showMessage(new ChatMessage(message.getBody(), "me",
					time, false));
		} else {
			chatActivity.showMessage(new ChatMessage(message.getBody(), sender,
					time, true));
		}
		}
	}

	@Override
	public boolean accept(Message.Type messageType) {
		switch (messageType) {
		case groupchat:
			return true;
		default:
			return false;
		}
	}

	public void create(String roomName) {
		// Creates open & persistent room
		preferences = chatActivity.getActivity().getSharedPreferences(
				"MyRideID", Context.MODE_PRIVATE);
		QBChatService.getInstance().createRoom(
				preferences.getString("RID", ""), false, true, this);
	}

	public void join(QBChatRoom room) {
		preferences = chatActivity.getActivity().getSharedPreferences(
				"MyRideID", Context.MODE_PRIVATE);
		QBChatService.getInstance().joinRoom(preferences.getString("RID", ""),
				this);
	}

	public static enum RoomAction {
		CREATE, JOIN
	}
}
