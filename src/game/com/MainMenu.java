/**
 * 
 */
package game.com;

import org.cocos2d.layers.Layer;
import org.cocos2d.menus.Menu;
import org.cocos2d.menus.MenuItem;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.nodes.TextureManager;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCRect;

import android.view.MotionEvent;

/**
 * @author Administrator
 *
 */
public class MainMenu extends Layer{
	static Sprite bgSpriteback, bgSprite1, bgSprite2, 
			bgSprite6;
	static MenuItem btnEnter;
	static CCRect touchRect;
	static boolean fLimited;

	public MainMenu()
	{
		setIsTouchEnabled(true);
		fLimited = false;
		loadImage();
	}
	
	@Override
	public void onExit()
	{
		TextureManager.sharedTextureManager().removeAllTextures();
		this.removeAllChildren(true);
		this.cleanup();
		System.gc();
	}
	
	public void play()
	{
		System.gc();
		TextureManager.sharedTextureManager().removeAllTextures();
		
		Scene scene = Scene.node();
		scene.addChild(new PlayView(), 0);
		Director.sharedDirector().replaceScene(BalloonDarts.newScene(GameConfig.TRANSITION_DURATION, 1, scene));
		
		if(Global.m_buttonSound != null)
			Global.m_buttonSound.start();
		
	}
	public void menuView(float dt)
	{
		bgSpriteback.setPosition(bgSpriteback.getPositionX() - 120 * Global.rScale_X,
									bgSpriteback.getPositionY());
		bgSprite1.setPosition(bgSprite1.getPositionX() - 120 * Global.rScale_X, 
								bgSprite1.getPositionY());
		bgSprite2.setPosition(bgSprite2.getPositionX() - 120 * Global.rScale_X,
								bgSprite2.getPositionY());
		bgSprite6. setPosition(bgSprite6.getPositionX() - 120 * Global.rScale_X, 
								bgSprite6.getPositionY());
		btnEnter.setPosition(btnEnter.getPositionX() - 120 * Global.rScale_X,
								btnEnter.getPositionY());
		
		if (btnEnter.getPositionX() <= Global.rWidth / 2 + 60 * Global.rScale_X) 
		{
			this.unschedule("menuView");
			fLimited = true;
		}
	}
	@Override
	public boolean ccTouchesEnded(MotionEvent event)
	{
		CCPoint point = new CCPoint();
		point.x = event.getX(); point.y = event.getY();
		if(CCRect.containsPoint(touchRect, point))
		{
			if (!fLimited) 
				this.schedule("menuView", 0.05f);
		}
		return true;
	}
	public void loadImage()
	{
		bgSpriteback = Sprite.sprite("gfx/ferriswheel.gif");
		bgSpriteback.setScaleX(Global.rScale_X);
		bgSpriteback.setScaleY(Global.rScale_Y);
		bgSpriteback.setPosition(Global.rWidth / 2 + (bgSpriteback.getWidth()*Global.rScale_X - Global.rWidth)/2,
								Global.rHeight / 2);
		addChild(bgSpriteback, 1);
		
		bgSprite1 = Sprite.sprite("gfx/backwall.gif");
		bgSprite1.setScaleX(Global.rScale_X);
		bgSprite1.setScaleY(Global.rScale_Y);
		bgSprite1.setPosition(bgSpriteback.getPositionX() + 700.0f * Global.rScale_X,
								Global.rHeight / 2);
		addChild(bgSprite1, 1);
		
		bgSprite2 = Sprite.sprite("gfx/boardwalk_sign.png");
		bgSprite2.setPosition(Global.rWidth / 2,
								Global.rHeight / 2);
		bgSprite2.setScaleX(Global.rScale_X);
		bgSprite2.setScaleY(Global.rScale_Y);
		addChild(bgSprite2, 1);

	    bgSprite6 = Sprite.sprite("gfx/balloonbooth.gif");
		bgSprite6.setPosition(Global.rWidth + 264 * Global.rScale_X,
								Global.rHeight / 2);
		bgSprite6.setScaleX(Global.rScale_X);
		bgSprite6.setScaleY(Global.rScale_Y);
		addChild(bgSprite6, 1);
		
		btnEnter = MenuItemImage.item("gfx/enter_btn.gif" ,
										"gfx/enter_btn_over.gif",
										this,
										"play");
		btnEnter.setScaleX(Global.rScale_X);
		btnEnter.setScaleY(Global.rScale_Y);
		Menu menu = Menu.menu(btnEnter);
		menu.setPosition(0, 0);
		btnEnter.setPosition(Global.rWidth + 264 * Global.rScale_X, Global.rHeight / 6);
		addChild(menu, 1);	
		
		touchRect = CCRect.make(50.0f*Global.rScale_X,
								50.0f*Global.rScale_Y, 
								(float)0.8*Global.rWidth, 
								(float)0.8*Global.rHeight);
	}
}


