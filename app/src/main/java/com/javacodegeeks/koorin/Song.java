package com.javacodegeeks.koorin;

/**
 * Created by JED on 8/24/2015.
 */
public class Song {

    public String song_name;
    public String song_file_name;

    Song(String s_name, String s_file_name){
        song_name       = s_name ;
        song_file_name  = s_file_name ;
    }
    String getSongName(){
        return song_name;
    }
    String getSongFileName(){
        return song_file_name;
    }

}
