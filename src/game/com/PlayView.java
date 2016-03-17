/**
 * 
 */
package game.com;

import org.cocos2d.actions.interval.RotateBy;
import org.cocos2d.events.TouchDispatcher;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.Menu;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.nodes.TextureManager;
import org.cocos2d.types.CCColor3B;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCRect;

import android.view.MotionEvent;

/**
 * @author Administrator
 *
 */
public class PlayView extends Layer
{
	static Sprite[] m_dartSprite = new Sprite[6];
	static Sprite[] m_dartflySprite = new Sprite[3];
	static Sprite[] m_balloonSprite = new Sprite[GameConfig.BALLOON_NUM];
	static Sprite[] m_scoreSprite = new Sprite[5];
	static Sprite m_dartselectedSprite, m_reticleSprite, m_placardSprite,
					m_loadingSprite, m_maxScoreSprite, m_timeBonusSprite,
					m_matchSprite, m_targetSprite;
	
	static MenuItemImage m_btnMenu;
	static MenuItemImage btnQuick, btnProgressive, btnBoardWalk, btnHighScores1, btnHighScores2;
	static Menu m_menu;
	
	static Label m_lblTime, m_lblScore, m_lblLCD1, m_lblLCD2, m_lblLCD3, m_lblLCD4,
					m_lblLCDScore;
	
	static CCRect[] m_dartRect = new CCRect[6];
	static CCRect[] m_balloonRect = new CCRect[GameConfig.BALLOON_NUM];
	
	static CCPoint m_ptStart = CCPoint.ccp(0, 0), m_ptPrevTouchPoint, m_ptCurrTouchPoint;
	
	static int initOpacity;
	
	static boolean m_fMatch = false, m_fGameStart = false, m_fLCD = false;
	static boolean[] m_fTouchBeganed = new boolean[6];
	static boolean[] m_fTouchEnded = new boolean[6];
	
	static int[] m_nBalloonIndex  = new int[GameConfig.BALLOON_NUM];
	static int[] m_nBurstAniCount = new int[GameConfig.BALLOON_NUM];
	static int m_nTargetPos;
	static int m_nPrevTargetPos;
	static int num;
	static int m_nHideDartNum;
	static int signX, signY;
	static int[] m_nThrust = new int[GameConfig.BALLOON_NUM];
	static int[] m_nSign = new int[GameConfig.BALLOON_NUM];
	static int[] m_nIndex = new int[3];
	
	static float m_rTime, m_rLCDTime, m_rTargetX, m_rTargetY, m_rMaxHeight, m_rGlobalTime,
					m_rHeightTime, m_rVx, m_rVy, m_rDelayTime;
	static float[] m_rAlpha = new float[GameConfig.BALLOON_NUM];
	static float fx, fy;
	static boolean bgamestart = false;
	
	public PlayView()
	{
		Global.g_fProgressivePlay = false;
		setIsTouchEnabled(false);
		if(Global.m_mainSound != null && Global.m_mainSound.isPlaying() != true)
			Global.m_mainSound.start();
		loadSprite();
	}
	
	@Override
	public void onExit()
	{
		TextureManager.sharedTextureManager().removeAllTextures();
		this.removeAllChildren(true);
		this.cleanup();
		System.gc();
	}

