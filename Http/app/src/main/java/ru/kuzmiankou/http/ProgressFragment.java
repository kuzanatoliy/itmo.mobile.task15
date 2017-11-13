package ru.kuzmiankou.http;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment {

    TextView contentView;
    String contentText = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        contentView = (TextView) view.findViewById(R.id.loading_content);
        if(contentText != null){
            contentView.setText(contentText);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(contentText == null) {
            new ProgressTask().execute();
        }
    }

    class ProgressTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... path) {
            String content;
            try {
                content = getContent("http://vk.com");
            }
            catch(IOException ex) {
                content = ex.getMessage();
            }
            return content;
        }

        @Override
        protected void onProgressUpdate(Void... items) {

        }

        @Override
        protected void onPostExecute(String content) {
            contentText = content;
            contentView.setText(content);
            Toast.makeText(getActivity(), "Data was downloaded", Toast.LENGTH_SHORT).show();
        }

        private String getContent(String path) throws IOException {
            BufferedReader reader=null;
            try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(10000);
                conn.connect();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder buf = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buf.append(line + "\n");
                }
                return(buf.toString());
            }
            finally {
                if(reader != null) {
                    reader.close();
                }
            }
        }
    }

}
