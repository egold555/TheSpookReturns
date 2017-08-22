package org.golde.java.game.textures.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.golde.java.game.renderEngine.Loader;
import org.lwjgl.util.vector.Vector2f;

public class GuiAnimatedTexture extends GuiStaticTexture{

	private List<Integer> frames = new ArrayList<Integer>();
	private int currentFrame = 0;
	private int minFrame;
	private int maxFrame;
	private int renderEveryXFrames;
	
	public GuiAnimatedTexture(Loader loader, String animationFolder, Vector2f position, Vector2f scale, int renderEveryXFrames) {
		this(loader, animationFolder, position, scale,renderEveryXFrames, false);
	}
	
	public GuiAnimatedTexture(Loader loader, String animationFolder, Vector2f position, Vector2f scale, int renderEveryXFrames, boolean randomFrameOrder) {
		super(position, scale);
		this.renderEveryXFrames = renderEveryXFrames;
		File directory = new File("res/gui/" + animationFolder);
		File[] files = directory.listFiles();
		if(!randomFrameOrder) {
			files = sortByNumber(files);
		}
		for(File file:files) {
			if(file.isDirectory()) {continue;}
			frames.add(loader.loadTexture("gui/" + animationFolder + "/" + file.getName(), false));
		}
		
		minFrame = frames.get(0);
		maxFrame = frames.get(frames.size() - 1);
		currentFrame = minFrame;
	}
	
	 private File[] sortByNumber(File[] files) {
	        Arrays.sort(files, new Comparator<File>() {
	            @Override
	            public int compare(File o1, File o2) {
	                int n1 = extractNumber(o1.getName());
	                int n2 = extractNumber(o2.getName());
	                return n1 - n2;
	            }

	            private int extractNumber(String name) {
	                int i = 0;
	                try {
	                    int s = name.indexOf('_')+1;
	                    int e = name.lastIndexOf('.');
	                    String number = name.substring(s, e);
	                    i = Integer.parseInt(number);
	                } catch(Exception e) {
	                    i = 0; 
	                }
	                return i;
	            }
	        });
	        
	        return files;
	    }

	@Override
	public int getTexture() {
		return currentFrame;
	}
	
	private int tempFrameCount = 0;
	@Override
	public void onRender() {
		tempFrameCount++;
		if(tempFrameCount > renderEveryXFrames) {
			nextFrame();
			tempFrameCount = 0;
		}
			
		
	}
	
	public void nextFrame() {
		if(currentFrame >= maxFrame) {
			currentFrame = minFrame;
		}
		currentFrame++;
	}
	
}