	public void initParam()
	{
		Global.g_nTotalScore = 0;
		m_fLCD = false;
		m_fMatch = false;
		m_fGameStart = false;
		
		int i;
		int nballoonIndex;
		String pszImageName = null;
		Math.random();
		for (i = 0; i < GameConfig.BALLOON_NUM; i++) {
			nballoonIndex = (int) (Math.random() * 100 % 27);
			m_nBalloonIndex[i] = nballoonIndex;
			switch (nballoonIndex) {
				case 0:
				case 1:
				case 2:
				case 3:
					if (Global.g_fProgressivePlay) 
						pszImageName = String.format("gfx/bluestar.gif");
					else
						pszImageName = String.format("gfx/blueballoon.gif");
					
					break;
				case 4:
				case 5:
				case 6:
				case 7:
					if (Global.g_fProgressivePlay) 
						pszImageName = String.format("gfx/greenstar.gif");
					else
						pszImageName = String.format("gfx/greenballoon.gif");
					
					break;
				case 8:
				case 9:
				case 10:
				case 11:
					if (Global.g_fProgressivePlay) 
						pszImageName = String.format("gfx/tealstar.gif");
					else
						pszImageName = String.format("gfx/tealballoon.gif");
					
					break;
				case 12:
				case 13:
				case 14:
				case 15:
					if (Global.g_fProgressivePlay) 
						pszImageName = String.format("gfx/magstar.gif");
					else
						pszImageName = String.format("gfx/magballoon.gif");
					
					break;
				case 16:
				case 17:
				case 18:
				case 19:
					if (Global.g_fProgressivePlay) 
						pszImageName = String.format("gfx/purplestar.gif");
					else
						pszImageName = String.format("gfx/purpleballoon.gif");
					
					break;
				case 20:
				case 21:
				case 22:
				case 23:
					if (Global.g_fProgressivePlay) 
						pszImageName = String.format("gfx/redstar.gif");
					else
						pszImageName = String.format("gfx/redballoon.gif");
					
					break;
				case 24:
					if (Global.g_fProgressivePlay) 
						pszImageName = String.format("gfx/leopardstar.gif");
					else
						pszImageName = String.format("gfx/leopardballoon.gif");
					
					break;
				case 25:
					if (Global.g_fProgressivePlay) 
						pszImageName = String.format("gfx/tigerstar.gif");
					else
						pszImageName = String.format("gfx/tigerballoon.gif");
					
					break;
				case 26:
					if (Global.g_fProgressivePlay) 
						pszImageName = String.format("gfx/zebrastar.gif");
					else
						pszImageName = String.format("gfx/zebraballoon.gif");
					
					break;
				default:
					break;
			}
			
			m_balloonSprite[i] = Sprite.sprite(pszImageName);
			m_balloonSprite[i].setPosition(93.8f * fx + (i % 8) * 40 * fx, 
											280 * fy - (int)(i / 8) * 53.4f * fy);
			m_balloonSprite[i].setScaleX(fx); m_balloonSprite[i].setScaleY(fy);
			addChild(m_balloonSprite[i], 1);
			
			m_balloonSprite[i].setAnchorPoint(0.5f, 1.0f);
			
			m_balloonRect[i] = CCRect.make(m_balloonSprite[i].getPositionX() - GameConfig.BALLOON_REAL_WIDTH * fx / 2.0f, 
										  m_balloonSprite[i].getPositionY() - 80 * fy,
										  GameConfig.BALLOON_REAL_WIDTH * fx, (GameConfig.BALLOON_REAL_HEIGHT+6) * fy);			
			
			int ntemp = (int)((float) (Math.random()*10))%2;
			m_nSign[i] = (int) Math.pow(-1, ntemp);
			m_rAlpha[i] = (float) (m_nSign[i] * 10 * Math.random() % 5);
			
			m_nThrust[i] = 0;
		}
		
		for (i = 0; i < 3; i++) 
		{
			pszImageName = String.format("gfx/dart1.gif");
			m_dartflySprite[i] = Sprite.sprite(pszImageName);
			m_dartflySprite[i].setPosition(-30, -30);
			m_dartflySprite[i].setScaleX(fx); m_dartflySprite[i].setScaleY(fy);
			addChild(m_dartflySprite[i], 2);
			m_dartflySprite[i].setVisible(false);	
		}
		
		m_lblLCD1 = Label.label("POP BALLOONS AND SCORE AS MANY POINT",
								"Marker Felt",
								GameConfig.FONT_SIZE);
		m_lblLCD1.setPosition(Global.rWidth, GameConfig.LCD_POSITION_Y * fy);
		m_lblLCD1.setColor(new CCColor3B(255, 0, 255));
		m_lblLCD1.setScaleX(fx); m_lblLCD1.setScaleY(fy);
		addChild(m_lblLCD1, 0);
		
		m_lblLCD2 = Label.label("READY", "Marker Felt", GameConfig.FONT_SIZE);
		m_lblLCD2.setPosition(Global.rWidth / 2, GameConfig.LCD_POSITION_Y * fy);
		m_lblLCD2.setColor(new CCColor3B(255, 255, 0));
		m_lblLCD2.setScaleX(fx); m_lblLCD2.setScaleY(fy);
		addChild(m_lblLCD2, 0);		
		m_lblLCD2.setVisible(false);
		
		m_lblLCD3 = Label.label("SET", "Marker Felt", GameConfig.FONT_SIZE);
		m_lblLCD3.setPosition(Global.rWidth / 2, GameConfig.LCD_POSITION_Y * fy);		
		m_lblLCD3.setColor(new CCColor3B(255, 255, 0));
		m_lblLCD3.setScaleX(fx); m_lblLCD3.setScaleY(fy);
		addChild(m_lblLCD3, 0);				
		m_lblLCD3.setVisible(false);
		
		m_lblLCD4 = Label.label("GO!", "Marker Felt", GameConfig.FONT_SIZE);
		m_lblLCD4.setPosition(Global.rWidth / 2, GameConfig.LCD_POSITION_Y * fy);		
		m_lblLCD4.setColor(new CCColor3B(255, 255, 0));
		m_lblLCD4.setScaleX(fx); m_lblLCD4.setScaleY(fy);
		addChild(m_lblLCD4, 0);				
		m_lblLCD4.setVisible(false);

		m_lblLCDScore = Label.label("   ", "Marker Felt", GameConfig.FONT_SIZE);
		m_lblLCDScore.setPosition(Global.rWidth / 2, GameConfig.LCD_POSITION_Y * fy);		
		m_lblLCDScore.setColor(new CCColor3B(255, 255, 0));
		m_lblLCDScore.setScaleX(fx); m_lblLCDScore.setScaleY(fy);
		addChild(m_lblLCDScore, 0);	
		m_lblLCDScore.setVisible(false);
		
		m_rTime = 0.0f;
		m_lblTime.setColor(new CCColor3B(255, 255, 255));
		Global.g_nTotalScore = 0;
		for (i = 0; i < 3; i++) {
			m_nIndex[i] = 7;
		}
		m_rDelayTime = 0;

		m_rLCDTime = 0.0f;
		
		for (i = 0; i < 6; i++) {
			m_dartSprite[i].setVisible(true);			
		}	
		
		schedule("balloonShake", 0.05f);
	}
	
	public void gameOver()
	{
		int i;

		unschedule("balloonShake");
		if (m_fLCD)
			unschedule("lcdTimer");
		
		if (m_lblLCD1 != null && m_lblLCD2 != null && m_lblLCD3 != null &&
				m_lblLCD4 != null && m_lblLCDScore != null) 
		{
			removeChild(m_lblLCD1, true);
			removeChild(m_lblLCD2, true);
			removeChild(m_lblLCD3, true);
			removeChild(m_lblLCD4, true);
			m_lblLCD1.cleanup();
			m_lblLCD2.cleanup();
			m_lblLCD3.cleanup();
			m_lblLCD4.cleanup();
		}

		if (m_lblLCDScore != null) 
		{
			removeChild(m_lblLCDScore, true);
			m_lblLCDScore.cleanup();
		}
		
		for (i = 0; i < GameConfig.BALLOON_NUM; i++) 
		{
			if (m_balloonSprite[i] != null)
			{
				removeChild(m_balloonSprite[i], true);
				m_balloonSprite[i].cleanup();
			}
		}	
		for (i = 0; i < 3; i++) {
			if (m_dartflySprite[i] != null)
			{
				removeChild(m_dartflySprite[i], true);
				m_dartflySprite[i].cleanup();
			}
		}	
			
		for (i = 0; i < 6; i++) {
			m_fTouchEnded[i] = false;		
		}
		
		if (Global.g_fProgressivePlay) {
			if (m_targetSprite != null) 
			{
				removeChild(m_targetSprite, true);
				m_targetSprite.cleanup();
			}
		}
	}
	
