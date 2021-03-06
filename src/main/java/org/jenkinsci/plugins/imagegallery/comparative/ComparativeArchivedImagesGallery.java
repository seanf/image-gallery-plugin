/*
 * The MIT License
 *
 * Copyright (c) 2012-2015 Richard Lavoie, BioUno
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.imagegallery.comparative;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jenkinsci.plugins.imagegallery.AbstractArchivedImagesGallery;
import org.jenkinsci.plugins.imagegallery.ImageGalleryDescriptor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.FilePath;
import hudson.util.FormValidation;


/**
 * Base image gallery to compare images in different way.
 *
 * @author Richard Lavoie
 * @since 1.0
 */
public abstract class ComparativeArchivedImagesGallery extends AbstractArchivedImagesGallery {

	/*
     * serial version UID.
     */
    private static final long serialVersionUID = -1507776620685441240L;

    /**
	 * Title.
	 */
	private final String title;
	
	/**
	 * Include pattern.
	 */
	private final String baseRootFolder;
	
	/**
     * Images width.
     */
	@Deprecated
    private Integer imageInnerWidth;

	/**
	 * Images inner width.
	 */
	private final String imageInnerWidthText;

	/**
	 * Constructor called from jelly.
	 * @param title gallery title
	 * @param baseRootFolder base root folder
	 * @param imageWidth width
	 * @param imageInnerWidth inner width
	 * @param markBuildAsUnstableIfNoArchivesFound flag to fail build if nothing found
	 */
	@Deprecated
	public ComparativeArchivedImagesGallery(String title, String baseRootFolder, Integer imageWidth, Integer imageInnerWidth,
                                                    boolean markBuildAsUnstableIfNoArchivesFound) {
		super(title, imageWidth, markBuildAsUnstableIfNoArchivesFound);
		this.title = title;
		this.baseRootFolder = baseRootFolder;
        this.imageInnerWidth = imageInnerWidth;
        imageInnerWidthText = Integer.toString(imageInnerWidth);
	}

	/**
     * Constructor called from jelly.
     * @param title gallery title
     * @param baseRootFolder base root folder
     * @param imageWidthText width
     * @param imageInnerWidthText inner width
     * @param markBuildAsUnstableIfNoArchivesFound flag to fail build if nothing found
     */
    public ComparativeArchivedImagesGallery(String title, String baseRootFolder, String imageWidthText, String imageInnerWidthText,
                                                    boolean markBuildAsUnstableIfNoArchivesFound) {
        super(title, imageWidthText, markBuildAsUnstableIfNoArchivesFound);
        this.title = title;
        this.baseRootFolder = baseRootFolder;
        this.imageInnerWidthText = imageInnerWidthText;
    }
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return the baseRootFolder
	 */
	public String getBaseRootFolder() {
		return baseRootFolder;
	}

    /**
     * @return the imageInnerWidth
     */
	@Deprecated
    public Integer getImageInnerWidth() {
        return imageInnerWidth;
    }

	/**
	 * @return the imageInnerWidthText
	 */
	public String getImageInnerWidthText() {
	    return imageInnerWidthText;
	}

	public static abstract class ComparativeDescriptorImpl extends ImageGalleryDescriptor {

        public FormValidation doCheckImageInnerWidth(StaplerRequest req, StaplerResponse resp, @QueryParameter String imageInnerWidthText) throws Exception {
            return FormValidation.validateRequired(imageInnerWidthText);
        }

	}

    /**
     * <p>Retrieve an array with the directories required to go from parent to file. Like in a network where you can
     * trace for hops.</p>
     *
     * <p>For example, from /home/silb/archives to /home/silb/archives/images/1.png, the method will return an array
     * with images and 1.png.</p>
     * @param file target file
     * @param parent parent file
     * @return an array with directories to go from {@code parent} to {@code file}.
     */
    protected List<String> getRelativeFrom(FilePath file, FilePath parent) {
        String fileRemote = file.getRemote();
        String parentRemote = parent.getRemote();
        Path filePath = Paths.get(fileRemote);
        Path parentPath = Paths.get(parentRemote);
        Path relativePath = parentPath.relativize(filePath);
        String[] paths = relativePath.toString().split("/");
        List<String> list = new ArrayList<String>();
        list.add("/");
        for (String path : paths) {
            if (!path.trim().equals("")) {
                list.add(path);
            }
        }
        return list;
    }
}
