package algorithmsprograms;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Picture;

import java.awt.*;

import static edu.princeton.cs.algs4.Heap.sort;

public class ObsRemoval {
    private static Picture getNewPicture(Picture[] pictures)
    {
        int width = pictures[0].width();
        int height = pictures[1].height();
        Picture newPic = new Picture(width, height);

        Color[] colors = new Color[pictures.length];


        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < height; j++)
            {
                for(int q = 0; q < pictures.length; q++)
                {
                    Color c = pictures[q].get(i, j);
                    colors[q] = c;
                }
                Color newColor = getMedian(colors);
                newPic.set(i, j, newColor);
            }
        }
        return newPic;
    }

    private static Color getMedian(Color[] colors)
    {
        Integer[] reds = new Integer[colors.length];
        Integer[]greens = new Integer[colors.length];
        Integer[] blues = new Integer[colors.length];

        for(int j = 0; j < colors.length; j++)
        {
            reds[j] = colors[j].getRed();
            greens[j] = colors[j].getGreen();
            blues[j] = colors[j].getBlue();
        }

        sort((Comparable[])reds);
        sort((Comparable[])greens);
        sort((Comparable[])blues);

        Integer redMed = null;
        Integer greenMed = null;
        Integer blueMed = null;
        if(colors.length % 2 != 0)
        {
            redMed = reds[(int) Math.floor(colors.length / 2)];
            greenMed = greens[(int) Math.floor(colors.length / 2)];
            blueMed = blues[(int) Math.floor(colors.length / 2)];
        }
        else {
            redMed = reds[(int) (colors.length / 2)];
            greenMed = greens[(int) (colors.length / 2)];
            blueMed = blues[(int) (colors.length / 2)];
        }

        Color color = new Color(redMed, greenMed, blueMed);
        return color;
    }

    public static void main(String[] args)
    {
        Picture[] pics = new Picture[args.length];

        for(int i = 0; i < args.length; i++)
        {
            pics[i] = new Picture(args[i]);
        }

        Picture newPhoto = getNewPicture(pics);

        newPhoto.save("NonObscured.jpg");
    }
}