	public void setTargetPos()
	{
		float rDeltaX, rDeltaY;
		
		rDeltaX = (m_ptCurrTouchPoint.x - m_ptPrevTouchPoint.x);
		rDeltaY = Math.abs(m_ptCurrTouchPoint.y - m_ptPrevTouchPoint.y);

		if (rDeltaX < 0.0f)
			signX = -1;
		else 
			signX = 1;
			
		if (Math.abs(rDeltaX) >= GameConfig.MAX_DELTA_X * fx) {
			rDeltaX = signX * GameConfig.MAX_DELTA_X * fx;
		}
		if (rDeltaY >= GameConfig.MAX_DELTA_Y * fy) {
			rDeltaY = GameConfig.MAX_DELTA_Y * fy;
		}
		
		m_rTargetX = m_reticleSprite.getPositionX() + ((m_reticleSprite.getWidth()-5) * fx / 2) * (rDeltaX / (GameConfig.MAX_DELTA_X * fx));
		m_rTargetY = (float) (m_reticleSprite.getPositionY() + (m_reticleSprite.getHeight()-/*53*/10) * fy * (rDeltaY / (GameConfig.MAX_DELTA_Y * fy) - 0.5*fy));
		if (m_rTargetX <= GameConfig.WAND_LEFT_X * fx)
			m_rTargetX = GameConfig.WAND_LEFT_X * fx;
		else if (m_rTargetX >= GameConfig.WAND_RIGHT_X * fx)
			m_rTargetX = GameConfig.WAND_RIGHT_X * fx;
		
		if (m_rTargetY <= GameConfig.WAND_BOTTOM_Y * fy)
			m_rTargetY = GameConfig.WAND_BOTTOM_Y * fy;
		else if (m_rTargetY >= GameConfig.WAND_TOP_Y * fy)
			m_rTargetY = GameConfig.WAND_TOP_Y * fy;
		
		m_rMaxHeight = m_reticleSprite.getPositionY() + m_reticleSprite.getHeight() * fy / 2 - m_dartSprite[0].getPositionY();
			
		m_rHeightTime = (float) Math.sqrt(2 * m_rMaxHeight / (float)(GameConfig.G*fy));
		m_rVy = GameConfig.G * fy * m_rHeightTime;
		m_rVx = rDeltaX * 3 * fx;
	}

	public void initDartState()
	{
		String strTemp;
		num++;
		if (num == 3) {
			num = 0;
		}
		
		m_dartselectedSprite.setVisible(false);	
		m_reticleSprite.setVisible(false);		

		

		strTemp = String.format("gfx/dart1.gif");
		int ntag = m_dartflySprite[num].getZOrder();
		removeChild(m_dartflySprite[num], true);
		m_dartflySprite[num] = null;
		m_dartflySprite[num] = Sprite.sprite(strTemp);
		m_dartflySprite[num].setPosition(m_ptStart.x, m_ptStart.y);
		m_dartflySprite[num].setScaleX(fx); 
		m_dartflySprite[num].setScaleY(fy); 
		addChild(m_dartflySprite[num], ntag);
		m_rGlobalTime = 0.0f;
		
		if(Global.m_throwSound != null)
			Global.m_throwSound.start();
	}
	
	public void flyDart(int ix)
	{
		String strTemp;
		
		m_dartflySprite[num].setVisible(true);
		m_rGlobalTime += GameConfig.DART_INTERVAL;
		float h = m_rVy * m_rGlobalTime - (GameConfig.G * fy * m_rGlobalTime * m_rGlobalTime) / 2;
		
		m_dartflySprite[num].setPosition(m_ptStart.x + m_rVx, m_dartSprite[0].getPositionY() + h);
		if (m_dartflySprite[num].getPositionX() >= m_rTargetX && signX == 1)
			m_rVx = -GameConfig.DECELERATION_X * fx;
		else if (m_dartflySprite[num].getPositionX() <= m_rTargetX && signX == -1) 
			m_rVx = GameConfig.DECELERATION_X * fx;
		
		if (m_rGlobalTime <= m_rHeightTime && m_dartflySprite[num].getPositionY() >= m_rMaxHeight - 27 * fy)
		{
			strTemp = String.format("gfx/dart2.gif");
			int ntag = m_dartflySprite[num].getZOrder();
			float rTemp_x = m_dartflySprite[num].getPositionX();
			float rTemp_y = m_dartflySprite[num].getPositionY();
			float rScal_x = m_dartflySprite[num].getScaleX();
			float rScal_y = m_dartflySprite[num].getScaleY();
			removeChild(m_dartflySprite[num], true);
			m_dartflySprite[num] = null;
			m_dartflySprite[num] = Sprite.sprite(strTemp);
			m_dartflySprite[num].setPosition(rTemp_x, rTemp_y);
			m_dartflySprite[num].setScaleX(rScal_x); 
			m_dartflySprite[num].setScaleY(rScal_y); 
			addChild(m_dartflySprite[num], ntag);
			
		}
		if (m_rGlobalTime <= m_rHeightTime && m_dartflySprite[num].getPositionY() >= m_rMaxHeight + m_dartSprite[0].getPositionY()-9 * fy)
		{
			strTemp = String.format("gfx/dart3.gif");
			int ntag = m_dartflySprite[num].getZOrder();
			float rTemp_x = m_dartflySprite[num].getPositionX();
			float rTemp_y = m_dartflySprite[num].getPositionY();
			float rScal_x = m_dartflySprite[num].getScaleX();
			float rScal_y = m_dartflySprite[num].getScaleY();
			removeChild(m_dartflySprite[num], true);
			m_dartflySprite[num] = null;
			m_dartflySprite[num] = Sprite.sprite(strTemp);
			m_dartflySprite[num].setPosition(rTemp_x, rTemp_y);
			m_dartflySprite[num].setScaleX(rScal_x); 
			m_dartflySprite[num].setScaleY(rScal_y); 
			addChild(m_dartflySprite[num], ntag);
			
		}
		if (m_rGlobalTime >= m_rHeightTime && m_dartflySprite[num].getPositionY() <= m_rTargetY) 
		{
			m_dartflySprite[num].setPosition(m_dartflySprite[num].getPositionX(), m_rTargetY);
			CCPoint hitPos = CCPoint.make(m_dartflySprite[num].getPositionX(),
										m_rTargetY - GameConfig.DART_REAL_HEIGHT * fy / 2);
			for (int i = 0; i < GameConfig.BALLOON_NUM; i++)
			{
				
				if (CCRect.containsPoint(m_balloonRect[i], hitPos)) 
				{
					m_nThrust[i]++;//?
					if (m_nPrevTargetPos == i) {
						m_fMatch = true;
					}
				}
			}
			m_fTouchEnded[ix] = false;
			
			if(Global.m_dartHitSound != null)
				Global.m_dartHitSound.start();
			for (int i = 0; i < 6; i++)
			{
				m_dartSprite[i].setVisible(true);			
			}	
		}
		float r_x = GameConfig.DART_SCALE_RATE * m_dartflySprite[num].getScaleX();
		float r_y = GameConfig.DART_SCALE_RATE * m_dartflySprite[num].getScaleY();
		m_dartflySprite[num].setScaleX(r_x); 
		m_dartflySprite[num].setScaleY(r_y);
	}
	
