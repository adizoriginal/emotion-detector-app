package com.example.stressleveldetector.model;

import java.util.List;

public class FacePlusPlusResponse {
    public List<Face> faces;

    public static class Face {
        public Attributes attributes;
    }

    public static class Attributes {
        public Emotion emotion;
    }

    public static class Emotion {
        public float anger;
        public float disgust;
        public float fear;
        public float happiness;
        public float neutral;
        public float sadness;
        public float surprise;
    }
}
