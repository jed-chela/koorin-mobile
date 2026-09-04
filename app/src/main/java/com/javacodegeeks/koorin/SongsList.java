package com.javacodegeeks.koorin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class SongsList extends Activity {

    ListView list_view;
    Song[] songs = {new Song("L'abe Igi Orombo", "labe_igi.mp3"), new Song("Omo Pupa", "omo_pupa.mp3")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_list);

        list_view = (ListView)findViewById(R.id.songs_list);

        final String[] song_names = new String[songs.length];

        for(int i = 0; i < songs.length; i++){
            Song song = songs[i];
            song_names[i] = song.getSongName();
        }

        list_view.setAdapter(new yourAdapter(this, song_names));

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.d("Log", "clicked item position: " + position);

                String song_file_name   = songs[position].getSongFileName();
                String song_name        = songs[position].getSongName();

                Intent i = new Intent(SongsList.this, MediaPlayer.class);
                i.putExtra("song_file_name", song_file_name);
                i.putExtra("song_name", song_name);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_songs_list, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    //    finish();
    }

}

class yourAdapter extends BaseAdapter {

    Context context;
    String[] data;
    private static LayoutInflater inflater = null;

    public yourAdapter(Context context, String[] data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.song_list_view_format, null);

        TextView num_header = (TextView) vi.findViewById(R.id.songs_list_header);
        num_header.setText(Integer.toString(position + 1));

        TextView text = (TextView) vi.findViewById(R.id.songs_list_text);
        text.setText(data[position]);

        return vi;
    }

}