	public void getProScore()
	{
		m_matchSprite.setPosition(m_balloonSprite[m_nPrevTargetPos].getPositionX(),
									m_balloonSprite[m_nPrevTargetPos].getPositionY());
		m_matchSprite.setOpacity(initOpacity);	
		m_lblLCDScore.setString("Match!");
		m_lblLCDScore.setVisible(true);
		m_lblLCDScore.stopAllActions();
		m_lblLCDScore.runAction(RotateBy.action(0.5f, 360));
		
		Global.g_nTotalScore += 100; 
		
		String str = String.format("%d", Global.g_nTotalScore);
		m_lblScore.setString(str);
	}
	
	public void getScore(int nIndex, int nPos)
	{
		int nScore;	
		String str = null;

		if (nIndex < 24) {
			Math.random();
			nScore = (int) (((10*Math.random())% 5) * 10 + 10);
			if (nScore == 50) {
				nScore *= (Math.random()*10 % 2) + 1; 
			};
			Global.g_nTotalScore += nScore; 
			
			str = String.format("%d POINTS!", nScore);
			m_lblLCDScore.setString(str);
			
			if (nScore == 100) {
				
				if(nPos < GameConfig.BALLOON_NUM)
				{
					m_maxScoreSprite.setPosition(m_balloonSprite[nPos].getPositionX(),
							m_balloonSprite[nPos].getPositionY());
					m_maxScoreSprite.setOpacity(initOpacity);
				}
			}
			else {
				int idx = nScore/10-1;
				
				if(idx >= 0 && idx < 5)
				{
					if(nPos < GameConfig.BALLOON_NUM)
					{
						m_scoreSprite[idx].setPosition(m_balloonSprite[nPos].getPositionX(),
								m_balloonSprite[nPos].getPositionY());
						m_scoreSprite[idx].setOpacity(initOpacity);
					}
				}
			}
			//Global.m_scoreSound.start();
		}
		else {
			if (nIndex == 24)
				m_rTime -= 5;
			if (nIndex == 25)
				m_rTime -= 7;
			if (nIndex == 26)
				m_rTime -= 10;
				
			str = String.format("Time BONUS!");
			m_lblLCDScore.setString(str);
			
			m_timeBonusSprite.setPosition(m_balloonSprite[nPos].getPositionX(), 
										m_balloonSprite[nPos].getPositionY());
			m_timeBonusSprite.setOpacity(initOpacity);
			//Global.m_bonusSound.start();
		}

		str = String.format("%d", Global.g_nTotalScore);
		m_lblScore.setString(str);	
		m_lblLCDScore.setVisible(true);
		m_lblLCDScore.stopAllActions();
		m_lblLCDScore.runAction(RotateBy.action( 0.5f, 360));
	}
	
	public void balloonShake(float dt)
	{
		int i;
		Math.random();
		for (i = 0; i < GameConfig.BALLOON_NUM; i++)
		{
			if (m_rAlpha[i] <  -5)/*(Math.random()*10 % 10))*/ 
			{
				m_nSign[i] = 1;
			}
			if (m_rAlpha[i] > 5)/*Math.random()*10 % 10)*/ 
			{
				m_nSign[i] = -1;
			}
			
			m_rAlpha[i] += m_nSign[i] * 1;
			m_balloonSprite[i].setRotation(m_rAlpha[i]);
		}
	}
	
	public void effectPlacard(float dt)
	{
		m_placardSprite.setPosition(m_placardSprite.getPositionX(),
				m_placardSprite.getPositionY() + 17.5f * Global.rScale_Y);
		m_menu.setPosition(m_menu.getPositionX(),
			   m_menu.getPositionY() + 17.5f * Global.rScale_Y);
		btnQuick.setPosition(btnQuick.getPositionX(),
		btnQuick.getPositionY() + 17.5f * Global.rScale_Y);
		btnProgressive.setPosition(btnProgressive.getPositionX(),
		btnProgressive.getPositionY() + 17.5f * Global.rScale_Y);
		btnBoardWalk.setPosition(btnBoardWalk.getPositionX(),
		btnBoardWalk.getPositionY() + 17.5f * Global.rScale_Y);
		btnHighScores1.setPosition(btnHighScores1.getPositionX(),
		btnHighScores1.getPositionY() + 17.5f * Global.rScale_Y);
		btnHighScores2.setPosition(btnHighScores2.getPositionX(),
		btnHighScores2.getPositionY() + 17.5f * Global.rScale_Y);
		m_btnMenu.setPosition(m_btnMenu.getPositionX(),
				  m_btnMenu.getPositionY() - 2.5f * Global.rScale_Y);
		if (m_placardSprite.getPositionY() >= Global.rHeight + Global.rScale_Y*m_placardSprite.getHeight() / 2) 
		{
			setIsTouchEnabled(true);
			schedule("lcdTimer", 0.1f);
			m_fLCD = true;
			m_fGameStart = true;
			unschedule("effectPlacard");
		}
	}
	
	public void repeatPlacard(float dt)
	{
		m_placardSprite.setPosition(m_placardSprite.getPositionX(),
			 	m_placardSprite.getPositionY() - 17.5f * Global.rScale_Y);
		m_menu.setPosition(m_menu.getPositionX(),
				m_menu.getPositionY() - 17.5f * Global.rScale_Y);
		btnQuick.setPosition(btnQuick.getPositionX(),
		btnQuick.getPositionY() - 17.5f * Global.rScale_Y);
		btnProgressive.setPosition(btnProgressive.getPositionX(),
		btnProgressive.getPositionY() - 17.5f * Global.rScale_Y);
		btnBoardWalk.setPosition(btnBoardWalk.getPositionX(),
		btnBoardWalk.getPositionY() - 17.5f * Global.rScale_Y);
		btnHighScores1.setPosition(btnHighScores1.getPositionX(),
		btnHighScores1.getPositionY() - 17.5f * Global.rScale_Y);
		btnHighScores2.setPosition(btnHighScores2.getPositionX(),
		btnHighScores2.getPositionY() - 17.5f * Global.rScale_Y);
		m_btnMenu.setPosition(m_btnMenu.getPositionX(),
					m_btnMenu.getPositionY() + 2.5f * Global.rScale_Y);
		if (m_placardSprite.getPositionY() <= (Global.rHeight - m_placardSprite.getHeight() * fy / 2 + 29 * fy))
		{
			setIsTouchEnabled(false);
			gameOver();
			m_lblTime.setString("0:45");
			unschedule("repeatPlacard");
		}		
	}

