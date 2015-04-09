package com.example.translation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static String INPUT_EMPTY = "You have't input anything.";
	private static final String MESSAGE = "message";
	private static final String OUTPUT = "output";
	private static final String INPUTTEXT = "inputText";
	private static final String OUTPUTTEXT = "outputText";
	
	private List<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();

	private EditText textMessage;
	private ListView listView;
	private Button sendBtn;
	TranslationDataSource dataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dataSource = new TranslationDataSource(this);
		dataSource.open();
		
		textMessage = (EditText) this.findViewById(R.id.chatInput);
		listView = (ListView) this.findViewById(R.id.messages);
		sendBtn = (Button) this.findViewById(R.id.sendBtn);
		sendBtn.setEnabled(false);
		textMessage.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				if(isNullOrEmpty(arg0.toString())){
					sendBtn.setEnabled(false);
				}else{
					sendBtn.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			
		});
		
        if(this.getIntent() != null && this.getIntent().getExtras() != null){
	        String incomingText = this.getIntent().getExtras().getString(Intent.EXTRA_TEXT);
	        if (!isNullOrEmpty(incomingText)){
	        	textMessage.setText(incomingText);
	        	sendBtn.performClick();
	        }
        }

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onClick_Send(View view) throws Exception {
		String inputText = textMessage.getText().toString();
		textMessage.getText().clear();
		if (isNullOrEmpty(inputText)) {
			Toast.makeText(MainActivity.this, INPUT_EMPTY, Toast.LENGTH_LONG)
					.show();
			return;
		}

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put(INPUTTEXT, inputText);
		messages.add(inputMap);
		String existingRecord = dataSource.queryTranslationRecord(inputText);
		Map<String, Object> outputMap = new HashMap<String, Object>();
		if (isNullOrEmpty(existingRecord)) {
			String outputText = (String) (new HttpConnectionTask()
					.execute(inputText)).get();
			JSONObject jObj = new JSONObject(outputText);
			String translationResult = jObj.getString(OUTPUT);
			if (isNullOrEmpty(translationResult)) {
				outputMap.put(OUTPUTTEXT, jObj.getString(MESSAGE));
			} else {
				outputMap.put(OUTPUTTEXT, translationResult);
			}
			dataSource.createTranslationRecord(inputText, translationResult);
		}else{
			outputMap.put(OUTPUTTEXT, existingRecord);
		}
		messages.add(outputMap);
		setListAdapter();
	}
	
	private void setListAdapter() {
		SimpleAdapter adapter = new SimpleAdapter(this, messages, R.layout.my_itemlist, new String[]{INPUTTEXT, OUTPUTTEXT}, new int[]{R.id.inputText, R.id.outputText});
		listView.setAdapter(adapter);
	}
	
	public static boolean isNullOrEmpty(String str){
		return str == null || str.trim().length() == 0;
	}
	
	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}
	
}
