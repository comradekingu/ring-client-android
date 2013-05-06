package com.savoirfairelinux.sflphone.model;

import java.util.ArrayList;

public class BubbleModel
{
	private static final String TAG = BubbleModel.class.getSimpleName();

	public long lastUpdate = 0;
	public int width, height;
	public ArrayList<Bubble> listBubbles = new ArrayList<Bubble>();

	private static final double BUBBLE_RETURN_TIME_HALF_LIFE = .25;
	private static final double BUBBLE_RETURN_TIME_LAMBDA = Math.log(2)/BUBBLE_RETURN_TIME_HALF_LIFE;

	public void update()
	{
		long now = System.nanoTime();

		// Do nothing if lastUpdate is in the future.
		if (lastUpdate > now)
			return;

		double dt = Math.min((now - lastUpdate) / 1000000000.0, .2);
		lastUpdate = now;

		//Log.w(TAG, "update dt="+dt);

		// Iterators should not be used in frequently called methods
		// to avoid garbage collection glitches caused by iterator objects.
		for(int i=0, n=listBubbles.size(); i<n; i++) {
			Bubble b = listBubbles.get(i);
			//Log.w(TAG, "update b");
			if(!b.dragged && b.attractor != null) {
				double bx=b.getPosX(), by=b.getPosY();
				double edt = -Math.expm1(-BUBBLE_RETURN_TIME_LAMBDA*dt);
				double dx = (b.attractor.x - bx) * edt;
				double dy = (b.attractor.y - by) * edt;
				//Log.w(TAG, "update dx="+dt+" dy="+dy);
				b.setPos((float)(bx+dx), (float)(by+dy));
			}
		}
	}
}
