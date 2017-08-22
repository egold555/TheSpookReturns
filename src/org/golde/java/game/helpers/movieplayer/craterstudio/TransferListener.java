package org.golde.java.game.helpers.movieplayer.craterstudio;

import java.io.IOException;

public interface TransferListener
{
   public void transferInitiated(int expectedBytes);

   public void transfered(int bytes);

   public void transferFinished(IOException potentialException);
}