package com.cs639.pacexchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.format.DateFormat;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ChatActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    //private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout activity_chat;

    //EmojiconEditText emojiconEditText;
    ImageView emojiButton,submitButton;
    //EmojIconActions emojIconActions;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_log_out)
        {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(task -> {
                Snackbar.make(activity_chat,"You have been signed out.", Snackbar.LENGTH_SHORT).show();
                finish();
            });
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                Snackbar.make(activity_chat,"Successfully signed in.Welcome!", Snackbar.LENGTH_SHORT).show();
                displayChatMessage();
            }
            else{
                Snackbar.make(activity_chat,"We couldn't sign you in.Please try again later", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

//        activity_chat = (RelativeLayout)findViewById(R.id.activity_chat);

        //Add Emoji
        emojiButton = (ImageView)findViewById(R.id.emoji_button);
        submitButton = (ImageView)findViewById(R.id.submit_button);
//        emojiconEditText = (EmojiconEditText)findViewById(R.id.emojicon_edit_text);
//        emojIconActions = new EmojIconActions(getApplicationContext(),activity_chat,emojiButton,emojiconEditText);
//        emojIconActions.ShowEmojicon();

        submitButton.setOnClickListener(view -> {
//            FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(emojiconEditText.getText().toString(),
//                    FirebaseAuth.getInstance().getCurrentUser().getEmail()));
//            emojiconEditText.setText("");
//            emojiconEditText.requestFocus();
        });

        //Check if not sign-in then navigate Signin page
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(activity_chat,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
            //Load content
            displayChatMessage();
        }
    }

    private void displayChatMessage() {

        ListView listOfMessage = (ListView)findViewById(R.id.list_of_message);
//        adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.activity_chat,FirebaseDatabase.getInstance().getReference())
//        {
//            @Override
//            protected void populateView(View v, ChatMessage model, int position) {
//
//                //Get references to the views of list_item.xml
//                TextView messageText, messageUser, messageTime;
////                messageText = (BubbleTextView) v.findViewById(R.id.message_text);
//                messageUser =  v.findViewById(R.id.message_user);
//                messageTime =  v.findViewById(R.id.message_time);
//
////                messageText.setText(model.getMessageText());
//                messageUser.setText(model.getMessageUser());
//                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
//
//            }
//        };
//        listOfMessage.setAdapter(adapter);
    }
}