package com.yw2ugly.editor;

import org.springframework.core.io.ResourceEditor;

public class ResourceEditorTest {
    public static void main(String[] args) {
        ResourceEditor resourceEditor = new ResourceEditor();

        resourceEditor.setAsText("classpath:application.properties");

        System.err.println(resourceEditor.getAsText());
        System.err.println(resourceEditor.getValue());

    }
}
