package org.golde.java.game.helpers;

import org.newdawn.slick.util.LogSystem;

/**
 * This is top stop SlickUtil from throwing pointless PNG related errors.
 * Not the best fix but whatever
 * @author Eric
 *
 */
public class BlankLogger implements LogSystem{

	@Override
	public void error(String paramString, Throwable paramThrowable) {}

	@Override
	public void error(Throwable paramThrowable) {}

	@Override
	public void error(String paramString) {}

	@Override
	public void warn(String paramString) {}

	@Override
	public void warn(String paramString, Throwable paramThrowable) {}

	@Override
	public void info(String paramString) {}

	@Override
	public void debug(String paramString) {}

}
