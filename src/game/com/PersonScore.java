/**
 * 
 */
package game.com;

import org.cocos2d.layers.Layer;
import org.cocos2d.menus.Menu;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.types.CCColor3B;

/**
 * @author Administrator
 *
 */
public class PersonScore extends Layer{
	
	float fx, fy;
	
	public PersonScore()
	{
		fx = Global.rScale_X;
		fy = Global.rScale_Y;
		this.isTouchEnabled_ = true;
		
		Sprite  mainSprite = Sprite.sprite("gfx/booth_back_1.gif");
		mainSprite.setPosition(Global.rWidth / 2, Global.rHeight / 2);
		mainSprite.setScaleX(Global.rScale_X); mainSprite.setScaleY(Global.rScale_Y);
		addChild(mainSprite);
		
		String str = String.format("Your score: %d", Global.g_nTotalScore);
		Label lblScore = Label.label(str, "Marker Felt", 30 * fx);
		lblScore.setPosition(Global.rWidth / 2, Global.rHeight - 32 * fy);
//		lblScore.setScaleX(fx); lblScore.setScaleY(fy);
		addChild(lblScore);
		lblScore.setColor(new CCColor3B(0, 255, 0));
		
		MenuItemImage m_btnBack = MenuItemImage.item("gfx/pr_back_btn.gif", 
													"gfx/pr_back_btn_over.gif",
													this,
												 	"gameBack");
		
		MenuItemImage m_btnBoardWalk = MenuItemImage.item("gfx/pr_boardwalk_btn.gif", 
															"gfx/pr_boardwalk_btn_over.gif",
															this,
														 	"boardWalk");

		Menu menu = Menu.menu(m_btnBack, m_btnBoardWalk);
		menu.setPosition( 0, 0 );
		
		m_btnBack.setScaleX(fx); m_btnBack.setScaleY(fy);
		m_btnBack.setPosition(0.6f * m_btnBack.getWidth() * fx, m_btnBack.getHeight() * fy/2);
		m_btnBoardWalk.setScaleX(fx); m_btnBoardWalk.setScaleY(fy);
		m_btnBoardWalk.setPosition(Global.rWidth - 0.5f * m_btnBoardWalk.getWidth() * fx,
									m_btnBoardWalk.getHeight() * fy/2);
		addChild(menu ,1);	
	}

	public void gameBack()
	{
		System.gc();
		
		if(Global.m_buttonSound != null)
			Global.m_buttonSound.start();
		Scene scene = Scene.node();
		scene.addChild(new PlayView(), 1);
		Director.sharedDirector().replaceScene(BalloonDarts.newScene(GameConfig.TRANSITION_DURATION, 1, scene));
		
	}
	
	public void boardWalk()
	{
		System.gc();
		
		if(Global.m_buttonSound != null)
			Global.m_buttonSound.start();
		
		Scene scene = Scene.node();
		scene.addChild(new HighScore(), 1);
		Director.sharedDirector().replaceScene(BalloonDarts.newScene(GameConfig.TRANSITION_DURATION, 3, scene));
	}
	
}
