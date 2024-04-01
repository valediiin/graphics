package de.jpp.io;

import de.jpp.io.interfaces.ParseException;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class TwoDimImgReader extends GridReaderTemplate<BufferedImage>{
    @Override
    public TwoDimGraph read(BufferedImage input) throws ParseException {

        if(input == null){
            throw new ParseException("File is null");
        }

        Map<String, XYNode> nodes = new HashMap<>();

        for(int y = 0; y < input.getHeight(); y++){
            for(int x = 0; x < input.getWidth(); x++){
                float[] hsb = new float[3];
                Color c = new Color(input.getRGB(x,y));
                Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb);

                if(hsb[2] >= 0.5){
                    nodes.put(String.format("%s|%s", x, y), new XYNode("", x, y));
                }
            }
        }

        return createGraph(nodes);
    }
}
