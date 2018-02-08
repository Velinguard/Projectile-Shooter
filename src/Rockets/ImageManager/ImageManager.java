package Rockets.ImageManager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageManager{
    //Manages the images.
    private BufferedImage image;

    public ImageManager(String path){
        image = null;
        try {
            image = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Removes white background from image
    public BufferedImage transparanty(BufferedImage image){
        ImageFilter filter = new RGBImageFilter() {
            int transparentColor = Color.white.getRGB() | 0xFF000000;

            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == transparentColor) {
                    return 0x00FFFFFF & rgb;
                } else {
                    return rgb;
                }
            }
        };

        ImageProducer filteredImgProd = new FilteredImageSource( image.getSource(), filter);
        Image transparentImg = Toolkit.getDefaultToolkit().createImage(filteredImgProd);
        BufferedImage bimage = new BufferedImage(transparentImg.getWidth(null), transparentImg.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(transparentImg, 0, 0, null);
        bGr.dispose();

        return bimage;

    }

    public int getHeight(){
       return image.getHeight();
    }
    public int getWidth(){
        return image.getWidth();
    }

    public BufferedImage getImage() {
        return image;
    }

    //Rotates the image with the rocket.
    public BufferedImage transformer(double angle){
        image = transparanty(image);
        AffineTransform transform = new AffineTransform();
        transform.translate(image.getWidth() / 2, image.getWidth() / 2);
        transform.rotate(angle, image.getWidth() / 2, image.getHeight()/ 2);
        transform.translate(-image.getWidth() /2 + 4,-image.getWidth() / 2);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, null);
    }
}
