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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * @author Administrator
 *
 */
public class HighScore extends Layer{

	float fx, fy;
	int nIterm;
	public static CharSequence playerName;
	public static String szName = null;	

	public HighScore()
	{
		fx = Global.rScale_X;
		fy = Global.rScale_Y;
		nIterm = 0;
		
		Sprite  mainSprite = Sprite.sprite("gfx/booth_back_1.gif");
		mainSprite.setPosition(Global.rWidth / 2, Global.rHeight / 2);
		mainSprite.setScaleX(Global.rScale_X); mainSprite.setScaleY(Global.rScale_Y);
		addChild(mainSprite);
		
		Sprite  bgSprite_0 = Sprite.sprite("gfx/myballoonsafariprizes.gif");
		bgSprite_0.setPosition(Global.rWidth - 1.2f*bgSprite_0.getWidth(), 
								Global.rHeight - bgSprite_0.getHeight());
		bgSprite_0.setScaleX(fx); bgSprite_0.setScaleY(fy);
		addChild(bgSprite_0);
		
		if(Global.g_fProgressivePlay)
		{
			String str = String.format("PROGRESSIVEPLAY!");
			Label lblScore = Label.label(str, "Marker Felt", 30 * fx);
			lblScore.setPosition(Global.rWidth / 2, Global.rHeight - 32 * fy);
//			lblScore.setScaleX(fx); lblScore.setScaleY(fy);
			addChild(lblScore);
			lblScore.setColor(new CCColor3B(0, 255, 0));
		}else
		{
			String str = String.format("QUICKLY ACTION!");
			Label lblScore = Label.label(str, "Marker Felt", 30 * fx);
			lblScore.setPosition(Global.rWidth / 2, Global.rHeight - 32 * fy);
//			lblScore.setScaleX(fx); lblScore.setScaleY(fy);
			addChild(lblScore);
			lblScore.setColor(new CCColor3B(0, 255, 0));
		}
		Sprite  bfSprite_1 = Sprite.sprite("gfx/scorepad.gif");
		bfSprite_1.setPosition(Global.rWidth / 2, Global.rHeight / 2);
		bfSprite_1.setScaleX(fx); bfSprite_1.setScaleY(fy);
		addChild(bfSprite_1);
		
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
		m_btnBack.setPosition(45 * fx, 25 * fy);
		m_btnBoardWalk.setScaleX(fx); m_btnBoardWalk.setScaleY(fy);
		m_btnBoardWalk.setPosition(Global.rWidth - 70 * fx,
									25 * fy);
		addChild(menu , 1);	
		
		getUserInfo();
	}