	public void gameTimer(float dt)
	{
		m_nPrevTargetPos = m_nTargetPos;				

		int i;
		if (m_rTime >= 35.0f)
			m_lblTime.setColor(new CCColor3B(255, 0, 0));
		else
			m_lblTime.setColor(new CCColor3B(255, 255, 255));
		
		int nTime = (int) (45 - m_rTime);
		String str = String.format("0:%02d", nTime);
		m_lblTime.setString(str);
		m_rTime += dt;
		
		if (m_rTime >= 45.0f) {
			m_lblTime.setString("0:00");
			m_lblLCDScore.setString("GAME OVER");
			m_lblLCDScore.setVisible(true);
			
			if(Global.m_buttonSound != null)
				Global.m_buttonSound.start();
			m_fGameStart = false;
		}
		if (m_rTime > 47.0f) {
			if(Global.m_mainSound != null)
				Global.m_mainSound.pause();
			gameOver();
			gameTurn();
			return;
		}
		
		for (i = 0; i < 6; i++) {
			if (m_fTouchEnded[i]) {
				flyDart(i);
			}
			if (!(m_dartSprite[i].isVisible()))
			{
				m_nHideDartNum++;
				if (i == 5) {
					break;
				}
			}
		}
		
		if (m_nHideDartNum == 18) {
			m_rDelayTime = 0;
			for (i = 0; i < 6; i++) {
				if (!(m_dartSprite[i].isVisible())) 
				{
					for (int j = 0; j < 3; j++) {
						if (m_nIndex[j] == 7) {
							m_nIndex[j] = i;					
							break;
						}
					}
				}
			}
		}
		if (m_nHideDartNum >= 18) {
			m_rDelayTime += dt;
			if (m_rDelayTime > 1.5f) {
				for (i = 0; i < 3; i++) {
					if(m_nIndex[i] < 6)
						m_dartSprite[m_nIndex[i]].setVisible(true);
					m_nHideDartNum = 0;
					m_nIndex[i] = 7;
				}
			}
		}
		
		for (i = 0; i < GameConfig.BALLOON_NUM; i++) 
		{
			String strTemp = null;
			if (m_nThrust[i] == 1) 
			{
				switch (m_nBalloonIndex[i]) 
				{
					case 0:
					case 1:
					case 2:
					case 3:
					{
						strTemp = String.format("gfx/blueburst%d.gif", m_nBurstAniCount[i]+1);
						
						break;
					}
					case 4:
					case 5:
					case 6:
					case 7:
					{
						strTemp = String.format("gfx/greenburst%d.gif", m_nBurstAniCount[i]+1);
						
						break;
					}
					case 8:
					case 9:
					case 10:
					case 11:
					{
						strTemp = String.format("gfx/tealburst%d.gif", m_nBurstAniCount[i]+1);
						
						break;
					}
					case 12:
					case 13:
					case 14:
					case 15:
					{
						strTemp = String.format("gfx/magburst%d.gif", m_nBurstAniCount[i]+1);
						
						break;
					}
					case 16:
					case 17:
					case 18:
					case 19:
					{
						strTemp = String.format("gfx/purpleburst%d.gif", m_nBurstAniCount[i]+1);
						break;
					}
					case 20:
					case 21:
					case 22:
					case 23:
					{
						strTemp = String.format("gfx/redburst%d.gif", m_nBurstAniCount[i]+1);
						break;
					}
					case 24:
					{
						strTemp = String.format("gfx/leopardburst%d.gif", m_nBurstAniCount[i]+1);
						break;
					}
					case 25:
					{
						strTemp = String.format("gfx/tigerburst%d.gif", m_nBurstAniCount[i]+1);
						break;
					}
					case 26:
					{
						strTemp = String.format("gfx/zebraburst%d.gif", m_nBurstAniCount[i]+1);
						break;
					}
					default:
						break;
				}
				int ntag = m_balloonSprite[i].getZOrder();
				float rTemp_x = m_balloonSprite[i].getPositionX();
				float rTemp_y = 0.0f;
				if(m_nBurstAniCount[i] == 0)
					rTemp_y = m_balloonSprite[i].getPositionY() - 25*Global.rScale_Y;
				else
					rTemp_y = m_balloonSprite[i].getPositionY();
				float rScal_x = m_balloonSprite[i].getScaleX();
				float rScal_y = m_balloonSprite[i].getScaleY();
				removeChild(m_balloonSprite[i], true);
				m_balloonSprite[i].cleanup();
				m_balloonSprite[i] = Sprite.sprite(strTemp);
				m_balloonSprite[i].setScaleX(rScal_x); 
				m_balloonSprite[i].setScaleY(rScal_y); 
				m_balloonSprite[i].setPosition(rTemp_x, rTemp_y);
				addChild(m_balloonSprite[i], ntag);
				
				m_nBurstAniCount[i]++;	
				
				if (m_nBurstAniCount[i] == GameConfig.BALLOON_ANI_NUM)
				{
					m_nThrust[i]++;
					m_nBurstAniCount[i] = 0;
					
					if (Global.g_fProgressivePlay && !m_fMatch) 
					{
						m_lblLCDScore.setString("NO Match!");
						m_lblLCDScore.setVisible(true);
						m_lblLCDScore.stopAllActions();
						m_lblLCDScore.runAction(RotateBy.action(0.5f, 360));
					}
					else if (Global.g_fProgressivePlay && m_fMatch)
					{
						Math.random();
						while (true)
						{
							m_nTargetPos = (int) (Math.random()*100 % GameConfig.BALLOON_NUM);		
							if (m_nThrust[m_nTargetPos] > 1) 
								continue;
							else
								break;
						}	
						
						if(m_targetSprite != null)
						{
							m_targetSprite.setPosition(m_balloonSprite[m_nTargetPos].getPositionX(),
									m_balloonSprite[m_nTargetPos].getPositionY() - 25*Global.rScale_Y);
							m_targetSprite.setOpacity(initOpacity);
						}
						getProScore();
						m_fMatch = false;
					}
					else if (!Global.g_fProgressivePlay) {
						getScore(m_nBalloonIndex[i], i);
					}
					
					if(Global.m_canvasSound != null)
						Global.m_canvasSound.start();
				}
			}
		}
		
		for (i = 0; i < 5; i++) 
		{
			m_scoreSprite[i].setPosition(m_scoreSprite[i].getPositionX(),
							m_scoreSprite[i].getPositionY() + 2*fy);
			int rTemp = (int) (m_scoreSprite[i].getOpacity()*0.8);
			m_scoreSprite[i].setOpacity(rTemp);
	    }
		if(m_maxScoreSprite != null)
		{
			m_maxScoreSprite.setPosition(m_maxScoreSprite.getPositionX(), 
										m_maxScoreSprite.getPositionY() + 2*fy);
			m_maxScoreSprite.setOpacity((int) (0.8*m_maxScoreSprite.getOpacity()));
		}
		if(m_timeBonusSprite != null)
		{
			m_timeBonusSprite.setPosition(m_timeBonusSprite.getPositionX(), 
											m_timeBonusSprite.getPositionY() + 2*fy);
			m_timeBonusSprite.setOpacity((int) (0.8*m_timeBonusSprite.getOpacity()));
		}
		
		if (Global.g_fProgressivePlay) {
			m_targetSprite.setOpacity((int) (0.99*m_targetSprite.getOpacity()));
			if(m_matchSprite != null)
			{
				m_matchSprite.setPosition(m_matchSprite.getPositionX(), 
											m_matchSprite.getPositionY() + 2*fy);
				m_matchSprite.setOpacity((int) (0.8*m_matchSprite.getOpacity()));
			}
		}	
	}
	
