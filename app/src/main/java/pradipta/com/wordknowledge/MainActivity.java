package pradipta.com.wordknowledge;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            DialogFragment customFragment = new AboutDialog();//Since AboutDialog extends DialogFragment,, Both are similar
            customFragment.show(getFragmentManager(),"theDialog"); //The second parameter is the id of our created fragment
            // in our AboutDialog.java file
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSubmit(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        if(!isEmpty(editText)){
            Toast.makeText(this,"Getting Knowledge..",Toast.LENGTH_LONG).show();

            new ConnectToAPI().execute();
        }
        else{
            Toast.makeText(this,"Enter a Word",Toast.LENGTH_SHORT).show();
        }
    }
    protected boolean isEmpty(EditText editText){
        return editText.getText().toString().trim().length()==0;
    }
    //This is a ASync Class that runs in the background
    class ConnectToAPI extends AsyncTask<Void, Void, Void>{
        String jsonString="";
        String synonymStr = "Synonyms: \n\n";
        String antonymStr = "Antonyms: \n\n";
        boolean failed = false;
        // Called after doInBackground finishes executing
        @Override
        protected void onPostExecute(Void aVoid) {

            TextView res = (TextView) findViewById(R.id.result);

            if (failed){
                res.setText("The API does not have information about that word");
            }
            else {
                res.setText(antonymStr + "\n\n\n" + synonymStr);

                antonymStr = "";
                synonymStr = "";
            }

        }
        //Memroize the damn below syntax all right?
        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("doInBackground","Beginning this method");

            EditText editText = (EditText) findViewById(R.id.editText);
            String word = getFirstWord(String.valueOf(editText.getText()));
            DefaultHttpClient client = new DefaultHttpClient(new BasicHttpParams());
            HttpGet post = new HttpGet("http://pydictionary-geekpradd.rhcloud.com/api/total/" + word);
            post.setHeader("Content-type","application/json");

            InputStream stream = null; //This reads the Http content as Bytes

            try{
                HttpResponse response = client.execute(post);

                HttpEntity entity = response.getEntity();

                stream = entity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line=reader.readLine())!=null){
                    sb.append(line+'\n');
                }
                jsonString = sb.toString();
                Log.i("doinbg",jsonString);
                JSONObject jObj = new JSONObject(jsonString);
                if (jObj.has("error")){
                    failed = true;
                }
                else {
                    JSONArray antonyms = jObj.getJSONArray("antonym");
                    JSONArray synonyms = jObj.getJSONArray("synonym");
                    handleArrays(antonyms, synonyms);
                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        public void handleArrays(JSONArray ants, JSONArray syns){
            Log.i("handleArrays","Beginning this method");
            for (int i=0;i<ants.length();i++){
                try {
                    antonymStr = antonymStr + ants.getString(i) + ", ";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (int i=0;i<syns.length();i++){
                try {
                    if (i==(syns.length()-1)){
                        synonymStr = synonymStr + syns.getString(i);
                    }
                    else {
                        synonymStr = synonymStr + syns.getString(i) + ", ";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        public String getFirstWord(String str){
           return str.split(" ")[0];
        }
    }
}