	public void getUserInfo()
	{
		if(Global.g_fGameDone)
		{
			final LayoutInflater factory = LayoutInflater.from(Director.sharedDirector().getActivity());
	        final View Name_Edit = factory.inflate(R.layout.name_edit, null);
	        final EditText player = (EditText)Name_Edit.findViewById(R.id.player_name_edit);
	        new AlertDialog.Builder(Director.sharedDirector().getActivity())
	            .setTitle("INSERT YOUR NAME !")
	            .setMessage("The name must be less than 10 characters!")
	            .setView(Name_Edit)
	            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	playerName = player.getText();
	                	szName = playerName.toString();

	                	if(szName.compareTo("") != 0)
		                	getInfo();
	                    /* User clicked OK so do some stuff */
	                }
	            })
	            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	getInfo();
	                    /* User clicked cancel so do some stuff */
	                }
	            })
	            .show();
	        
		}else
			getInfo();
		
		
	}
	
	public void getInfo()
	{
		if(Global.g_fProgressivePlay && Global.g_fGameDone && szName != null)
    	{
    		Global.mpUserName[Global.g_nPUserNum] = szName;
    		Global.g_nPUserNum++;
    	}else if( !Global.g_fProgressivePlay && Global.g_fGameDone && szName != null)
    	{
    		Global.mUserName[Global.g_nUserNum] = szName;
    		Global.g_nUserNum ++;
    	}
		Global.g_fGameDone = false;
		
		
		if(Global.g_fProgressivePlay)
			DisplayInfo_();
		else
			DisplayInfo();
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
		// TODO Auto-generated method stub
		System.gc();
		
		if(Global.m_buttonSound != null)
			Global.m_buttonSound.start();
		Scene scene = Scene.node();
		scene.addChild(new PersonScore(), 1);
		Director.sharedDirector().replaceScene(BalloonDarts.newScene(GameConfig.TRANSITION_DURATION, 3, scene));
	}
	
	public void DisplayInfo()
	{
		// TODO Auto-generated method stub
		String str;
		
		if(Global.g_nUserNum > 1)
			reSortInfo();
		for(int i = 0; i < Global.g_nUserNum; i++)
		{
			str = String.format(Global.mUserName[i]);
			Label lblScore = Label.label(str, "Marker Felt", 17);
			lblScore.setPosition(0.3f * Global.rWidth, 0.56f * Global.rHeight - i * 18 * fy);
			lblScore.setScaleX(fx); lblScore.setScaleY(fy);
			addChild(lblScore);
			lblScore.setColor(new CCColor3B(255, 255, 0));
			
			str = String.format("%d", Global.mScore[i]);
			Label lblScore_ = Label.label(str, "Marker Felt", 17);
			lblScore_.setPosition(0.6f * Global.rWidth, 0.56f * Global.rHeight - i * 18 * fy);
			lblScore_.setScaleX(fx); lblScore_.setScaleY(fy);
			addChild(lblScore_);
			lblScore_.setColor(new CCColor3B(255, 255, 0));
		}
		if(Global.g_nUserNum == GameConfig.MAX_USER_NUM)
			Global.g_nUserNum--;
	}

	public void reSortInfo() 
	{
		// TODO Auto-generated method stub
		String stemp;
		int ntemp;
		int nNum = Global.g_nUserNum;
		
		ntemp = Global.mScore[nNum];
		stemp = Global.mUserName[nNum];
		for(int i = 0; i < Global.g_nUserNum - 1; i++)
		{
			if(ntemp > Global.mScore[i])
			{
				nNum = i;
				break;
			}
		}
		for(int i = Global.g_nUserNum - 1; i > nNum - 1; i-- )
		{
			Global.mScore[i + 1] = Global.mScore[i];
			Global.mUserName[i + 1] = Global.mUserName[i];
		}
		Global.mScore[nNum] = ntemp;
		Global.mUserName[nNum] = stemp;
	}
	
	public void DisplayInfo_()
	{
		// TODO Auto-generated method stub
		String str;
		
		if(Global.g_nPUserNum > 1)
			reSortInfo_();
		for(int i = 0; i < Global.g_nPUserNum; i++)
		{
			str = String.format(Global.mpUserName[i]);
			Label lblScore = Label.label(str, "Marker Felt", 17);
			lblScore.setPosition(0.3f * Global.rWidth, 0.56f * Global.rHeight - i * 18 * fy);
			lblScore.setScaleX(fx); lblScore.setScaleY(fy);
			addChild(lblScore);
			lblScore.setColor(new CCColor3B(255, 255, 0));
			
			str = String.format("%d", Global.mpScore[i]);
			Label lblScore_ = Label.label(str, "Marker Felt", 17);
			lblScore_.setPosition(0.6f * Global.rWidth, 0.56f * Global.rHeight - i * 18 * fy);
			lblScore_.setScaleX(fx); lblScore_.setScaleY(fy);
			addChild(lblScore_);
			lblScore_.setColor(new CCColor3B(255, 255, 0));
		}
		if(Global.g_nPUserNum == GameConfig.MAX_USER_NUM)
			Global.g_nPUserNum--;
	}

	public void reSortInfo_() 
	{
		// TODO Auto-generated method stub
		String stemp;
		int ntemp;
		int nNum = Global.g_nPUserNum;
		
		ntemp = Global.mpScore[Global.g_nPUserNum];
		stemp = Global.mpUserName[Global.g_nPUserNum];
		for(int i = 0; i < Global.g_nPUserNum - 1; i++)
		{
			if(ntemp > Global.mpScore[i])
			{
				nNum = i;
				break;
			}
		}
		for(int i = Global.g_nPUserNum - 1; i > nNum - 1; i-- )
		{
			Global.mpScore[i + 1] = Global.mpScore[i];
			Global.mpUserName[i + 1] = Global.mpUserName[i];
		}
		Global.mpScore[nNum] = ntemp;
		Global.mpUserName[nNum] = stemp;
	}
	
	
}
