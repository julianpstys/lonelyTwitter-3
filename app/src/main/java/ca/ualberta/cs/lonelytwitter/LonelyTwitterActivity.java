package ca.ualberta.cs.lonelytwitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class LonelyTwitterActivity extends Activity {

	private static final String FILENAME = "tweet_list.sav";
	private EditText bodyText;
	private ListView oldTweetsList;

	private ArrayList<Tweet> tweetList;
	private ArrayAdapter<Tweet> adapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("LifeCycle ---->", "onCreate is called");
		setContentView(R.layout.main);

		bodyText = (EditText) findViewById(R.id.body);
		Button saveButton = (Button) findViewById(R.id.save);

		// Clear button
		Button clearButton = (Button) findViewById(R.id.clear);

		oldTweetsList = (ListView) findViewById(R.id.oldTweetsList);

		clearButton.setOnClickListener(new View.OnClickListener() {

			// On click view method
			public void onClick(View v) {

				// From: https://stackoverflow.com/questions/3802304/clear-listview-content
				oldTweetsList.setAdapter(null); // Clears all tables in oldTweetList

				// Deletes old tweet file
				// From: https://www.geeksforgeeks.org/delete-file-using-java/
				File file = new File(FILENAME);
				file.delete();





			}
		});


		saveButton.setOnClickListener(new View.OnClickListener() {


			public void onClick(View v) {
				setResult(RESULT_OK);
				String text = bodyText.getText().toString();

				Tweet tweet = new NormalTweet(text);
				tweetList.add(tweet);


				adapter.notifyDataSetChanged();

				saveInFile();

				finish();

			}
		});

	}

	@Override
	protected void onStart() {

		// TODO Auto-generated method stub
		super.onStart();
		Log.i("LifeCycle --->", "onStart is called");
		loadFromFile();

		adapter = new ArrayAdapter<Tweet>(this, R.layout.list_item, tweetList);


		oldTweetsList.setAdapter(adapter);

		// -1

		/*NormalTweet normalTweet = new NormalTweet("");
        try {
            normalTweet.setMessage("Hello World!");
//            normalTweet.setMessage("aaaaaaaa123904ifkdjfhirhtiorhtherihgtjkerhgjkhergtjkherjkh49ryhui4thg754tdfjlsdkhioerhgiohruioghejiogfuioerhgfiohrjkfhasdjkhgiuhgiorgjkdfhgidfgsdkljfkldsjfklsdjklfjdklsfjkldghsk");
        }
        catch (TweetTooLongException e) {
            Log.e("Error ---->", "Tweet message too long");

        }


        ImportantTweet importantTweet1 = new ImportantTweet("Hello World! This is important");
        ImportantTweet importantTweet2 = new ImportantTweet("This is another important tweet");

        NormalTweet normalTweet1 = new NormalTweet("This is not that important");
        NormalTweet normalTweet2 = new NormalTweet("This is not that important either");

        ArrayList <Tweet> tweetList = new ArrayList<Tweet>();
        tweetList.add(normalTweet);
        tweetList.add(normalTweet1);
        tweetList.add(normalTweet2);
        tweetList.add(importantTweet1);
        tweetList.add(importantTweet2);

        for (Tweet t:
                tweetList) {
            Log.d("Tweet Polymorphism", t.isImportant().toString());

        }


        ArrayList <Tweetable> tweetableList = new ArrayList<Tweetable>();
        tweetableList.add(normalTweet);
        tweetableList.add(normalTweet1);
        tweetableList.add(normalTweet2);
        tweetableList.add(importantTweet1);
        tweetableList.add(importantTweet2);

        String messageOnScreen = "";
        for (Tweetable t:
             tweetableList) {
            messageOnScreen += t.getMessage() + "\n";
        }
        Toast.makeText(this, messageOnScreen, Toast.LENGTH_SHORT).show();*/
		// -2
	}

	private void loadFromFile() {


		try {

			FileInputStream fis = openFileInput(FILENAME);

			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson=new Gson();

			// From https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
			Type listType = new TypeToken<ArrayList<NormalTweet>>(){}.getType();
			tweetList = gson.fromJson(in, listType);


		} catch (FileNotFoundException e) {
			tweetList=new ArrayList<Tweet>();

			e.printStackTrace();
		}  catch (IOException e) {
			throw new RuntimeException();
			//e.printStackTrace();
		}

	}


	private void saveInFile() { // String text, Date date
		try {

			FileOutputStream fos = openFileOutput(FILENAME,
					Context.MODE_PRIVATE);
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

			Gson gson = new Gson();

			gson.toJson(tweetList, out);
			out.flush();



		} catch (FileNotFoundException e) {
			throw new RuntimeException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}

	}

	@Override
	protected void onDestroy() {


		super.onDestroy();

		Log.i("Lifecycle", "onDestroy is called");
	}

}