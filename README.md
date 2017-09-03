# blind-test-game

Blind test game in Java.
Try to guess the songs that play!

The song will play for 10 seconds, then you can type the answer.
You get 3 tries, 100 points per correct answer, and the hint costs 50 points.

Requirements

  VLC 64bit installed (game uses vlcj for music playing)
  In your music files, the tag "Title" and "Album Artist" needs to be set.
  "Title" is used for the answer, "Album Artist" is used for the hint (else you get "[[Unknown]]".

build

  mvn clean compile assembly:single

launch

  java -jar <jarfile> <file>

The <file> argument is the list of songs that will be used for the game: one filename per line.
If you have you songs in a music library software, you can make a playlist and export it to get a compatible file.