	public void lcdTimer(float dt)
	{
		m_lblLCD1.setPosition(m_lblLCD1.getPositionX() - 14 * fx, m_lblLCD1.getPositionY());
		if (m_fTouchBeganed[0] || m_fTouchBeganed[1] || m_fTouchBeganed[2] || m_fTouchBeganed[3]
		     || m_fTouchBeganed[4] || m_fTouchBeganed[5]) 
		{
			m_rLCDTime += dt; 

			m_lblLCD1.setVisible(false);
			m_lblLCD2.setVisible(true);
			if (m_rLCDTime > 0.5 && m_rLCDTime < 1) {
				if(Global.m_goSound != null)
					Global.m_goSound.start();
			}
			if (m_rLCDTime > 1) {
				m_lblLCD2.setVisible(false);
				m_lblLCD3.setVisible(true);
			}
			if (m_rLCDTime > 2) {
				m_lblLCD3.setVisible(false);
				m_lblLCD4.setVisible(true);
			}
			if (m_rLCDTime > 3) {
				m_lblLCD4.setVisible(false);
				removeChild(m_lblLCD1, true);
				removeChild(m_lblLCD2, true);
				removeChild(m_lblLCD3, true);
				removeChild(m_lblLCD4, true);
				m_lblLCD1.cleanup();
				m_lblLCD2.cleanup();
				m_lblLCD3.cleanup();
				m_lblLCD4.cleanup();
				
				if(Global.m_goSound != null)
					Global.m_goSound.start();
				for (int i = 0; i < 6; i++)
					if (m_fTouchBeganed[i]) 
						m_fTouchBeganed[i] = false;
				
				schedule("gameTimer", GameConfig.DART_INTERVAL);
				m_fLCD = false;
				
				if (Global.g_fProgressivePlay) {
					Math.random();
					int ix = (int) (Math.random() % GameConfig.BALLOON_NUM) * 100;
					m_nTargetPos = m_nPrevTargetPos = ix;
					m_targetSprite = Sprite.sprite("gfx/ballglow.gif");
					m_targetSprite.setPosition(m_balloonSprite[ix].getPositionX(), 
												m_balloonSprite[ix].getPositionY() - 10.5f*fy);
					m_targetSprite.setScaleX(fx); m_targetSprite.setScaleY(fy);
					addChild(m_targetSprite, 4);
				}
				
				unschedule("lcdTimer");
			}
		}
	}
	@Override
	public boolean ccTouchesBegan(MotionEvent event) 
	{
		CCPoint convertedLocation = Director.sharedDirector().convertCoordinate(event.getX(), event.getY());
		
		for (int i = 0; i < 6;  i++)
		{
			boolean ftemp = m_dartSprite[i].isVisible();
			if (CCRect.containsPoint(m_dartRect[i], convertedLocation) && m_fGameStart && ftemp)
			{
				if (!m_fLCD)
				{
					m_ptStart = convertedLocation;
					m_ptPrevTouchPoint = m_ptCurrTouchPoint = convertedLocation;

					m_dartSprite[i].setVisible(false);
					
					m_dartselectedSprite.setPosition(m_dartSprite[i].getPositionX(), m_dartSprite[i].getPositionY());
					m_dartselectedSprite.setVisible(true);
					
					m_reticleSprite.setPosition(m_ptStart.x, m_ptStart.y + 210 * fy);
					m_reticleSprite.setVisible(true);	
					m_lblLCDScore.setVisible(false);
					bgamestart = true;
				}
				m_fTouchBeganed[i] = true;	
			}
		}
		return TouchDispatcher.kEventHandled;
	}
	
