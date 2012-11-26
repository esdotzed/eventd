package com.eventd.augment.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.eventd.augment.ui.objects.PaintableIcon;
import com.eventd.augment.ui.objects.PaintablePosition;

/**
 * This class extends Marker and draws an icon instead of a circle for it's visual representation.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class IconMarker extends Marker {
    private Bitmap bitmap = null;
    private PaintableIcon icon = null;
    private PaintablePosition iconContainter = null;

    public IconMarker(String name, double latitude, double longitude, double altitude, int color, Bitmap bitmap) {
        super(name, latitude, longitude, altitude, color);
        this.bitmap = bitmap;
        icon = new PaintableIcon(bitmap);
    }
    
    @Override
    public void drawIcon(Canvas canvas) {
    	if (canvas==null || bitmap==null) return;
    	
        float maxHeight = Math.round(canvas.getHeight() / 10f) + 1;
        
        if (iconContainter==null) 
        	iconContainter = new PaintablePosition(icon, (circleVector.x - maxHeight/1.5f), (circleVector.y - maxHeight/1.5f), 0, 2);
        else 
        	iconContainter.set(icon, (circleVector.x - maxHeight/1.5f), (circleVector.y - maxHeight/1.5f), 0, 2);
        
        iconContainter.paint(canvas);
    }
}
