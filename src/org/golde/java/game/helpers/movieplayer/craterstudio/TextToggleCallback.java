package org.golde.java.game.helpers.movieplayer.craterstudio;

public interface TextToggleCallback
{
   public void onMatch(String value);

   public void onOther(String value);

   public void onDone(boolean endedInMatch);
}