	@Override
	public boolean ccTouchesMoved(MotionEvent event) 
	{
		CCPoint convertedLocation = Director.sharedDirector().convertCoordinate(event.getX(), event.getY());
		if(bgamestart == true)
		{
			float moveDistX = convertedLocation.x - m_ptStart.x;
			float moveDistY = convertedLocation.y - m_ptStart.y;
			
			if (Math.abs(moveDistX) > 50 * fx) 
			{
				moveDistX = 50 * fx;       
			}
			if (Math.abs(moveDistY) > 60 * fy) {
				moveDistY = 60 * fy;
			}
			
			m_reticleSprite.setPosition(m_reticleSprite.getPositionX() + moveDistX / 2,
										m_reticleSprite.getPositionY() + moveDistY / 2);
			
			if (m_reticleSprite.getPositionY() >= 250 * fy)
				m_reticleSprite.setPosition(m_reticleSprite.getPositionX() + moveDistX / 5, 250 * fy);
			else if (m_reticleSprite.getPositionY() <= 107 * fy)
				m_reticleSprite.setPosition(m_reticleSprite.getPositionX() + moveDistX / 5, 107 * fy);
			if (m_reticleSprite.getPositionX() >= m_ptStart.x + 22.5f * fx)
				m_reticleSprite.setPosition(m_ptStart.x + 22.5f * fx, m_reticleSprite.getPositionY() + moveDistY / 5);
			else if (m_reticleSprite.getPositionX() <= m_ptStart.x - 22.5f * fx)
				m_reticleSprite.setPosition(m_ptStart.x - 22.5f * fx, m_reticleSprite.getPositionY() + moveDistY / 5);
			if (m_reticleSprite.getPositionX() >= GameConfig.WAND_RIGHT_X * fx)
				m_reticleSprite.setPosition(GameConfig.WAND_RIGHT_X * fx, m_reticleSprite.getPositionY() + moveDistY / 5);
			else if (m_reticleSprite.getPositionX() <= GameConfig.WAND_LEFT_X * fx)
				m_reticleSprite.setPosition(GameConfig.WAND_LEFT_X * fx, m_reticleSprite.getPositionY() + moveDistY / 5);
			
			if (Math.abs(convertedLocation.x - m_ptStart.x) < 56 * fx && 
					Math.abs(convertedLocation.y - m_ptStart.y) < 50 * fy) 
			{//?
				m_ptPrevTouchPoint = m_ptCurrTouchPoint;
				m_ptCurrTouchPoint = convertedLocation;
			}
	//		else 
	//		{
	//			return false;
	//		}
		}
		return TouchDispatcher.kEventIgnored;
	}
	
	@Override
	public boolean ccTouchesEnded(MotionEvent event)
	{
//		CCPoint convertedLocation = Director.sharedDirector().convertCoordinate(event.getX(), event.getY());
		if(bgamestart == true)
		{
			for (int i = 0; i < 6;  i++)
			{
				if (m_fTouchBeganed[i] && !m_fLCD)
				{
					m_fTouchEnded[i] = true;	
					m_fTouchBeganed[i] = false;
					
					setTargetPos();
					initDartState();
				}
			}
			bgamestart = false;
		}
		System.gc();
		return TouchDispatcher.kEventIgnored;
	}
	
	public void gameTurn()
	{
		System.gc();
		unschedule("gameTimer");
		Global.g_fGameDone = true;
		if(Global.g_fProgressivePlay)
		{
			Global.mpScore[Global.g_nPUserNum] = Global.g_nTotalScore;
		}else
		{
			Global.mScore[Global.g_nUserNum] = Global.g_nTotalScore;
		}
		if(Global.m_mainSound.isPlaying())
			Global.m_mainSound.stop();
		Scene scene = Scene.node();
		scene.addChild(new ScoreView(), 1);
		Director.sharedDirector().replaceScene(BalloonDarts.newScene(GameConfig.TRANSITION_DURATION, 1, scene));
	}
	
	public void actionMenu()
	{
		if(Global.m_buttonSound != null)
			Global.m_buttonSound.start();
		schedule("repeatPlacard", 0.05f);
		
	}
	
	public void actionQuickPlay()
	{
		
		Global.g_fProgressivePlay = false;
		if(Global.m_buttonSound != null &&
				Global.m_buttonSound.isPlaying() != true)
			Global.m_buttonSound.start();
		schedule("effectPlacard", 0.05f);
		initParam();

	}
	
	public void actionProgressivePlay()
	{
		Global.g_fProgressivePlay = true;
		
		if(Global.m_buttonSound != null)
			Global.m_buttonSound.start();
		initParam();
		schedule("effectPlacard", 0.05f);
		
	}
	
	public void actionBoardWalk()
	{
		System.gc();
		
		if(Global.m_buttonSound != null)
			Global.m_buttonSound.start();
		Scene scene = Scene.node();
		scene.addChild(new PersonScore(), 1);
		Director.sharedDirector().replaceScene(BalloonDarts.newScene(GameConfig.TRANSITION_DURATION, 3, scene));
		if(Global.m_mainSound.isPlaying())	
			Global.m_mainSound.stop();
		
	}
	
	public void actionHighScore1()
	{
		System.gc();
		
		if(Global.m_buttonSound != null)
			Global.m_buttonSound.start();
		Scene scene = Scene.node();
		scene.addChild(new HighScore(), 1);
		Director.sharedDirector().replaceScene(BalloonDarts.newScene(GameConfig.TRANSITION_DURATION, 5, scene));
		if(Global.m_mainSound.isPlaying())	
			Global.m_mainSound.stop();
	}
	
	public void actionHighScore2()
	{
		System.gc();
		
		if(Global.m_buttonSound != null)
			Global.m_buttonSound.start();
		Global.g_fProgressivePlay = true;
		Scene scene = Scene.node();
		scene.addChild(new HighScore(), 1);
		Director.sharedDirector().replaceScene(BalloonDarts.newScene(GameConfig.TRANSITION_DURATION, 5, scene));
		if(Global.m_mainSound.isPlaying())	
			Global.m_mainSound.stop();
	}
	
	public void removeLoading(float dt)
	{
		removeChild(m_loadingSprite, true);
		m_loadingSprite.cleanup();
		
		btnQuick = MenuItemImage.item("gfx/quickplay_balloon.gif",
										"gfx/quickplay_over_balloon.gif",
										this,
										"actionQuickPlay");
		
		btnProgressive = MenuItemImage.item("gfx/progressiveplay_balloon.gif",
											"gfx/progressive_over_balloon.gif",
											this,
											"actionProgressivePlay");
		
		btnBoardWalk = MenuItemImage.item("gfx/boardwalk_balloon.gif",
											"gfx/boardwalk_over_balloon.gif",
											this,
											"actionBoardWalk");
		
		btnHighScores1 = MenuItemImage.item("gfx/hiscores_balloon.gif",
											"gfx/hiscores_over_balloon.gif",
											this,
											"actionHighScore1");
		
		btnHighScores2 = MenuItemImage.item("gfx/hiscores_balloon.gif",
											"gfx/hiscores_over_balloon.gif",
											this,
											"actionHighScore2");
		
		
		m_menu = Menu.menu(btnQuick, btnProgressive, btnBoardWalk, btnHighScores1, btnHighScores2);
		m_menu.setPosition( 0, 0 );
		btnQuick.setPosition(190 * fx, 230 * fy);
		btnProgressive.setPosition(210 * fx, 190 * fy);
		btnBoardWalk.setPosition(210 * fx, 150 * fy);
		btnHighScores1.setPosition(340 * fx, 230 * fy);
		btnHighScores2.setPosition(340 * fx, 150 * fy);
		btnQuick.setScaleX(fx); btnQuick.setScaleY(fy);
		btnProgressive.setScaleX(fx); btnProgressive.setScaleY(fy);
		btnBoardWalk.setScaleX(fx); btnBoardWalk.setScaleY(fy);
		btnHighScores1.setScaleX(fx); btnHighScores1.setScaleY(fy);
		btnHighScores2.setScaleX(fx); btnHighScores2.setScaleY(fy);
		addChild(m_menu, 2);	
		
		unschedule("removeLoading");
	}
	
