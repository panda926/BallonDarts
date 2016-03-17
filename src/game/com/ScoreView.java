/**
 * 
 */
package game.com;

import org.cocos2d.actions.interval.MoveTo;
import org.cocos2d.actions.interval.ScaleTo;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.Menu;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.types.CCColor3B;
import org.cocos2d.types.CCPoint;


/**
 * @author Administrator
 *
 */
public class ScoreView extends Layer{

	Sprite  m_maskSprite, m_dollSprite, m_prizeSprite;
	MenuItemImage m_btnContinue;
	
	
	float fx, fy;
	
	public ScoreView()
	{
//		setIsTouchEnabled(true);
		loadImage();
	}
	
	public void loadImage()
	{
		fx = Global.rScale_X;
		fy = Global.rScale_Y;
		
		Sprite  bgSprite = Sprite.sprite("gfx/booth_back_1.gif");
		bgSprite.setPosition(Global.rWidth / 2, Global.rHeight / 2);
		bgSprite.setScaleX(Global.rScale_X); bgSprite.setScaleY(Global.rScale_Y);
		addChild(bgSprite);
		
		Sprite bgSprite1 = Sprite.sprite("gfx/newfrontboothstrip.gif");
		bgSprite1.setPosition(Global.rWidth / 2, bgSprite1.getHeight() * fy / 2);
		bgSprite1.setScaleX(fx); bgSprite1.setScaleY(fy);
		addChild(bgSprite1);
		
		Sprite bgSprite2 = Sprite.sprite("gfx/myballoonsafariprizes.gif");
		bgSprite2.setPosition(Global.rWidth - bgSprite2.getHeight() * fx / 2 - 48 * fx, 
							Global.rHeight - bgSprite1.getHeight() * fy / 2 - 42 * fy);
		bgSprite2.setScaleX(fx); bgSprite2.setScaleY(fy);
		addChild(bgSprite2);
		
		String str = String.format("Your score: %d", Global.g_nTotalScore);
		Label lblScore = Label.label(str, "Marker Felt", 25 * fx);
		lblScore.setPosition(Global.rWidth / 2, Global.rHeight - 32 * fy);
		lblScore.setScaleX(fx); lblScore.setScaleY(fy);
		addChild(lblScore);
		lblScore.setColor(new CCColor3B(0, 255, 0));
		//lblScore.setColor.ccYELLOW];
		
		m_maskSprite = Sprite.sprite("gfx/mask.gif");
		m_maskSprite.setPosition(Global.rWidth / 2, Global.rHeight / 2);
		m_maskSprite.setScaleX(Global.rScale_X); m_maskSprite.setScaleY(Global.rScale_Y);
		addChild(m_maskSprite);
		m_maskSprite.setOpacity((byte)80);
		
		m_prizeSprite = Sprite.sprite("gfx/prizeplaque.gif");
		m_prizeSprite.setPosition(Global.rWidth / 2, Global.rHeight / 2);
		m_prizeSprite.setScaleX(fx); m_prizeSprite.setScaleY(fy);
		addChild(m_prizeSprite);
		
		m_dollSprite = Sprite.sprite("gfx/pr_balloon_small.gif");
		m_dollSprite.setPosition(Global.rWidth / 2 + 67.5f * fx, Global.rHeight / 2);
		m_dollSprite.setScaleX(2*fx); m_dollSprite.setScaleY(2*fy);
		addChild(m_dollSprite);
		
		m_btnContinue = MenuItemImage.item("gfx/pr_continue_btn.gif", 
											"gfx/pr_continue_btn_over.gif",
											this,
										 	"actContinue");
		
		Menu menu = Menu.menu(m_btnContinue);
		menu.setPosition( 0, 0 );
		m_btnContinue.setPosition(Global.rWidth / 2, 0.7f * m_btnContinue.getHeight() * fy);
		m_btnContinue.setScaleX(fx); m_btnContinue.setScaleY(fy);
		addChild(menu ,0);	
		
		
	}
	
	public void actContinue()
	{
		m_maskSprite.setVisible(false);
		m_prizeSprite.setVisible(false);
		m_btnContinue.setVisible(false);
		if(Global.m_buttonSound != null)
			Global.m_buttonSound.start();
//		schedule("dollMove", 0.1f);
		float rTempx = m_dollSprite.getScaleX();
		float rTempy = m_dollSprite.getScaleY();
		CCPoint pTask = CCPoint.make(60 * Global.rScale_X, 260 * Global.rScale_Y);
		m_dollSprite.runAction(MoveTo.action(1.0f, pTask.x, pTask.y));
		m_dollSprite.runAction(ScaleTo.action(1.0f, rTempx, rTempy));
		turn();
		
	}
	
	public void turn()
	{
		System.gc();
		
		Scene scene = Scene.node();
		scene.addChild(new HighScore(), 1);
		Director.sharedDirector().replaceScene(BalloonDarts.newScene(GameConfig.TRANSITION_DURATION, 1, scene));
	}
}
