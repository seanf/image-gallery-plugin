/*
 * The MIT License
 *
 * Copyright (c) 2012-2015 Bruno P. Kinoshita, BioUno
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
package org.jenkinsci.plugins.imagegallery;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

import javax.annotation.Nonnull;

import hudson.DescriptorExtensionList;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Node;
import hudson.model.TaskListener;
import jenkins.model.Jenkins;


/**
 * Image gallery extension point. Multiple image gallery types extend this class.  
 *
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 0.1
 */
public abstract class ImageGallery implements Serializable, Describable<ImageGallery>, Comparable<ImageGallery> {

	/*
     * serial version UID.
     */
    private static final long serialVersionUID = 3016249288702988364L;
    /**
	 * Title.
	 */
	private final String title;
	/**
	 * Images width.
	 */
	@Deprecated
	private Integer imageWidth;
	
	private final String imageWidthText;
	
	@Deprecated
	public ImageGallery(String title, Integer imageWidth) {
		this.title = title;
		this.imageWidth = imageWidth;
		imageWidthText = Integer.toString(imageWidth);
	}

	public ImageGallery(String title, String imageWidthText) {
	    this.title = title;
	    this.imageWidthText = imageWidthText;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return the imageWidth
	 */
	@Deprecated
	public Integer getImageWidth() {
		return imageWidth;
	}

	/**
     * @return the imageWidthText
     */
    public String getImageWidthText() {
        return imageWidthText;
    }

    /* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(ImageGallery o) {
		return o != null ? this.getDescriptor().getDisplayName().compareTo(o.getDescriptor().getDisplayName()) : 0;
	}

	/* (non-Javadoc)
	 * @see hudson.model.Describable#getDescriptor()
	 */
	public Descriptor<ImageGallery> getDescriptor() {
		return (ImageGalleryDescriptor) Jenkins.getInstance().getDescriptor(getClass());
	}
	
	public static DescriptorExtensionList<ImageGallery, Descriptor<ImageGallery>> all() {
		return Jenkins.getInstance().<ImageGallery, Descriptor<ImageGallery>> getDescriptorList(ImageGallery.class);
	}

	public static DescriptorExtensionList<ImageGallery, Descriptor<ImageGallery>> allExcept(Node current) {
		return Jenkins.getInstance().<ImageGallery, Descriptor<ImageGallery>> getDescriptorList(ImageGallery.class);
	}
	
	public Collection<? extends Action> getProjectActions(AbstractProject<?, ?> project) {
		return null;
	}
	
	public abstract boolean createImageGallery(@Nonnull AbstractBuild<?, ?> build, TaskListener listener) throws InterruptedException, IOException;

}