	public void loadSprite()
	{
		int i = 0;
		fx = Global.rScale_X;
		fy = Global.rScale_Y;
		
		Sprite bgSprite = Sprite.sprite("gfx/dartboothbkg.gif");
		bgSprite.setPosition(Global.rWidth / 2, Global.rHeight / 2);
		bgSprite.setScaleX(fx); bgSprite.setScaleY(fy);
		addChild(bgSprite, 1);
		
		for (i = 0; i < 6; i++) {
 			m_dartSprite[i] = Sprite.sprite("gfx/dartunlit.gif");
			m_dartSprite[i].setPosition(0.22f * Global.rWidth + i * 58 * fx, 0.15f * Global.rHeight);
			m_dartSprite[i].setScaleX(fx); m_dartSprite[i].setScaleY(fy);
			addChild(m_dartSprite[i], 1);			
		}
		
		for (i = 0; i < 6; i++) {
			m_dartRect[i] = CCRect.make(m_dartSprite[i].getPositionX() - fx*m_dartSprite[i].getWidth() / 2.0f + 17 * fx, 
										m_dartSprite[i].getPositionY() - fy*m_dartSprite[i].getHeight() / 2.0f,
										fx*m_dartSprite[i].getWidth() - 40 * fx,
										fy*m_dartSprite[i].getHeight());
		}
		
		for (i = 0; i < 5; i++) {
			String str = String.format("gfx/dart%d0.gif", i+1);
			m_scoreSprite[i] = Sprite.sprite(str);
			m_scoreSprite[i].setPosition(-130, -130);
			m_scoreSprite[i].setScaleX(fx); m_scoreSprite[i].setScaleY(fy);
			addChild(m_scoreSprite[i], 3);			
		}
		m_maxScoreSprite = Sprite.sprite("gfx/dart100bonus.gif");
		m_maxScoreSprite.setPosition(-130, -130);
		m_maxScoreSprite.setScaleX(fx); m_maxScoreSprite.setScaleY(fy);
		addChild(m_maxScoreSprite, 3);			
		m_timeBonusSprite = Sprite.sprite("gfx/darttimebonus.gif");
		m_timeBonusSprite.setPosition(-130, -130);
		m_timeBonusSprite.setScaleX(fx); m_timeBonusSprite.setScaleY(fy);
		addChild(m_timeBonusSprite, 3);			
		m_matchSprite = Sprite.sprite("gfx/dartmatch.gif");
		m_matchSprite.setPosition(-130, -130);
		m_matchSprite.setScaleX(fx); m_matchSprite.setScaleY(fy);
		addChild(m_matchSprite, 3);			
		
		m_reticleSprite = Sprite.sprite("gfx/reticle.gif");
		m_reticleSprite.setPosition(0,0);
		m_reticleSprite.setScaleX(fx); m_reticleSprite.setScaleY(fy);
		addChild(m_reticleSprite, 3);
		m_reticleSprite.setVisible(false);
		initOpacity = m_reticleSprite.getOpacity();
		
		m_dartselectedSprite = Sprite.sprite("gfx/dart_selected.gif");
		m_dartselectedSprite.setPosition(m_dartSprite[0].getPositionX(), m_dartSprite[0].getPositionY());
		m_dartselectedSprite.setScaleX(fx); m_dartselectedSprite.setScaleY(fy);
		addChild(m_dartselectedSprite, 1);
		m_dartselectedSprite.setVisible(false);
		
		m_lblTime = Label.label("0:45",  "Marker Felt",  20);
		m_lblTime.setPosition(0.22f * Global.rWidth, GameConfig.LCD_POSITION_Y * fy);		
		m_lblTime.setScaleX(fx); m_lblTime.setScaleY(fy);
		addChild(m_lblTime, 1);		
		
		m_lblScore = Label.label("  0", "Marker Felt", 20);
		m_lblScore.setPosition(0.78f * Global.rWidth, GameConfig.LCD_POSITION_Y * fy);		
		m_lblScore.setScaleX(fx); m_lblScore.setScaleY(fy);
		addChild(m_lblScore, 1);		
		
		m_placardSprite = Sprite.sprite("gfx/placard_balloon.gif");
		m_placardSprite.setPosition(Global.rWidth / 2,
									Global.rHeight - m_placardSprite.getHeight() * fy / 2 + 29 * fy);
		m_placardSprite.setScaleX(fx); m_placardSprite.setScaleY(fy);
		addChild(m_placardSprite, 2);

		m_loadingSprite = Sprite.sprite("gfx/loading_balloon.gif");
		m_loadingSprite.setPosition(Global.rWidth / 2, Global.rHeight / 2 + 29 * fy);
		m_loadingSprite.setScaleX(fx); m_loadingSprite.setScaleY(fy);
		addChild(m_loadingSprite, 2);
		
		m_btnMenu = MenuItemImage.item("gfx/menu_btn_balloon.gif", 
										"gfx/menu_btn_balloon_over.gif",
										this,
										"actionMenu");
		
		Menu  menu = Menu.menu(m_btnMenu);
		menu.setPosition( 0, 0 );
		m_btnMenu.setPosition(m_btnMenu.getWidth() * fx / 2 + 12 * fx,
							Global.rHeight + m_btnMenu.getHeight() * fy / 2);
		m_btnMenu.setScaleX(fx); m_btnMenu.setScaleY(fy);
		addChild(menu, 1);	
		
		schedule("removeLoading", 2.0f);
		
	}
}
