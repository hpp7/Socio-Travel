package com.cockpunchers.commutator2;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.XMPPException;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseUser;
import com.quickblox.core.QBCallback;
import com.quickblox.core.QBCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.core.result.Result;
import com.quickblox.module.auth.QBAuth;
import com.quickblox.module.chat.QBChatService;
import com.quickblox.module.chat.listeners.SessionListener;
import com.quickblox.module.chat.smack.SmackAndroid;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;
import com.quickblox.module.users.result.QBUserResult;

public class RideChatFragment extends Fragment implements QBCallback {
	public static final String EXTRA_MODE = "GROUP";
	private static final String TAG = RideChatFragment.class.getSimpleName();
	private EditText messageEditText;

	private Chat chat;
	private ChatAdapter adapter;
	private ListView messagesContainer;
	private QBUser user;
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.activity_main_chat, container,
				false);
		initViews(rootView);
		return rootView;
	}

	@Override
	public void onResume(){
		 onDestroy();
	    // put your code here...

	}

	private void initViews(View v) {
		messagesContainer = (ListView) v.findViewById(R.id.messagesContainer);
		messageEditText = (EditText) v.findViewById(R.id.messageEdit);
		Button sendButton = (Button) v.findViewById(R.id.chatSendButton);
		TextView meLabel = (TextView) v.findViewById(R.id.meLabel);
		TextView companionLabel = (TextView) v
				.findViewById(R.id.companionLabel);
		RelativeLayout container = (RelativeLayout) v
				.findViewById(R.id.container);

		adapter = new ChatAdapter(getActivity(), new ArrayList<ChatMessage>());
		messagesContainer.setAdapter(adapter);

		chat = new RoomChat(this);
		container.removeView(meLabel);
		container.removeView(companionLabel);

		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String lastMsg = messageEditText.getText().toString();

				StringBuilder builder = new StringBuilder(ParseUser
						.getCurrentUser().getString("Name") + ":\n");
				builder.append(lastMsg);
				if (TextUtils.isEmpty(lastMsg)) {
					return;
				}

				messageEditText.setText("");
				try {
					chat.sendMessage(builder.toString());
				} catch (XMPPException e) {
					Log.e(TAG, "failed to send a message", e);
				}

				// if (mode == Mode.SINGLE) {
				// showMessage(new ChatMessage(lastMsg, Calendar.getInstance()
				// .getTime(), false));
			}
			// }
		});
	}

	public void showMessage(ChatMessage message) {
		// saveMessageToHistory(message);
		adapter.add(message);
		adapter.notifyDataSetChanged();
		scrollDown();
	}

	public void showMessage(List<ChatMessage> messages) {
		adapter.add(messages);
		adapter.notifyDataSetChanged();
		scrollDown();
	}

	private void scrollDown() {
		messagesContainer.setSelection(messagesContainer.getCount() - 1);
	}

	public void onBackPressed() {
		// super.onBackPressed();
		// Intent intent = new Intent();
		// setResult(RESULT_CANCELED, intent);
		// finish();
	}

	@Override
	public void onComplete(Result result) {
		if (result.isSuccess()) {
			((App) getActivity().getApplication()).setQbUser(user);
			QBChatService.getInstance().loginWithUser(user,
					new SessionListener() {
						@Override
						public void onLoginSuccess() {
							// if (progressDialog != null) {
							// progressDialog.dismiss();
							// }
							Log.i(TAG, "success when login");

							Intent intent = new Intent();
							getActivity().setResult(getActivity().RESULT_OK,
									intent);
							// getActivity().finish();
						}

						@Override
						public void onLoginError() {
							Log.i(TAG, "error when login");
						}

						@Override
						public void onDisconnect() {
							Log.i(TAG, "disconnect when login");
						}

						@Override
						public void onDisconnectOnError(Exception exc) {
							Log.i(TAG, "disconnect error when login");
						}
					});
		} else {
			AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
			dialog.setMessage(
					"Error(s) occurred. Look into DDMS log for details, "
							+ "please. Errors: " + result.getErrors()).create()
					.show();
		}
	}

	@Override
	public void onComplete(Result arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

}

// public static enum Mode {
// SINGLE, GROUP
// }

