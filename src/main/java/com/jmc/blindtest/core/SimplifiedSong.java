package com.jmc.blindtest.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.AbstractFrameBodyTextInfo;

public class SimplifiedSong {
	
	public final String filename;
	public final String title;
	public final String albumArtist;
	
	public SimplifiedSong(String filename, String title, String albumArtist) {
		this.filename = filename;
		this.title = title;
		this.albumArtist = albumArtist;
	}
	
	public static Optional<SimplifiedSong> ofFile(String filename) {
		String ext = Utils.getFileExtension(new File(filename)).toLowerCase();
        switch(ext) {
            case "flac"     :     return ofFlac(filename);
            case "mp3"		:     return ofMP3(filename);
            default         :     return Optional.empty(); 
        }
	}
	
	private static Optional<SimplifiedSong> ofFlac(String source) {
		File file = new File(source);
        try {
            AudioFile songFile = AudioFileIO.read(file);
            Tag tag = songFile.getTag();
            
            String title    = tag.getFirst(FieldKey.TITLE).trim();
            if(title.isEmpty()) title = new File(source).getName().toString();
            
            List<String> albumArtists = new ArrayList<String>();
            tag.getFields(FieldKey.ALBUM_ARTIST).stream()
                                                .map(t -> t.toString().trim())
                                                .forEach(t -> albumArtists.add(t));
            if(albumArtists.isEmpty()) albumArtists.add("[[Unknown]]");
            
            SimplifiedSong song = new SimplifiedSong(source, title, albumArtists.stream().collect(Collectors.joining(", ")));
            return Optional.of(song);
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
	}
	
	private static Optional<SimplifiedSong> ofMP3(String source) {
		File file = new File(source);
		try {
			MP3File songFile = (MP3File) AudioFileIO.read(file);
			ID3v24Tag tag = songFile.getID3v2TagAsv24();
			
			String title = tag.getFirst(FieldKey.TITLE).trim();
			if(title.isEmpty()) title = new File(source).getName().toString();
			
            List<String> albumArtists = new ArrayList<String>();
            AbstractID3v2Frame aaFrame = tag.getFirstField(ID3v24Frames.FRAME_ID_ACCOMPANIMENT); //TPE2 ID3v24 tag
            if(aaFrame.getBody() instanceof AbstractFrameBodyTextInfo) {
            	AbstractFrameBodyTextInfo textBody = (AbstractFrameBodyTextInfo)aaFrame.getBody(); 
            	albumArtists.addAll(Stream.of(textBody.getText().split(";")).collect(Collectors.toList()));
            }
            if(albumArtists.isEmpty()) albumArtists.add("[[Unknown]]");
            
            SimplifiedSong song = new SimplifiedSong(source, title, albumArtists.stream().collect(Collectors.joining(", ")));
            return Optional.of(song);
			
		} catch (Exception e) {
			System.err.println("Can't parse song " + source);
            System.err.println("Error is : " + e.getMessage());
		}
		
		return Optional.empty();
	}

}
