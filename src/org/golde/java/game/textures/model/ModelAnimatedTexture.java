package org.golde.java.game.textures.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.golde.java.game.renderEngine.Loader;

public class ModelAnimatedTexture extends ModelTexture{

	private List<Integer> frames = new ArrayList<Integer>();
	private int currentFrame = 0;
	private int minFrame;
	private int maxFrame;
	private int renderEveryXFrames;
	
	public ModelAnimatedTexture(Loader loader, String folder, int renderEveryXFrames, boolean randomFrameOrder) {
		this.renderEveryXFrames = renderEveryXFrames;
		File directory = new File("res/" + folder);
		File[] files = directory.listFiles();
		if(!randomFrameOrder) {
			files = sortByNumber(files);
		}
		for(File file:files) {
			if(file.isDirectory()) {continue;}
			int loadNum = loader.loadTexture(folder + "/" + file.getName(), false);
			frames.add(loadNum);
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
	
	private int tempFrameCount = 0;
	@Override
	public void onRender() {
		
		if(tempFrameCount >= renderEveryXFrames) {
			nextFrame();
			tempFrameCount = 0;
		}
			
		tempFrameCount++;
	}
	
	private void nextFrame() {
		currentFrame++;
		if(currentFrame > maxFrame) {
			currentFrame = minFrame;
		}
	}

	@Override
	public int getTextureID() {
		return currentFrame;
	}
	
}
