import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class ImageMerge {

    // Merges two colors buy replacing the least significant bits of
    // color1 with the most significant bits of color2    
    public static Color mergeColors(Color color1, Color color2)
    {
        int red1 = color1.getRed();
        int green1 = color1.getGreen();
        int blue1 = color1.getBlue();
        int red2 = color2.getRed();
        int green2 = color2.getGreen();
        int blue2 = color2.getBlue();
        
        // remove least significant bits from color1
        red1 = red1 - (red1 % 2);
        green1 = green1 - (green1 % 2);
        blue1 = blue1 - (blue1 % 2);
        
        // modify color1 by most significant bit of color2
        if (red2 > 127) red1++;
        if (green2 > 127) green1++;
        if (blue2 > 127) blue1++;
        
        Color newColor = new Color(red1, green1, blue1);
        return newColor;        
    }    
    
    // @param args the command line arguments
    // These should be the input images, with the secret image second
    public static void main(String[] args)
    {
        Picture publicPic = new Picture(args[0]);    // .png, .gif or .jpg
        Picture privatePic = new Picture(args[1]);   // .png, .gif or .jpg
        
        int w = Math.min(publicPic.width(), privatePic.width());
        int h = Math.min(publicPic.height(), privatePic.height());
        Picture mergedPic = new Picture(w, h);
        
        for (int i = 0; i < w; i++)
        {
            for (int j = 0; j < h; j++)
            {
                Color publicColor = publicPic.get(i, j);
                Color privateColor = privatePic.get(i, j);
                Color mergedColor = mergeColors(publicColor, privateColor);                
                mergedPic.set(i, j , mergedColor);
            }
        }
        
        mergedPic.save("OutputImage.png");
    }
    
}
